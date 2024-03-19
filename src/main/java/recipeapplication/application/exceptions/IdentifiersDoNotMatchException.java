package recipeapplication.application.exceptions;

public class IdentifiersDoNotMatchException extends RuntimeException {
    public IdentifiersDoNotMatchException() {
        super();
    }

    public IdentifiersDoNotMatchException(String description){
        super(description);
    }
}
