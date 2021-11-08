package hu.uni.eku.tzs.service.exceptions;

public class SaleAlreadyExistsException extends Exception{
    public SaleAlreadyExistsException() {
    }

    public SaleAlreadyExistsException(String message) {
        super(message);
    }

    public SaleAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaleAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public SaleAlreadyExistsException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
