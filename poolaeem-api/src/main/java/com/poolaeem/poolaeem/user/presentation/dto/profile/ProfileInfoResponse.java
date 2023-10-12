package com.poolaeem.poolaeem.user.presentation.dto.profile;

import com.poolaeem.poolaeem.user.domain.dto.ProfileDto;

public class ProfileInfoResponse {
    private ProfileInfoResponse() {
    }

    public record ProfileInfoDto(
            String userId,
            String email,
            String name,
            String profileImageUrl
    ) {
        public ProfileInfoDto(ProfileDto.ProfileInfo profileInfo) {
            this(profileInfo.getUserId(), profileInfo.getEmail(), profileInfo.getName(), profileInfo.getProfileImageUrl());
        }
    }
}
