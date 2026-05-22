package com.example.bssm_dev.domain.api.log.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class BodyTruncator {
    private static final int MAX_BODY_BYTES = 2048;

    private final ObjectMapper objectMapper;

    public record Result(String body, boolean truncated, Long contentLength) {}

    public Result truncate(Object body) {
        if (body == null) return new Result(null, false, 0L);
        if (body instanceof byte[] bytes) return truncateBytes(bytes);
        return truncateText(toText(body));
    }

    private Result truncateBytes(byte[] bytes) {
        if (!isValidUtf8(bytes)) {
            return new Result("[binary data]", false, (long) bytes.length);
        }
        return truncateText(new String(bytes, StandardCharsets.UTF_8));
    }

    private Result truncateText(String text) {
        if (text == null) return new Result(null, false, 0L);
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        if (bytes.length <= MAX_BODY_BYTES) {
            return new Result(text, false, (long) bytes.length);
        }
        return new Result(new String(bytes, 0, MAX_BODY_BYTES, StandardCharsets.UTF_8), true, (long) bytes.length);
    }

    private String toText(Object body) {
        if (body instanceof String s) return s;
        try {
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            return body.toString();
        }
    }

    private boolean isValidUtf8(byte[] bytes) {
        try {
            StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes));
            return true;
        } catch (CharacterCodingException e) {
            return false;
        }
    }
}
