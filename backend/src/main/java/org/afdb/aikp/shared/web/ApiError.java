package org.afdb.aikp.shared.web;

import org.afdb.aikp.shared.exception.ErrorCode;

import java.time.OffsetDateTime;

public record ApiError(

        OffsetDateTime timestamp,

        int status,

        ErrorCode code,

        String error,

        String message,

        String path

) {
}