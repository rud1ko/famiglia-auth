package com.famiglia.famiglia_auth.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    FATHER,
    MOTHER,
    SON,
    DAUGHTER,
    GRANDMOTHER,
    GRANDFATHER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
