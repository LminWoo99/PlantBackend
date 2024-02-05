package Plant.PlantProject.exception;

public class PlantNotFoundException extends RuntimeException {
    public PlantNotFoundException() {
    }

    public PlantNotFoundException(String message) {
        super(message);
    }

    public PlantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}