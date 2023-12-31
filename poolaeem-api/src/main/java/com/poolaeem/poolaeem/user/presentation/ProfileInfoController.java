package com.poolaeem.poolaeem.user.presentation;

import com.poolaeem.poolaeem.common.annotation.LoggedInUser;
import com.poolaeem.poolaeem.common.annotation.LoggedInUserOnly;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import com.poolaeem.poolaeem.user.application.ProfileInfoService;
import com.poolaeem.poolaeem.user.domain.dto.ProfileDto;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import com.poolaeem.poolaeem.user.presentation.dto.profile.ProfileInfoRequest;
import com.poolaeem.poolaeem.user.presentation.dto.profile.ProfileInfoResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileInfoController {

    private final ProfileInfoService profileInfoService;

    public ProfileInfoController(ProfileInfoService profileInfoService) {
        this.profileInfoService = profileInfoService;
    }

    @LoggedInUserOnly
    @GetMapping("/api/profile/info")
    public ApiResponseDto<ProfileInfoResponse.ProfileInfoDto> readProfileInfo(@LoggedInUser UserVo user) {
        ProfileDto.ProfileInfo profileInfo = profileInfoService.readProfileInfo(user.id());

        return ApiResponseDto.OK(new ProfileInfoResponse.ProfileInfoDto(profileInfo));
    }

    @LoggedInUserOnly
    @PatchMapping("/api/profile/name")
    public ApiResponseDto<ProfileInfoResponse.ProfileInfoDto> updateUserName(@LoggedInUser UserVo user,
                                                                             @Valid @RequestBody ProfileInfoRequest.UserNameUpdateDto dto) {
        profileInfoService.updateUserName(user.id(), dto.getUserName());
        ProfileDto.ProfileInfo profileInfo = profileInfoService.readProfileInfo(user.id());

        return ApiResponseDto.OK(new ProfileInfoResponse.ProfileInfoDto(profileInfo));
    }

    @LoggedInUserOnly
    @PostMapping("/api/profile/image")
    public ApiResponseDto<ProfileInfoResponse.ProfileInfoDto> updateProfileImage(@LoggedInUser UserVo user,
                                                                                 @Valid ProfileInfoRequest.ProfileImageUpdateDto dto) {
        profileInfoService.updateProfileImage(user.id(), dto.getFile());
        ProfileDto.ProfileInfo profileInfo = profileInfoService.readProfileInfo(user.id());
        return ApiResponseDto.OK(new ProfileInfoResponse.ProfileInfoDto(profileInfo));
    }
}
