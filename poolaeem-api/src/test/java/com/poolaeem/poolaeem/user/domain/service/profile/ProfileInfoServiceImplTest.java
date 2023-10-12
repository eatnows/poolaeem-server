package com.poolaeem.poolaeem.user.domain.service.profile;

import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.common.exception.user.UserNotFoundException;
import com.poolaeem.poolaeem.component.TextGenerator;
import com.poolaeem.poolaeem.user.domain.dto.ProfileDto;
import com.poolaeem.poolaeem.user.domain.entity.User;
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
                null,
                false);
        given(userRepository.findDtoByUserIdAndIsDeleted(userId, false))
                .willReturn(Optional.of(mockUser));

        ProfileDto.ProfileInfo profileInfo = profileInfoService.readProfileInfo(userId);

        assertThat(profileInfo.userId()).isEqualTo(userId);
        assertThat(profileInfo.email()).isEqualTo(mockUser.email());
        assertThat(profileInfo.name()).isEqualTo(mockUser.name());
//        assertThat(profileInfo.getProfileImageUrl()).isEqualTo(userId);
    }

    @Test
    @DisplayName("존재하지 않는 유저의 프로필 정보는 조회할 수 없다.")
    void testUserProfileRetrievalForNonExistentUser() {
        String otherUserId = "user-unit";

        given(userRepository.findDtoByUserIdAndIsDeleted(otherUserId, false))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> profileInfoService.readProfileInfo(otherUserId))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("유저의 이름을 새로운 이름으로 변경할 수 있다.")
    void testUserNameUpdate() {
        String userId = "user-unit";
        String newUserName = TextGenerator.generate(30);

        User mockUser = new User(
                "user-unit",
                "unit@poolaeem.com",
                "유닛",
                UserRole.ROLE_USER,
                null,
                null,
                null,
                null
        );
        given(userRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(mockUser));
        assertThat(mockUser.getName()).isNotEqualTo(newUserName);

        profileInfoService.updateUserName(userId, newUserName);

        assertThat(mockUser.getId()).isEqualTo(userId);
        assertThat(mockUser.getName()).isEqualTo(newUserName);
    }

    @Test
    @DisplayName("유저의 이름을 31자 이상으로는 변경할 수 없다")
    void testUserNameUpdateForOverLength() {
        String userId = "user-unit";
        String newUserName = TextGenerator.generate(31);

        assertThatThrownBy(() -> profileInfoService.updateUserName(userId, newUserName))
                .isInstanceOf(BadRequestDataException.class);
    }

    @Test
    @DisplayName("유저의 이름은 최소 1글자 이상이어야 한다.")
    void testUserNameUpdateForMinLength() {
        String userId = "user-unit";
        String newUserName = TextGenerator.generate(0);

        assertThatThrownBy(() -> profileInfoService.updateUserName(userId, newUserName))
                .isInstanceOf(BadRequestDataException.class);
    }
}