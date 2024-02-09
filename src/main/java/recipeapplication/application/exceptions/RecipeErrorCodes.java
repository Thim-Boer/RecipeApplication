package recipeapplication.application.exceptions;

public record RecipeErrorCodes(String ErrorDescription) {
    public static final RecipeErrorCodes DataNotFound = new RecipeErrorCodes("Er is geen data gevonden.");
    public static final RecipeErrorCodes CannotDelete = new RecipeErrorCodes("Deze kan niet verwijderd worden.");
    public static final RecipeErrorCodes AlreadyUsed = new RecipeErrorCodes("Deze is al in gebruik.");
    public static final RecipeErrorCodes NoHitWithSearchCriteria = new RecipeErrorCodes("Er is geen recept gevonden met deze zoekopdracht.");
    public static final RecipeErrorCodes NotAuthorized = new RecipeErrorCodes("Je hebt geen rechten om dit te doen.");
    public static final RecipeErrorCodes IdsDonotMatch = new RecipeErrorCodes("De identifiers komen niet overeen.");
    public static final RecipeErrorCodes CannotBeUploaded = new RecipeErrorCodes("Het huidige bestand kan niet worden geupload.");
    public static final RecipeErrorCodes DataAlreadyExists = new RecipeErrorCodes("Deze data bestaat al.");
}
