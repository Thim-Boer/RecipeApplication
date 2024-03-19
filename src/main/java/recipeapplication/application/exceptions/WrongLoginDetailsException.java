package recipeapplication.application.exceptions;

public class WrongLoginDetailsException extends RuntimeException{
    public WrongLoginDetailsException() {
        super();
    }

    public WrongLoginDetailsException(String description) {
        super(description);
    }
}
