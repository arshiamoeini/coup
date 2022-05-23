package models;

public class Result {
    public static final Result SUCCESSFUL = new Result(Result.Type.SUCCESSFUL, "");
    public static final Result DEFEAT_CHALLENGE = new Result(Type.UNSUCCESSFUL, "defeat challenge");
    public static final Result PREVENTED = new Result(Type.UNSUCCESSFUL, "blocked");
    public static final Result NOT_COMPLETE = new Result(Type.UNSUCCESSFUL, "not complete");

    public enum Type {
        CAN_NOT,
        UNSUCCESSFUL,
        SUCCESSFUL,
    }
    private Type type;
    private String message;

    public Result(Type type, String message) {
        this.type = type;
        this.message = message;
    }
    public static Result CAN_NOT(int less) {
        return new Result(Type.CAN_NOT, "need "+less+" coin more");
    }

    public boolean isSuccessful() {
        return type == Type.SUCCESSFUL;
    }

    public Type getType() {
        return type;
    }
    public String getMessage() {
        return message;
    }
    public boolean isNotComplete() {
        return this == NOT_COMPLETE;
    }
}
