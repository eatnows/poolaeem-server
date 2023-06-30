package com.poolaeem.poolaeem.user.application;

import com.poolaeem.poolaeem.user.domain.dto.ProfileDto;

public interface ProfileInfoService {
    ProfileDto.ProfileInfo readProfileInfo(String userId);
}
