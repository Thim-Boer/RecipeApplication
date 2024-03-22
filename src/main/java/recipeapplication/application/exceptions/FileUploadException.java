package recipeapplication.application.exceptions;

public class FileUploadException extends RuntimeException{

    public FileUploadException(String description) {
        super(description);
    }

}
