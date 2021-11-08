package hu.uni.eku.tzs.service.exceptions;

public class SaleNotFoundException extends Exception {
    public SaleNotFoundException() {}

    public SaleNotFoundException(String message) {
        super(message);
    }

    public SaleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaleNotFoundException(Throwable cause) {
        super(cause);
    }

    public SaleNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
