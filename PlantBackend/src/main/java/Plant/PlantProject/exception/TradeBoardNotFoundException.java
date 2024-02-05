package Plant.PlantProject.exception;

public class TradeBoardNotFoundException extends RuntimeException {
    public TradeBoardNotFoundException() {
    }

    public TradeBoardNotFoundException(String message) {
        super(message);
    }

    public TradeBoardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}