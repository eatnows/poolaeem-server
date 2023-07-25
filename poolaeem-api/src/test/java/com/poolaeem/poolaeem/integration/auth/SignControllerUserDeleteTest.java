package com.poolaeem.poolaeem.integration.auth;

import com.poolaeem.poolaeem.common.event.obj.EventsPublisherFileEvent;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.integration.base.BaseLocalStackTest;
import com.poolaeem.poolaeem.user.domain.entity.User;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("통합: 유저 탈퇴 테스트")
@Sql(scripts = {
        "classpath:/sql/user/user.sql",
        "classpath:/sql/file/file.sql"
})
@RecordApplicationEvents
class SignControllerUserDeleteTest extends BaseLocalStackTest {
    private final String DELETE_USER = "/api/users/{userId}";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationEvents applicationEvents;

    @Test
    @DisplayName("유저가 탈퇴할 수 있다. (프로필 이미지도 삭제)")
    void testDeleteUser() throws Exception {
        String pathUserId = "user-1";

        Optional<User> before = userRepository.findByIdAndIsDeletedFalse(pathUserId);
        assertThat(before).isPresent();

        mockMvc.perform(
                        delete(DELETE_USER, pathUserId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)));

        Optional<User> after = userRepository.findByIdAndIsDeletedFalse(pathUserId);
        assertThat(after).isEmpty();

        // 프로필 이미지가 존재할 경우 이미지 삭제 이벤트가 발행
        assertThat(applicationEvents.stream(EventsPublisherFileEvent.FileDeleteEvent.class).count()).isEqualTo(1);
    }

    @Test
    @DisplayName("유저가 탈퇴하면 이메일과 이름이 알 수 없는 값으로 변경된다.")
    void testDeleteUserAfterUpdatingEmailAndName() throws Exception {
        String pathUserId = "user-1";

        User before = userRepository.findAll().stream().filter(u -> u.getId().equals(pathUserId)).findFirst().get();

        mockMvc.perform(
                        delete(DELETE_USER, pathUserId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)));

        User after = userRepository.findAll().stream().filter(u -> u.getId().equals(pathUserId)).findFirst().get();
        assertThat(after.getEmail()).isNotEqualTo(before.getEmail());
        assertThat(after.getName()).isNotEqualTo(before.getName());
        assertThat(after.getOauthId()).isNotEqualTo(before.getOauthId());
        assertThat(after.getUpdatedAt()).isNotEqualTo(before.getUpdatedAt());
    }

    @Test
    @DisplayName("탈퇴 요청 userId와 토큰의 값이 일치하지 않으면 탈퇴할 수 없다.")
    void testDeleteUserForNotEqualUserId() throws Exception {
        String pathUserId = "not-equal-user-1";

        mockMvc.perform(
                        delete(DELETE_USER, pathUserId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code", is(ApiResponseCode.FORBIDDEN.getCode())));
    }
}