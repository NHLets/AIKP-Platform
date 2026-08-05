package org.afdb.aikp.modules.iam.application.mapper;

import org.afdb.aikp.modules.iam.application.response.UserResponse;
import org.afdb.aikp.modules.iam.application.response.UserSummary;
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

    public static UserSummary toSummary(User user) {
    return new UserSummary(
            user.getId().getValue(),
            user.getUsername().value(),
            user.getFullName().value(),
            user.getEmail().value(),
            user.getStatus()
    );
    }

    public static List<UserSummary> toSummaryList(List<User> users) {
    return users.stream()
            .map(UserApplicationMapper::toSummary)
            .toList();
    }

}