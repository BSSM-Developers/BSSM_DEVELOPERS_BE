package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.domain.api.dto.response.ApiUsageResponse;
import com.example.bssm_dev.domain.api.exception.ApiNotFoundException;
import com.example.bssm_dev.domain.api.exception.UnauthorizedApiUsageAccessException;
import com.example.bssm_dev.domain.api.mapper.ApiUsageMapper;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.model.ApiUseReason;
import com.example.bssm_dev.domain.api.model.key.ApiUsageId;
import com.example.bssm_dev.domain.api.model.type.ApiUseState;
import com.example.bssm_dev.domain.api.repository.ApiUsageRepository;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.model.type.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiUsageQueryServiceTest {

    @InjectMocks
    private ApiUsageQueryService apiUsageQueryService;

    @Mock
    private ApiUsageRepository apiUsageRepository;

    @Mock
    private ApiUsageMapper apiUsageMapper;

    @Mock
    private ApiQueryService apiQueryService;

    private User creator;
    private User otherUser;
    private Api api;
    private ApiToken apiToken1;
    private ApiToken apiToken2;
    private ApiUsage apiUsage1;
    private ApiUsage apiUsage2;

    @BeforeEach
    void setUp() {
        creator = User.of(
                "creator@bssm.hs.kr",
                "API Creator",
                "https://profile.image.url",
                UserRole.ROLE_USER
        );
        setUserId(creator, 1L);

        otherUser = User.of(
                "other@bssm.hs.kr",
                "Other User",
                "https://profile.image.url",
                UserRole.ROLE_USER
        );
        setUserId(otherUser, 2L);

        api = Api.of(
                "test-api-id",
                creator,
                "/test-endpoint",
                "GET",
                "Test API",
                "https://test.domain.com",
                "https://github.com/test/repo",
                false
        );

        apiToken1 = ApiToken.builder()
                .apiTokenId(1L)
                .user(otherUser)
                .apiTokenUUID("token-uuid-1")
                .secretKey("secret-1")
                .apiTokenName("Token 1")
                .build();

        apiToken2 = ApiToken.builder()
                .apiTokenId(2L)
                .user(otherUser)
                .apiTokenUUID("token-uuid-2")
                .secretKey("secret-2")
                .apiTokenName("Token 2")
                .build();

        ApiUseReason reason1 = ApiUseReason.of(
                otherUser,
                api,
                apiToken1,
                "Test reason 1",
                ApiUseState.APPROVED
        );

        ApiUseReason reason2 = ApiUseReason.of(
                otherUser,
                api,
                apiToken2,
                "Test reason 2",
                ApiUseState.PENDING
        );

        apiUsage1 = ApiUsage.of(apiToken1, api, reason1, "Usage 1", "/test-endpoint");
        apiUsage2 = ApiUsage.of(apiToken2, api, reason2, "Usage 2", "/test-endpoint");
    }

    private void setUserId(User user, Long userId) {
        try {
            Field field = User.class.getDeclaredField("userId");
            field.setAccessible(true);
            field.set(user, userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Nested
    @DisplayName("본인이 등록한 API의 사용 신청 목록 조회 테스트")
    class GetApiUsagesByApiIdTests {

        @Test
        @DisplayName("API creator가 자신의 API 사용 신청 목록 조회 성공")
        void getApiUsagesByApiId_WithCreator_Success() {
            // given
            String apiId = "test-api-id";
            List<ApiUsage> apiUsages = Arrays.asList(apiUsage1, apiUsage2);
            
            ApiUsageResponse response1 = new ApiUsageResponse(
                    1L, "test-api-id", "Usage 1", "/test-endpoint",
                    "Test API", "https://test.domain.com", "GET", "1", "APPROVED"
            );
            ApiUsageResponse response2 = new ApiUsageResponse(
                    2L, "test-api-id", "Usage 2", "/test-endpoint",
                    "Test API", "https://test.domain.com", "GET", "2", "PENDING"
            );

            when(apiQueryService.findById(apiId)).thenReturn(api);
            when(apiUsageRepository.findAllByApiId(apiId)).thenReturn(apiUsages);
            when(apiUsageMapper.toResponse(apiUsage1)).thenReturn(response1);
            when(apiUsageMapper.toResponse(apiUsage2)).thenReturn(response2);

            // when
            List<ApiUsageResponse> result = apiUsageQueryService.getApiUsagesByApiId(creator, apiId);

            // then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Usage 1", result.get(0).name());
            assertEquals("Usage 2", result.get(1).name());
            verify(apiQueryService).findById(apiId);
            verify(apiUsageRepository).findAllByApiId(apiId);
            verify(apiUsageMapper, times(2)).toResponse(any(ApiUsage.class));
        }

        @Test
        @DisplayName("API creator가 아닌 사용자가 조회 시 UnauthorizedApiUsageAccessException 발생")
        void getApiUsagesByApiId_WithNonCreator_ThrowsException() {
            // given
            String apiId = "test-api-id";

            when(apiQueryService.findById(apiId)).thenReturn(api);

            // when & then
            assertThrows(UnauthorizedApiUsageAccessException.class, () ->
                    apiUsageQueryService.getApiUsagesByApiId(otherUser, apiId)
            );

            verify(apiQueryService).findById(apiId);
            verify(apiUsageRepository, never()).findAllByApiId(anyString());
            verify(apiUsageMapper, never()).toResponse(any(ApiUsage.class));
        }

        @Test
        @DisplayName("존재하지 않는 API ID로 조회 시 ApiNotFoundException 발생")
        void getApiUsagesByApiId_WithNonExistentApiId_ThrowsException() {
            // given
            String apiId = "non-existent-api-id";

            when(apiQueryService.findById(apiId)).thenThrow(ApiNotFoundException.raise());

            // when & then
            assertThrows(ApiNotFoundException.class, () ->
                    apiUsageQueryService.getApiUsagesByApiId(creator, apiId)
            );

            verify(apiQueryService).findById(apiId);
            verify(apiUsageRepository, never()).findAllByApiId(anyString());
            verify(apiUsageMapper, never()).toResponse(any(ApiUsage.class));
        }

        @Test
        @DisplayName("사용 신청이 없는 API 조회 시 빈 리스트 반환")
        void getApiUsagesByApiId_WithNoUsages_ReturnsEmptyList() {
            // given
            String apiId = "test-api-id";
            List<ApiUsage> emptyList = Arrays.asList();

            when(apiQueryService.findById(apiId)).thenReturn(api);
            when(apiUsageRepository.findAllByApiId(apiId)).thenReturn(emptyList);

            // when
            List<ApiUsageResponse> result = apiUsageQueryService.getApiUsagesByApiId(creator, apiId);

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(apiQueryService).findById(apiId);
            verify(apiUsageRepository).findAllByApiId(apiId);
            verify(apiUsageMapper, never()).toResponse(any(ApiUsage.class));
        }
    }
}
