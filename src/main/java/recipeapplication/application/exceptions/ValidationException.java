package recipeapplication.application.exceptions;

import java.util.List;

public class ValidationException extends RuntimeException{
    public ValidationException(){
        super();
    }

    public ValidationException(List<String> errors) {
        super("Validation failed: " + String.join(", ", errors));
    }
}
