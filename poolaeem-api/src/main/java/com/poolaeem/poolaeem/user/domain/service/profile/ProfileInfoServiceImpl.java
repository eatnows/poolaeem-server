package com.poolaeem.poolaeem.user.domain.service.profile;

import com.poolaeem.poolaeem.common.component.uuid.UUIDGenerator;
import com.poolaeem.poolaeem.common.event.FileEventsPublisher;
import com.poolaeem.poolaeem.common.event.obj.EventsPublisherFileEvent;
import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.common.exception.user.UserNotFoundException;
import com.poolaeem.poolaeem.common.file.FilePath;
import com.poolaeem.poolaeem.file.application.FileService;
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
    private final FileService fileService;

    public ProfileInfoServiceImpl(UserRepository userRepository, FileEventsPublisher fileEventsPublisher, FileService fileService) {
        this.userRepository = userRepository;
        this.fileEventsPublisher = fileEventsPublisher;
        this.fileService = fileService;
    }

    @Transactional(readOnly = true)
    @Override
    public ProfileDto.ProfileInfo readProfileInfo(String userId) {
        UserVo user = getUserVo(userId, false);

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

    @Override
    public ProfileDto.WorkbookCreatorRead readWorkbookCreator(String userId) {
        UserVo user = getUserVo(userId, null);
        if (user == null || user.getIsDeleted()) {
            return new ProfileDto.WorkbookCreatorRead(user);
        }

        return new ProfileDto.WorkbookCreatorRead(user, getProfileImageUrl(user));
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
        if (reqUserName.length() > UserValidation.USER_NAME_MAX_LENGTH) {
            throw new BadRequestDataException(UserValidation.Message.USER_NAME_MAX_LENGTH);
        }
        if (reqUserName.length() < UserValidation.USER_NAME_MIN_LENGTH) {
            throw new BadRequestDataException(UserValidation.Message.USER_NAME_MIN_LENGTH);
        }
    }

    private User getUserEntity(String userId) {
        return userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private UserVo getUserVo(String userId, Boolean isDeleted) {
        if (isDeleted == null) {
            return userRepository.findDtoByUserIdAndIsDeleted(userId, null)
                    .orElse(null);
        }
        return userRepository.findDtoByUserIdAndIsDeleted(userId, isDeleted)
                .orElseThrow(UserNotFoundException::new);
    }

    private String getProfileImageUrl(UserVo user) {
        String profileImageUrl = null;
        if (existProfileImage(user.getProfileImage())) {
            profileImageUrl = fileService.getImageUrl(user.getProfileImage());
        }

        return profileImageUrl;
    }


    private boolean existProfileImage(String profileImage) {
        return !StringUtils.isEmpty(profileImage);
    }
}
