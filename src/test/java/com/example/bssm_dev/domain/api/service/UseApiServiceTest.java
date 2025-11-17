//package com.example.bssm_dev.domain.api.service;
//
//import com.example.bssm_dev.domain.api.dto.response.ProxyResponse;
//import com.example.bssm_dev.domain.api.exception.ApiTokenNotFoundException;
//import com.example.bssm_dev.domain.api.exception.EndpointNotFoundException;
//import com.example.bssm_dev.domain.api.exception.InvalidSecretKeyException;
//import com.example.bssm_dev.domain.api.model.Api;
//import com.example.bssm_dev.domain.api.model.ApiToken;
//import com.example.bssm_dev.domain.api.model.ApiUsage;
//import com.example.bssm_dev.domain.api.model.ApiUseReason;
//import com.example.bssm_dev.domain.api.model.key.ApiUsageId;
//import com.example.bssm_dev.domain.api.model.type.MethodType;
//import com.example.bssm_dev.domain.api.service.query.ApiTokenQueryService;
//import com.example.bssm_dev.domain.api.service.query.ApiUsageQueryService;
//import com.example.bssm_dev.domain.user.model.User;
//import com.example.bssm_dev.domain.user.model.type.UserRole;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
///**
// * UseApiService 테스트
// *
// * Token: e2f7df2d-b32f-4903-8dda-69857c019339
// * Secret: cb42fa582a6143989d0998e463e51c27
// *
// * 테스트 대상 API 엔드포인트:
// * - POST /objects
// * - GET /objects/{id}
// * - PUT /objects/{id}
// * - PATCH /objects/{id}
// * - DELETE /objects/{id}
// *
// * 참고: 이 테스트는 @SpringBootTest 대신 @ExtendWith(MockitoExtension.class)를 사용합니다.
// * 이유:
// * 1. UseApiService는 실제 DB를 직접 사용하지 않고 Query 서비스를 통해서만 접근
// * 2. Query 서비스는 모두 모킹되어 있음
// * 3. 실제 외부 API 호출만 수행하므로 Spring Context가 불필요
// * 4. 테스트 실행 속도가 훨씬 빠름 (Spring Context 로딩 불필요)
// *
// * 만약 실제 Spring Bean 주입 환경을 테스트하려면:
// * - @SpringBootTest 사용
// * - H2 in-memory DB 의존성 추가 (testImplementation 'com.h2database:h2')
// */
//@ExtendWith(MockitoExtension.class)
//class UseApiServiceTest {
//    @InjectMocks
//    private UseApiService useApiService;
//
//    @Mock
//    private ApiTokenQueryService apiTokenQueryService;
//
//    @Mock
//    private ApiUsageQueryService apiUsageQueryService;
//
//    private ApiToken apiToken;
//    private User user;
//
//    private static final String TOKEN = "e2f7df2d-b32f-4903-8dda-69857c019339";
//    private static final String SECRET_KEY = "cb42fa582a6143989d0998e463e51c27";
//    private static final String BASE_URL = "https://api.restful-api.dev";
//
//    @BeforeEach
//    void setUp() {
//        user = User.of(
//                "test@bssm.hs.kr",
//                "Test User",
//                "https://profile.image.url",
//                UserRole.ROLE_USER
//        );
//
//        apiToken = ApiToken.builder()
//                .apiTokenId(1L)
//                .user(user)
//                .apiTokenUUID(TOKEN)
//                .secretKey(SECRET_KEY)
//                .apiTokenName("Test Token")
//                .build();
//    }
//
//    @Nested
//    @DisplayName("유효성 검증 테스트")
//    class ValidationTests {
//
//        @Test
//        @DisplayName("유효한 토큰과 시크릿 키로 GET 요청 시 성공")
//        void get_WithValidTokenAndSecret_Success() {
//            // given
//            String endpoint = "/objects/7";
//            ApiUsage apiUsage = createApiUsage(endpoint, "GET", 3L);
//
//            when(apiTokenQueryService.findByTokenClientId(TOKEN))
//                    .thenReturn(apiToken);
//            when(apiUsageQueryService.findByTokenAndEndpoint(apiToken, endpoint, MethodType.GET))
//                    .thenReturn(apiUsage);
//
//            // when
//            ProxyResponse response = useApiService.get(SECRET_KEY, TOKEN, endpoint);
//
//            // then
//            assertNotNull(response);
//            verify(apiTokenQueryService).findByTokenClientId(TOKEN);
//            verify(apiUsageQueryService).findByTokenAndEndpoint(apiToken, endpoint, MethodType.GET);
//        }
//
//        @Test
//        @DisplayName("잘못된 Secret Key로 요청 시 InvalidSecretKeyException 발생")
//        void get_WithInvalidSecretKey_ThrowsException() {
//            // given
//            String endpoint = "/objects/3";
//            String invalidSecretKey = "invalid-secret-key";
//
//            when(apiTokenQueryService.findByTokenClientId(TOKEN))
//                    .thenReturn(apiToken);
//
//            // when & then
//            assertThrows(InvalidSecretKeyException.class, () ->
//                useApiService.get(invalidSecretKey, TOKEN, endpoint)
//            );
//
//            verify(apiTokenQueryService).findByTokenClientId(TOKEN);
//            verify(apiUsageQueryService, never()).findByTokenAndEndpoint(any(), any(), any());
//        }
//
//        @Test
//        @DisplayName("존재하지 않는 토큰으로 요청 시 ApiTokenNotFoundException 발생")
//        void get_WithNonExistentToken_ThrowsException() {
//            // given
//            String endpoint = "/objects/3";
//            String invalidToken = "invalid-token-uuid";
//
//            when(apiTokenQueryService.findByTokenClientId(invalidToken))
//                    .thenThrow(ApiTokenNotFoundException.raise());
//
//            // when & then
//            assertThrows(ApiTokenNotFoundException.class, () ->
//                useApiService.get(SECRET_KEY, invalidToken, endpoint)
//            );
//
//            verify(apiTokenQueryService).findByTokenClientId(invalidToken);
//            verify(apiUsageQueryService, never()).findByTokenAndEndpoint(any(), any(), any());
//        }
//
//        @Test
//        @DisplayName("존재하지 않는 엔드포인트로 요청 시 EndpointNotFoundException 발생")
//        void get_WithNonExistentEndpoint_ThrowsException() {
//            // given
//            String endpoint = "/nonexistent-endpoint";
//
//            when(apiTokenQueryService.findByTokenClientId(TOKEN))
//                    .thenReturn(apiToken);
//            when(apiUsageQueryService.findByTokenAndEndpoint(apiToken, endpoint, MethodType.GET))
//                    .thenThrow(EndpointNotFoundException.raise());
//
//            // when & then
//            assertThrows(EndpointNotFoundException.class, () ->
//                useApiService.get(SECRET_KEY, TOKEN, endpoint)
//            );
//
//            verify(apiTokenQueryService).findByTokenClientId(TOKEN);
//            verify(apiUsageQueryService).findByTokenAndEndpoint(apiToken, endpoint, MethodType.GET);
//        }
//    }
//
//    @Nested
//    @DisplayName("HTTP 메서드별 테스트")
//    class HttpMethodTests {
//
//        @Test
//        @DisplayName("GET /objects/{id} - 객체 조회")
//        void get_FetchObject_Success() {
//            // given
//            String endpoint = "/objects/3";
//            ApiUsage apiUsage = createApiUsage(endpoint, "GET", 3L);
//
//            when(apiTokenQueryService.findByTokenClientId(TOKEN))
//                    .thenReturn(apiToken);
//            when(apiUsageQueryService.findByTokenAndEndpoint(apiToken, endpoint, MethodType.GET))
//                    .thenReturn(apiUsage);
//
//            // when
//            ProxyResponse response = useApiService.get(SECRET_KEY, TOKEN, endpoint);
//
//            // then
//            assertNotNull(response);
//            assertNotNull(response.body());
//            verify(apiTokenQueryService).findByTokenClientId(TOKEN);
//            verify(apiUsageQueryService).findByTokenAndEndpoint(apiToken, endpoint, MethodType.GET);
//        }
//
//        @Test
//        @DisplayName("POST /objects - 새 객체 생성")
//        void post_CreateObject_Success() {
//            // given
//            String endpoint = "/objects";
//            Map<String, Object> requestBody = new HashMap<>();
//            requestBody.put("name", "Apple MacBook Pro 16");
//            requestBody.put("data", Map.of(
//                    "year", 2019,
//                    "price", 1849.99,
//                    "CPU model", "Intel Core i9",
//                    "Hard disk size", "1 TB"
//            ));
//
//            ApiUsage apiUsage = createApiUsage(endpoint, "POST", 2L);
//
//            when(apiTokenQueryService.findByTokenClientId(TOKEN))
//                    .thenReturn(apiToken);
//            when(apiUsageQueryService.findByTokenAndEndpoint(apiToken, endpoint, MethodType.POST))
//                    .thenReturn(apiUsage);
//
//            // when
//            ProxyResponse response = useApiService.post(SECRET_KEY, TOKEN, endpoint, requestBody);
//
//            // then
//            assertNotNull(response);
//            assertNotNull(response.body());
//            verify(apiTokenQueryService).findByTokenClientId(TOKEN);
//            verify(apiUsageQueryService).findByTokenAndEndpoint(apiToken, endpoint, MethodType.POST);
//        }
//
//        @Test
//        @DisplayName("PUT /objects/{id} - 객체 전체 업데이트")
//        void put_UpdateObject_Success() {
//            // given - 먼저 POST로 객체 생성
//            String createEndpoint = "/objects";
//            Map<String, Object> createBody = new HashMap<>();
//            createBody.put("name", "Apple MacBook Pro 16");
//            createBody.put("data", Map.of(
//                    "year", 2019,
//                    "price", 1849.99,
//                    "CPU model", "Intel Core i9",
//                    "Hard disk size", "1 TB"
//            ));
//
//            ApiUsage createApiUsage = createApiUsage(createEndpoint, "POST", 2L);
//            when(apiTokenQueryService.findByTokenClientId(TOKEN))
//                    .thenReturn(apiToken);
//            when(apiUsageQueryService.findByTokenAndEndpoint(apiToken, createEndpoint, MethodType.POST))
//                    .thenReturn(createApiUsage);
//
//            ProxyResponse createResponse = useApiService.post(SECRET_KEY, TOKEN, createEndpoint, createBody);
//
//            // 생성된 객체의 ID 추출
//            @SuppressWarnings("unchecked")
//            Map<String, Object> createdObject = (Map<String, Object>) createResponse.body();
//            String objectId = createdObject.get("id").toString();
//
//            // PUT으로 업데이트
//            String updateEndpoint = "/objects/" + objectId;
//            Map<String, Object> updateBody = new HashMap<>();
//            updateBody.put("name", "Apple MacBook Pro 16 (Updated)");
//            updateBody.put("data", Map.of(
//                    "year", 2019,
//                    "price", 2049.99,
//                    "CPU model", "Intel Core i9",
//                    "Hard disk size", "1 TB",
//                    "color", "silver"
//            ));
//
//            ApiUsage updateApiUsage = createApiUsage(updateEndpoint, "PUT", 4L);
//            when(apiUsageQueryService.findByTokenAndEndpoint(apiToken, updateEndpoint, MethodType.PUT))
//                    .thenReturn(updateApiUsage);
//
//            // when
//            ProxyResponse response = useApiService.put(SECRET_KEY, TOKEN, updateEndpoint, updateBody);
//
//            // then
//            assertNotNull(response);
//            verify(apiUsageQueryService).findByTokenAndEndpoint(apiToken, updateEndpoint, MethodType.PUT);
//        }
//
//        @Test
//        @DisplayName("PATCH /objects/{id} - 객체 부분 업데이트")
//        void patch_PartialUpdateObject_Success() {
//            // given - 먼저 POST로 객체 생성
//            String createEndpoint = "/objects";
//            Map<String, Object> createBody = new HashMap<>();
//            createBody.put("name", "Apple MacBook Pro 16");
//            createBody.put("data", Map.of(
//                    "year", 2019,
//                    "price", 1849.99,
//                    "CPU model", "Intel Core i9",
//                    "Hard disk size", "1 TB"
//            ));
//
//            ApiUsage createApiUsage = createApiUsage(createEndpoint, "POST", 2L);
//            when(apiTokenQueryService.findByTokenClientId(TOKEN))
//                    .thenReturn(apiToken);
//            when(apiUsageQueryService.findByTokenAndEndpoint(apiToken, createEndpoint, MethodType.POST))
//                    .thenReturn(createApiUsage);
//
//            ProxyResponse createResponse = useApiService.post(SECRET_KEY, TOKEN, createEndpoint, createBody);
//
//            // 생성된 객체의 ID 추출
//            @SuppressWarnings("unchecked")
//            Map<String, Object> createdObject = (Map<String, Object>) createResponse.body();
//            String objectId = createdObject.get("id").toString();
//
//            // PATCH로 부분 업데이트
//            String patchEndpoint = "/objects/" + objectId;
//            Map<String, Object> patchBody = new HashMap<>();
//            patchBody.put("name", "Apple MacBook Pro 16 (Patched)");
//
//            ApiUsage patchApiUsage = createApiUsage(patchEndpoint, "PATCH", 5L);
//            when(apiUsageQueryService.findByTokenAndEndpoint(apiToken, patchEndpoint, MethodType.PATCH))
//                    .thenReturn(patchApiUsage);
//
//            // when
//            ProxyResponse response = useApiService.patch(SECRET_KEY, TOKEN, patchEndpoint, patchBody);
//
//            // then
//            assertNotNull(response);
//            verify(apiUsageQueryService).findByTokenAndEndpoint(apiToken, patchEndpoint, MethodType.PATCH);
//        }
//
//        @Test
//        @DisplayName("DELETE /objects/{id} - 객체 삭제")
//        void delete_DeleteObject_Success() {
//            // given - 먼저 POST로 객체 생성
//            String createEndpoint = "/objects";
//            Map<String, Object> createBody = new HashMap<>();
//            createBody.put("name", "Apple MacBook Pro 16");
//            createBody.put("data", Map.of(
//                    "year", 2019,
//                    "price", 1849.99,
//                    "CPU model", "Intel Core i9",
//                    "Hard disk size", "1 TB"
//            ));
//
//            ApiUsage createApiUsage = createApiUsage(createEndpoint, "POST", 2L);
//            when(apiTokenQueryService.findByTokenClientId(TOKEN))
//                    .thenReturn(apiToken);
//            when(apiUsageQueryService.findByTokenAndEndpoint(apiToken, createEndpoint, MethodType.POST))
//                    .thenReturn(createApiUsage);
//
//            ProxyResponse createResponse = useApiService.post(SECRET_KEY, TOKEN, createEndpoint, createBody);
//
//            // 생성된 객체의 ID 추출
//            @SuppressWarnings("unchecked")
//            Map<String, Object> createdObject = (Map<String, Object>) createResponse.body();
//            String objectId = createdObject.get("id").toString();
//
//            // DELETE로 삭제
//            String deleteEndpoint = "/objects/" + objectId;
//            ApiUsage deleteApiUsage = createApiUsage(deleteEndpoint, "DELETE", 6L);
//            when(apiUsageQueryService.findByTokenAndEndpoint(apiToken, deleteEndpoint, MethodType.DELETE))
//                    .thenReturn(deleteApiUsage);
//
//            // when
//            ProxyResponse response = useApiService.delete(SECRET_KEY, TOKEN, deleteEndpoint);
//
//            // then
//            assertNotNull(response);
//            verify(apiUsageQueryService).findByTokenAndEndpoint(apiToken, deleteEndpoint, MethodType.DELETE);
//        }
//    }
//
//    @Nested
//    @DisplayName("엔드포인트별 파라미터 검증 테스트")
//    class EndpointParameterTests {
//
//        @Test
//        @DisplayName("동적 경로 파라미터가 포함된 엔드포인트 호출")
//        void get_WithPathParameter_Success() {
//            // given - GET /objects/{id} 형식
//            String endpoint = "/objects/7";
//            ApiUsage apiUsage = createApiUsage("/objects/{id}", "GET", 9L);
//
//            when(apiTokenQueryService.findByTokenClientId(TOKEN))
//                    .thenReturn(apiToken);
//            when(apiUsageQueryService.findByTokenAndEndpoint(apiToken, endpoint, MethodType.GET))
//                    .thenReturn(apiUsage);
//
//            // when
//            ProxyResponse response = useApiService.get(SECRET_KEY, TOKEN, endpoint);
//
//            // then
//            assertNotNull(response);
//            verify(apiUsageQueryService).findByTokenAndEndpoint(apiToken, endpoint, MethodType.GET);
//        }
//    }
//
//    private ApiUsage createApiUsage(String endpoint, String method, Long apiUsageId) {
//        User creator = User.of(
//                "creator@bssm.hs.kr",
//                "API Creator",
//                "https://profile.image.url",
//                UserRole.ROLE_USER
//        );
//
//        Api api = Api.builder()
//                .apiId(apiUsageId)
//                .creator(creator)
//                .endpoint(endpoint)
//                .method(method)
//                .name(method + " " + endpoint)
//                .domain(BASE_URL)
//                .repositoryUrl("https://github.com/test/repo")
//                .autoApproval(true)
//                .build();
//
//        ApiUseReason reason = ApiUseReason.builder()
//                .apiUseReasonId(1L)
//                .writer(user)
//                .api(api)
//                .apiToken(apiToken)
//                .apiUseReason("Test API Usage")
//                .apiUseState(com.example.bssm_dev.domain.api.model.type.ApiUseState.APPROVED)
//                .build();
//
//        return ApiUsage.builder()
//                .id(new ApiUsageId(1L, apiUsageId))
//                .apiToken(apiToken)
//                .api(api)
//                .apiUseReason(reason)
//                .name(method + " " + endpoint)
//                .endpoint(endpoint)
//                .build();
//    }
//}
