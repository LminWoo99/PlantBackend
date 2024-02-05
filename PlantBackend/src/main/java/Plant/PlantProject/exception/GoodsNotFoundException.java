package Plant.PlantProject.exception;

public class GoodsNotFoundException extends RuntimeException{
    public GoodsNotFoundException() {
    }

    public GoodsNotFoundException(String message) {
        super(message);
    }

    public GoodsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
