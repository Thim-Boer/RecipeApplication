package recipeapplication.application.exceptions;

import java.util.ArrayList;

public class NotificationCollector {
    public ArrayList<RecipeErrorCodes> errors = new ArrayList<RecipeErrorCodes>();

    public void AddErrorToCollection(RecipeErrorCodes error) {
        errors.add(error);
    }

    public boolean HasErrors() {
        return errors.size() > 0 ? true : false;
    }

    public ArrayList<RecipeErrorCodes> ReturnErrors(){
        return errors;
    }
}
