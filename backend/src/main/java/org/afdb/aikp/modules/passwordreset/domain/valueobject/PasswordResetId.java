package org.afdb.aikp.modules.passwordreset.domain.valueobject;

import java.util.UUID;

public record PasswordResetId(UUID value){

    public static PasswordResetId generate(){
        return new PasswordResetId(UUID.randomUUID());
    }
}
