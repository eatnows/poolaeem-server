package com.poolaeem.poolaeem.user.application;

import com.poolaeem.poolaeem.user.domain.dto.ProfileDto;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileInfoService {
    ProfileDto.ProfileInfo readProfileInfo(String userId);

    void updateUserName(String userId, String userName);

    void updateProfileImage(String userId, MultipartFile file);
}
