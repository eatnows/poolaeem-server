package com.poolaeem.poolaeem.user.domain.dto;

import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import lombok.Getter;

public class ProfileDto {
    private ProfileDto() {
    }

    public record ProfileInfo(
        String userId,
        String email,
        String name,
        String profileImageUrl
    ) {
    }

    @Getter
    public static class WorkbookCreatorRead {
        private String userId;
        private String name;
        private String profileImageUrl;
        private boolean isDeleted;

        public WorkbookCreatorRead(UserVo user) {
            if (user == null || user.isDeleted()) {
                isDeleted = true;
            }
        }

        public WorkbookCreatorRead(UserVo user, String profileImageUrl) {
            if (user == null) {
                isDeleted = true;
            } else {
                userId = user.id();
                name = user.name();
                this.profileImageUrl = profileImageUrl;
            }
        }
    }
}
