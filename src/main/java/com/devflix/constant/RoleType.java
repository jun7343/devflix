package com.devflix.constant;

public class RoleType {
    public static final String USER = "ROLE_USER";
    public static final String MANAGER = "ROLE_MANAGER";
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static String[] getAll() {
        return new String[] {USER, MANAGER, ADMIN};
    }
}
