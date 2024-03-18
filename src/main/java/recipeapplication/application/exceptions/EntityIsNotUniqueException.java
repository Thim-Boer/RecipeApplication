package recipeapplication.application.exceptions;

public class EntityIsNotUniqueException extends RuntimeException {
    public EntityIsNotUniqueException() {
        super();
    }

    public EntityIsNotUniqueException(String description) {
        super(description);
    }
}
