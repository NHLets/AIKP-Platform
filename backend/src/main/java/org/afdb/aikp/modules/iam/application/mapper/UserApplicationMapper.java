package org.afdb.aikp.modules.iam.application.mapper;

import org.afdb.aikp.modules.iam.application.response.UserResponse;
import org.afdb.aikp.modules.iam.domain.model.User;

import java.util.List;

public final class UserApplicationMapper {

    private UserApplicationMapper() {
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId().getValue(),
                user.getUsername().value(),
                user.getEmail().value(),
                user.getFullName().value(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLastLogin()
        );
    }

    public static List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(UserApplicationMapper::toResponse)
                .toList();
    }
}