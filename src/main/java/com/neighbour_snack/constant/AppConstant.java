package com.neighbour_snack.constant;

public final class AppConstant {

    // Private constructor to prevent instantiation
    private AppConstant() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String APPLICATION_VERSION_KEY = "app_version";
    public static final String APPLICATION_VERSION = "NS_1.0.0";
    public static final String API_VERSION = "/api/v1";

    public static final String SIGN_IN = "/signin";
    public static final String SIGN_IN_BASE_URL = API_VERSION + SIGN_IN;
    public static final String SIGN_UP_BASE_URL = API_VERSION + "/signup";
    public static final String SMTP_BASE_URL = API_VERSION + "/smtp";
    public static final String USER_BASE_URL = API_VERSION + "/users";
    public static final String UNVERIFIED_USER_BASE_URL = API_VERSION + "/unverified-users";

    public static final String CATEGORY_BASE_URL = API_VERSION + "/categories";

    /* Admin data */
    public static final String ADMIN_NAME = "admin";
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_EMAIL = "admin@mail.com";
    public static final String ADMIN_PASSWORD = "Admin@1234";

    /* SMTP Settings Keys */
    public static final String SMTP_HOST_KEY = "smtp_host";
    public static final String SMTP_PORT_KEY = "smtp_port";
    public static final String SMTP_IS_SSL_KEY = "smtp_isssl";
    public static final String SMTP_USERNAME_KEY = "smtp_username";
    public static final String SMTP_PASSWORD_KEY = "smtp_password";
    public static final String SMTP_STATUS_KEY = "smtp_status";

    /* Regex */
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*.()\\s])[A-Za-z\\d!@#$%^&*.()][A-Za-z\\d\\s!@#$%^&*.()]{6,48}[A-Za-z\\d!@#$%^&*.()]$";

    /* Remember me */
    public static final String REMEMBER_ME_SECRET_KEY = "AjksIHipoJjbiw(&(*98289)jasnkju(*^*BHBias90990120u))";
}
