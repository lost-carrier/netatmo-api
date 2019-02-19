package losty.netatmo.exceptions;

public class NetatmoException extends RuntimeException {

    public NetatmoException(String message) {
        super(message);
    }

    public NetatmoException(Exception exception) {
        super(exception);
    }

    public NetatmoException(String message, Exception exception) {
        super(message, exception);
    }
}
