package utils;

public class FootManagerException extends RuntimeException {
    private String errorCode;

    public FootManagerException(String message) {
        super(message);
    }

    public FootManagerException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public FootManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public FootManagerException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    // Common error codes
    public static final String DB_CONNECTION_ERROR = "DB001";
    public static final String DB_QUERY_ERROR = "DB002";
    public static final String VALIDATION_ERROR = "VAL001";
    public static final String AUTHENTICATION_ERROR = "AUTH001";
    public static final String AUTHORIZATION_ERROR = "AUTH002";
    public static final String ENTITY_NOT_FOUND = "ENT001";
    public static final String DUPLICATE_ENTRY = "ENT002";
}