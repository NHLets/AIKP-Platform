package org.afdb.aikp.shared.web;

public record ApiValidationError(

        String field,

        String message

) {
}