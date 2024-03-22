package recipeapplication.application.exceptions;

public class UserIsNotAuthorizedException extends RuntimeException {

    public UserIsNotAuthorizedException(String description) {
        super(description);
    }
}
