package org.afdb.aikp.modules.passwordreset.domain.valueobject;

import java.util.UUID;

public record ResetToken(String value){

    public static ResetToken generate(){
        return new ResetToken(UUID.randomUUID().toString());
    }
}\n