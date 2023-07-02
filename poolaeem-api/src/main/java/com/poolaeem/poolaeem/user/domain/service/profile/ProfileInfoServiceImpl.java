package com.poolaeem.poolaeem.user.domain.service.profile;

import com.poolaeem.poolaeem.common.component.uuid.UUIDGenerator;
import com.poolaeem.poolaeem.common.event.FileEventsPublisher;
import com.poolaeem.poolaeem.common.event.obj.EventsPublisherFileEvent;
import com.poolaeem.poolaeem.common.exception.user.UserNotFoundException;
import com.poolaeem.poolaeem.file.domain.FilePath;
import com.poolaeem.poolaeem.user.application.ProfileInfoService;
import com.poolaeem.poolaeem.user.domain.dto.ProfileDto;
import com.poolaeem.poolaeem.user.domain.entity.User;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import com.poolaeem.poolaeem.user.domain.validation.UserValidation;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileInfoServiceImpl implements ProfileInfoService {

    private final UserRepository userRepository;
    private final FileEventsPublisher fileEventsPublisher;

    public ProfileInfoServiceImpl(UserRepository userRepository, FileEventsPublisher fileEventsPublisher) {
        this.userRepository = userRepository;
        this.fileEventsPublisher = fileEventsPublisher;
    }

    @Transactional(readOnly = true)
    @Override
    public ProfileDto.ProfileInfo readProfileInfo(String userId) {
        UserVo user = getUserVo(userId);

        String profileImageUrl = getProfileImageUrl(user);

        return new ProfileDto.ProfileInfo(user.getId(), user.getEmail(), user.getName(), profileImageUrl);
    }

    @Transactional
    @Override
    public void updateUserName(String userId, String reqUserName) {
        validationUserName(reqUserName);

        User user = getUserEntity(userId);
        user.updateName(reqUserName);
    }

    @Transactional
    @Override
    public void updateProfileImage(String userId, MultipartFile fileObject) {
        User user = getUserEntity(userId);

        if (user.isExistProfileImage()) {
            fileEventsPublisher.publish(generateFileDeleteEvent(user.getProfileImage()));
            user.deleteProfileImage();
        }
        if (fileObject != null) {
            String newFileId = UUIDGenerator.generate();
            user.changeProfileImage(newFileId);
            fileEventsPublisher.publish(generateFileUploadEvent(newFileId, fileObject));
        }
    }

    private EventsPublisherFileEvent.FileDeleteEvent generateFileDeleteEvent(String fileId) {
        return new EventsPublisherFileEvent.FileDeleteEvent(
                fileId,
                FilePath.PROFILE_IMAGE
        );
    }

    private EventsPublisherFileEvent.FileUploadEvent generateFileUploadEvent(String newFileId, MultipartFile fileObject) {
        return new EventsPublisherFileEvent.FileUploadEvent(
                newFileId,
                FilePath.PROFILE_IMAGE,
                fileObject
        );
    }

    private void validationUserName(String reqUserName) {
        assert reqUserName.length() <= UserValidation.USER_NAME_MAX_LENGTH: "유저 이름 최대 길이보다 짧아야 합니다..";
        assert reqUserName.length() >= UserValidation.USER_NAME_MIN_LENGTH : "유저 이름 최소 길이보다 길어야 합니다.";
    }

    private User getUserEntity(String userId) {
        return userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private UserVo getUserVo(String userId) {
        return userRepository.findDtoByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private String getProfileImageUrl(UserVo user) {
        String profileImageUrl = null;
        if (existProfileImage(user.getProfileImage())) {
            // TODO 유저 프로필 이미지 조회 주소 생성

        }

        return profileImageUrl;
    }


    private boolean existProfileImage(String profileImage) {
        return !StringUtils.isEmpty(profileImage);
    }
}
