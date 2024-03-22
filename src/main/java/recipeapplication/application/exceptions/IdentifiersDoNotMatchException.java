package recipeapplication.application.exceptions;

public class IdentifiersDoNotMatchException extends RuntimeException {

    public IdentifiersDoNotMatchException(String description){
        super(description);
    }
}
