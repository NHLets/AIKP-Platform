package org.afdb.aikp.modules.settings.presentation;

import java.time.OffsetDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/settings")
@PreAuthorize("hasRole('ADMIN')")
public class PlatformSettingsController {

    private final JdbcTemplate jdbcTemplate;

    public PlatformSettingsController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public record SettingsResponse(
        String platformName,
        String platformDescription,
        OffsetDateTime updatedAt
    ) {}

    public record UpdateSettingsRequest(
        String platformName,
        String platformDescription
    ) {}

    @GetMapping
    public SettingsResponse getSettings() {
        return jdbcTemplate.queryForObject(
            """
            SELECT platform_name, platform_description, updated_at
            FROM reference.platform_settings
            WHERE id = 1
            """,
            (rs, rowNum) -> new SettingsResponse(
                rs.getString("platform_name"),
                rs.getString("platform_description"),
                rs.getObject("updated_at", OffsetDateTime.class)
            )
        );
    }

    @PutMapping
    public SettingsResponse updateSettings(@RequestBody UpdateSettingsRequest request) {
        if (request == null || request.platformName() == null
                || request.platformName().trim().isEmpty()
                || request.platformName().trim().length() > 120) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Platform name is required and must not exceed 120 characters."
            );
        }

        String description = request.platformDescription() == null
            ? ""
            : request.platformDescription().trim();

        if (description.length() > 1000) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Platform description must not exceed 1000 characters."
            );
        }

        int updated = jdbcTemplate.update(
            """
            UPDATE reference.platform_settings
            SET platform_name = ?, platform_description = ?,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = 1
            """,
            request.platformName().trim(),
            description
        );

        if (updated != 1) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Platform settings could not be updated."
            );
        }

        return getSettings();
    }
}
