package com.poolaeem.poolaeem.integration.user.profile;

import com.poolaeem.poolaeem.common.event.obj.EventsPublisherFileEvent;
import com.poolaeem.poolaeem.component.TextGenerator;
import com.poolaeem.poolaeem.integration.base.BaseLocalStackTest;
import com.poolaeem.poolaeem.user.domain.entity.User;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
import com.poolaeem.poolaeem.user.presentation.dto.profile.ProfileInfoRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;
import software.amazon.awssdk.services.s3.S3Client;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RecordApplicationEvents
@DisplayName("통합: 유저 정보 수정 테스트")
@Sql(scripts = "classpath:/sql/file/file.sql")
class ProfileInfoControllerUpdateTest extends BaseLocalStackTest {
    private final String UPDATE_USER_NAME = "/api/profile/name";
    private final String UPDATE_PROFILE_IMAGE = "/api/profile/image";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private ApplicationEvents applicationEvents;

    @Test
    @DisplayName("유저의 이름을 수정할 수 있다.")
    void testUserNameUpdate() throws Exception {
        String userId = "user-1";
        String newUserName = "newName";

        User before = userRepository.findByIdAndIsDeletedFalse(userId).get();
        assertThat(before.getName()).isNotEqualTo(newUserName);

        ResultActions result = this.mockMvc.perform(
                patch(UPDATE_USER_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new ProfileInfoRequest.UserNameUpdateDto(newUserName)))
                        .accept(MediaType.APPLICATION_JSON)
        );

        User after = userRepository.findByIdAndIsDeletedFalse(userId).get();
        assertThat(after.getName()).isEqualTo(newUserName);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.userId", is("user-1")))
                .andExpect(jsonPath("$.data.email", is("test@poolaeem.com")))
                .andExpect(jsonPath("$.data.name", is(newUserName)))
                .andExpect(jsonPath("$.data.profileImageUrl", containsString("file-1")));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 31})
    @DisplayName("유저의 이름을 최소 1자 이상, 30자 이하로만 변경할 수 있다.")
    void testUserNameUpdateForLengthValidation(int length) throws Exception {
        String newUserName = TextGenerator.generate(length);

        ResultActions result = this.mockMvc.perform(
                patch(UPDATE_USER_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new ProfileInfoRequest.UserNameUpdateDto(newUserName)))
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isBadRequest())
                .andExpect(exception -> assertThat(exception.getResolvedException())
                        .isInstanceOf(MethodArgumentNotValidException.class));
    }

    @Test
    @DisplayName("유저의 프로필 이미지를 변경할 수 있다.")
    void testUserProfileImageUpdate() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "file_name.txt", "text/plain", "<< file content >>".getBytes(StandardCharsets.UTF_8));
        String userId = "user-1";

        User before = userRepository.findByIdAndIsDeletedFalse(userId).get();
        assertThat(before.getProfileImage()).isEqualTo("file-1");

        ResultActions result = this.mockMvc.perform(
                multipart(UPDATE_PROFILE_IMAGE)
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
        );

        User after = userRepository.findByIdAndIsDeletedFalse(userId).get();
        assertThat(after.getProfileImage()).isNotNull();
        assertThat(after.getProfileImage()).isNotEqualTo("file-1");
        assertThat(applicationEvents.stream(EventsPublisherFileEvent.FileUploadEvent.class).count()).isEqualTo(1);

        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.userId", is("user-1")))
                .andExpect(jsonPath("$.data.email", is("test@poolaeem.com")))
                .andExpect(jsonPath("$.data.name", is(after.getName())))
                .andExpect(jsonPath("$.data.profileImageUrl", containsString(after.getProfileImage())));
    }

    @Test
    @DisplayName("유저의 프로필 이미지를 삭제할 수 있다.")
    void testUserProfileImageDelete() throws Exception {
        // 프로필 이미지 추가 -> 프로필 이미지 삭제
        MockMultipartFile mockFile = new MockMultipartFile("file", "file_name.txt", "text/plain", "<< file content >>".getBytes(StandardCharsets.UTF_8));
        String userId = "user-1";

        User before = userRepository.findByIdAndIsDeletedFalse(userId).get();
        assertThat(before.getProfileImage()).isEqualTo("file-1");
        this.mockMvc.perform(
                multipart(UPDATE_PROFILE_IMAGE)
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
        );
        User before2 = userRepository.findByIdAndIsDeletedFalse(userId).get();
        assertThat(before2.getProfileImage()).isNotEqualTo("file-1");

        ResultActions result = this.mockMvc.perform(
                multipart(UPDATE_PROFILE_IMAGE)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
        );
        User after = userRepository.findByIdAndIsDeletedFalse(userId).get();
        assertThat(after.getProfileImage()).isNull();

        assertThat(applicationEvents.stream(EventsPublisherFileEvent.FileDeleteEvent.class).count()).isEqualTo(2);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.userId", is("user-1")))
                .andExpect(jsonPath("$.data.email", is("test@poolaeem.com")))
                .andExpect(jsonPath("$.data.name", is(after.getName())))
                .andExpect(jsonPath("$.data.profileImageUrl", nullValue()));
    }
}