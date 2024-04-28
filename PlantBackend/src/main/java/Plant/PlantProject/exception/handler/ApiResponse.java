package Plant.PlantProject.exception.handler;
public record ApiResponse(
        ApiStatus status,
        String message,
        String errorCodeName,
        Object data
) {
    public static ApiResponse success(Object data) {
        return new ApiResponse(ApiStatus.SUCCESS, null, null, data);
    }
    public static ApiResponse error(String message, String errorCodeName) {
        return new ApiResponse(ApiStatus.ERROR, message, errorCodeName, null);
    }
}
