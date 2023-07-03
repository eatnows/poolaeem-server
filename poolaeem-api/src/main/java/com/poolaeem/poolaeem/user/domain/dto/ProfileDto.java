package com.poolaeem.poolaeem.user.domain.dto;

import lombok.Getter;

public class ProfileDto {
    private ProfileDto() {
    }

    @Getter
    public static class ProfileInfo {
        private String userId;
        private String email;
        private String name;
        private String profileImageUrl;

        public ProfileInfo(String userId, String email, String name, String profileImageUrl) {
            this.userId = userId;
            this.email = email;
            this.name = name;
            this.profileImageUrl = profileImageUrl;
        }
    }
}
