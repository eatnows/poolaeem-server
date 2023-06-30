package com.poolaeem.poolaeem.user.domain.service.profile;

import com.poolaeem.poolaeem.common.exception.user.UserNotFoundException;
import com.poolaeem.poolaeem.user.application.ProfileInfoService;
import com.poolaeem.poolaeem.user.domain.dto.ProfileDto;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ProfileInfoServiceImpl implements ProfileInfoService {

    private final UserRepository userRepository;

    public ProfileInfoServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ProfileDto.ProfileInfo readProfileInfo(String userId) {
        UserVo user = getUserVo(userId);

        String profileImageUrl = getProfileImageUrl(user);

        return new ProfileDto.ProfileInfo(user.getId(), user.getEmail(), user.getName(), profileImageUrl);
    }

    private UserVo getUserVo(String userId) {
        return userRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private String getProfileImageUrl(UserVo user) {
        String profileImageUrl = null;
        if (existProfileImage(user.getProfileImage())) {
            // TODO 유저 프로필 이미지 조회 주소 생성

        }

        return profileImageUrl;
    }


    private boolean existProfileImage(String profileImage) {
        return !StringUtils.isEmpty(profileImage);
    }
}
