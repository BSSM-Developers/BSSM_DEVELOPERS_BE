package com.example.bssm_dev.common.util;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * SSRF(Server-Side Request Forgery) 방어를 위한 도메인 검증 유틸리티.
 *
 * <p>검증 규칙:
 * <ol>
 *   <li>scheme은 https:// 만 허용</li>
 *   <li>숫자 IP 직접 입력 차단 (IPv4 / IPv6)</li>
 *   <li>내부 hostname 패턴 차단 (localhost, *.local, *.internal 등)</li>
 *   <li>DNS 해석 후 resolved IP가 내부 대역이면 차단</li>
 * </ol>
 */
public final class DomainValidator {

    private static final Logger log = Logger.getLogger(DomainValidator.class.getName());

    // RFC 1918 / loopback / link-local / CGNAT 패턴
    private static final List<long[]> BLOCKED_IPV4_RANGES = List.of(
            range("10.0.0.0", "10.255.255.255"),        // RFC 1918
            range("172.16.0.0", "172.31.255.255"),       // RFC 1918
            range("192.168.0.0", "192.168.255.255"),     // RFC 1918
            range("127.0.0.0", "127.255.255.255"),       // Loopback
            range("169.254.0.0", "169.254.255.255"),     // Link-local (AWS metadata 등)
            range("100.64.0.0", "100.127.255.255"),      // CGNAT
            range("0.0.0.0", "0.255.255.255"),           // 현재 네트워크
            range("192.0.0.0", "192.0.0.255"),           // IETF Protocol
            range("198.51.100.0", "198.51.100.255"),     // TEST-NET-2
            range("203.0.113.0", "203.0.113.255"),       // TEST-NET-3
            range("240.0.0.0", "255.255.255.255")        // Reserved / Broadcast
    );

    private static final Pattern IPV4_PATTERN =
            Pattern.compile("^(\\d{1,3}\\.){3}\\d{1,3}$");

    // IPv6 주소 직접 입력 차단 패턴 (::1, [::1], [2001:db8::1] 등)
    private static final Pattern IPV6_PATTERN =
            Pattern.compile("^\\[?[0-9a-fA-F:]+]?$");

