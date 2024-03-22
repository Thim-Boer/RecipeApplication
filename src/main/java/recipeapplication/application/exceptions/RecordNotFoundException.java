package recipeapplication.application.exceptions;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String description) {
        super(description);
    }
}
