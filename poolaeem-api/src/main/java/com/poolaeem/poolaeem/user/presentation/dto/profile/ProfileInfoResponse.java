package com.poolaeem.poolaeem.user.presentation.dto.profile;

import com.poolaeem.poolaeem.user.domain.dto.ProfileDto;
import lombok.Getter;

public class ProfileInfoResponse {
    private ProfileInfoResponse() {
    }

    @Getter
    public static class ProfileInfoDto {
        private String userId;
        private String email;
        private String name;
        private String profileImageUrl;

        public ProfileInfoDto(ProfileDto.ProfileInfo profileInfo) {
            this.userId = profileInfo.getUserId();
            this.email = profileInfo.getEmail();
            this.name = profileInfo.getName();
            this.profileImageUrl = profileInfo.getProfileImageUrl();
        }
    }
}