    private static final List<Pattern> BLOCKED_HOSTNAME_PATTERNS = List.of(
            Pattern.compile("^localhost$", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*\\.local$", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*\\.internal$", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*\\.intranet$", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*\\.corp$", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*\\.lan$", Pattern.CASE_INSENSITIVE)
    );

    private DomainValidator() {
    }

    /**
     * 도메인 URL 검증. 위반 시 GlobalException 을 던집니다.
     *
     * @param domainUrl 검증할 URL 문자열 (예: https://api.example.com)
     */
    public static void validate(String domainUrl) {
        if (domainUrl == null || domainUrl.isBlank()) {
            throw InvalidDomainUrlException.raise();
        }

        URL url = parseUrl(domainUrl);

        validateScheme(url);
        String hostname = url.getHost();
        validateHostname(hostname);
        validateResolvedIp(hostname, domainUrl);
    }

    private static URL parseUrl(String domainUrl) {
        try {
            return new URI(domainUrl).toURL();
        } catch (URISyntaxException | IllegalArgumentException | java.net.MalformedURLException e) {
            log.warning("[DomainValidator] URL 파싱 실패: " + domainUrl);
            throw InvalidDomainUrlException.raise();
        }
    }

    private static void validateScheme(URL url) {
        if (!"https".equalsIgnoreCase(url.getProtocol())) {
            log.warning("[DomainValidator] https가 아닌 scheme 차단: scheme=" + url.getProtocol());
            throw InvalidDomainUrlException.raise();
        }
    }

    private static void validateHostname(String hostname) {
        if (hostname == null || hostname.isBlank()) {
            throw InvalidDomainUrlException.raise();
        }

        // IPv4 직접 입력 차단
        if (IPV4_PATTERN.matcher(hostname).matches()) {
            log.warning("[DomainValidator] IPv4 직접 입력 차단: " + hostname);
            throw BlockedInternalDomainException.raise();
        }

        // IPv6 직접 입력 차단
        if (IPV6_PATTERN.matcher(hostname).matches()) {
            log.warning("[DomainValidator] IPv6 직접 입력 차단: " + hostname);
            throw BlockedInternalDomainException.raise();
        }

        // 내부 hostname 패턴 차단
        for (Pattern pattern : BLOCKED_HOSTNAME_PATTERNS) {
            if (pattern.matcher(hostname).matches()) {
                log.warning("[DomainValidator] 내부 hostname 패턴 차단: " + hostname);
                throw BlockedInternalDomainException.raise();
            }
        }
    }

    private static void validateResolvedIp(String hostname, String domainUrl) {
        try {
            InetAddress[] addresses = InetAddress.getAllByName(hostname);
            for (InetAddress address : addresses) {
                byte[] raw = address.getAddress();

                // IPv6 loopback (::1) 및 링크로컬 (fe80::/10) 차단
                if (raw.length == 16) {
                    if (isBlockedIpv6(raw)) {
                        log.warning("[DomainValidator] 내부 IPv6 resolved 차단: hostname=" + hostname + ", ip=" + address.getHostAddress());
                        throw BlockedInternalDomainException.raise();
                    }
                    continue;
                }

                // IPv4 내부 대역 차단
                long ip = ipToLong(raw);
                for (long[] range : BLOCKED_IPV4_RANGES) {
                    if (ip >= range[0] && ip <= range[1]) {
                        log.warning("[DomainValidator] 내부 IPv4 resolved 차단: hostname=" + hostname + ", ip=" + address.getHostAddress());
                        throw BlockedInternalDomainException.raise();
                    }
                }
            }
        } catch (UnknownHostException e) {
            // DNS 해석 실패 = 존재하지 않는 도메인 → 차단
            log.warning("[DomainValidator] DNS 해석 실패 차단: hostname=" + hostname);
            throw InvalidDomainUrlException.raise();
        }
    }

    private static boolean isBlockedIpv6(byte[] raw) {
        // ::1 loopback
        boolean isLoopback = true;
        for (int i = 0; i < 15; i++) {
            if (raw[i] != 0) { isLoopback = false; break; }
        }
        if (isLoopback && raw[15] == 1) return true;

        // fe80::/10 link-local
        if ((raw[0] & 0xFF) == 0xFE && (raw[1] & 0xC0) == 0x80) return true;

        // fc00::/7 unique local
        if ((raw[0] & 0xFE) == 0xFC) return true;

        return false;
    }

    private static long ipToLong(byte[] raw) {
        return ((raw[0] & 0xFFL) << 24)
                | ((raw[1] & 0xFFL) << 16)
                | ((raw[2] & 0xFFL) << 8)
                | (raw[3] & 0xFFL);
    }

    private static long[] range(String start, String end) {
        try {
            long s = ipToLong(InetAddress.getByName(start).getAddress());
            long e = ipToLong(InetAddress.getByName(end).getAddress());
            return new long[]{s, e};
        } catch (UnknownHostException ex) {
            throw new IllegalStateException("IP 범위 초기화 실패: " + start + " - " + end, ex);
        }
    }

    // --- 예외 클래스 ---

    public static class InvalidDomainUrlException extends GlobalException {
        private InvalidDomainUrlException() {
            super(ErrorCode.INVALID_DOMAIN_URL);
        }

        public static InvalidDomainUrlException raise() {
            return new InvalidDomainUrlException();
        }
    }

    public static class BlockedInternalDomainException extends GlobalException {
        private BlockedInternalDomainException() {
            super(ErrorCode.BLOCKED_INTERNAL_DOMAIN);
        }

        public static BlockedInternalDomainException raise() {
            return new BlockedInternalDomainException();
        }
    }
}
