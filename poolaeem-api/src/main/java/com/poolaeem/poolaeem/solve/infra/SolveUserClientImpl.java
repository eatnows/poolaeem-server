package com.poolaeem.poolaeem.solve.infra;

import com.poolaeem.poolaeem.solve.domain.dto.WorkbookCreator;
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
    public WorkbookCreator readWorkbookCreator(String userId) {
        ProfileDto.ProfileInfo info = profileInfoService.readProfileInfo(userId);
        return new WorkbookCreator(info.getName(), info.getProfileImageUrl());
    }
}
