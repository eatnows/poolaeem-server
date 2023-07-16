package com.poolaeem.poolaeem.solve.infra;

import com.poolaeem.poolaeem.solve.domain.dto.WorkbookAuthor;
import com.poolaeem.poolaeem.user.application.ProfileInfoService;
import com.poolaeem.poolaeem.user.domain.dto.ProfileDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolveUserClientImpl implements SolveUserClient {
    private final ProfileInfoService profileInfoService;

    public SolveUserClientImpl(ProfileInfoService profileInfoService) {
        this.profileInfoService = profileInfoService;
    }


    @Transactional(readOnly = true)
    @Override
    public WorkbookAuthor readWorkbookAuthor(String userId) {
        ProfileDto.ProfileInfo info = profileInfoService.readProfileInfo(userId);
        return new WorkbookAuthor(info.getName(), info.getProfileImageUrl());
    }
}
