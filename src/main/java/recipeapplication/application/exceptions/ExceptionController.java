package recipeapplication.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = RecordNotFoundException.class)
    public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = UserIsNotAuthorizedException.class)
    public ResponseEntity<Object> handleUserIsNotAuthorizedException(UserIsNotAuthorizedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(value = EntityIsNotUniqueException.class)
    public ResponseEntity<Object> handleEntityIsNotUniqueException(EntityIsNotUniqueException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(value = IdentifiersDoNotMatchException.class)
    public ResponseEntity<Object> handleIdentifiersDoNotMatchException(IdentifiersDoNotMatchException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(value = WrongLoginDetailsException.class)
    public ResponseEntity<Object> handleWrongLoginDetailsException(WrongLoginDetailsException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}