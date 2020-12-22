package com.sitebase.constant;

public enum RoleType {
    ROLE_USER("ROLE_USER"), ROLE_MANAGER("ROLE_MANAGER"), ROLE_ADMIN("ROLE_ADMIN");

    private String role;

    RoleType(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
