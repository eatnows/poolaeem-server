package com.poolaeem.poolaeem.user.domain.service.profile;

import com.poolaeem.poolaeem.common.exception.user.UserNotFoundException;
import com.poolaeem.poolaeem.user.domain.dto.ProfileDto;
import com.poolaeem.poolaeem.user.domain.entity.UserRole;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("단위: 프로필 정보 테스트")
class ProfileInfoServiceImplTest {

    @InjectMocks
    private ProfileInfoServiceImpl profileInfoService;
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("userId 로 유저의 프로필 정보를 조회할 수 있다.")
    void testUserProfileInfoRetrieval() {
        String userId = "user-unit";
        UserVo mockUser = new UserVo(
                "user-unit",
                "unit@poolaeem.com",
                "유닛",
                UserRole.ROLE_USER,
                null,
                null,
                null,
                null
        );
        given(userRepository.findDtoByUserIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(mockUser));

        ProfileDto.ProfileInfo profileInfo = profileInfoService.readProfileInfo(userId);

        assertThat(profileInfo.getUserId()).isEqualTo(userId);
        assertThat(profileInfo.getEmail()).isEqualTo(mockUser.getEmail());
        assertThat(profileInfo.getName()).isEqualTo(mockUser.getName());
//        assertThat(profileInfo.getProfileImageUrl()).isEqualTo(userId);
    }

    @Test
    @DisplayName("존재하지 않는 유저의 프로필 정보는 조회할 수 없다.")
    void testUserProfileRetrievalForNonExistentUser() {
        String otherUserId = "user-unit";

        given(userRepository.findDtoByUserIdAndIsDeletedFalse(otherUserId))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> profileInfoService.readProfileInfo(otherUserId))
                .isInstanceOf(UserNotFoundException.class);
    }
}