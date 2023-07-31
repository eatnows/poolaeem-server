package com.poolaeem.poolaeem.user.domain.dto;

import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
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
    @Getter
    public static class WorkbookCreatorRead {
        private String userId;
        private String name;
        private String profileImageUrl;
        private boolean isDeleted;

        public WorkbookCreatorRead(UserVo user) {
            if (user == null || user.getIsDeleted()) {
                isDeleted = true;
            }
        }

        public WorkbookCreatorRead(UserVo user, String profileImageUrl) {
            if (user == null) {
                isDeleted = true;
            } else {
                userId = user.getId();
                name = user.getName();
                this.profileImageUrl = profileImageUrl;
            }
        }
    }
}
