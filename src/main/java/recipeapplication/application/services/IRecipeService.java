package recipeapplication.application.services;

import java.util.List;
import java.util.Optional;

import recipeapplication.application.dto.UpdateRecipeModel;
import recipeapplication.application.dto.UploadDto;
import recipeapplication.application.models.Image;
import recipeapplication.application.models.Recipe;

public interface IRecipeService {
    boolean checkIfUserIsAuthorized(Recipe recipe);

    Recipe insertRecipe(Recipe recipe);

    List<Recipe> getAllRecipes();

    Recipe getRecipeById(Long id);

    List<Recipe> getRecipeByName(String searchTerm);

    Recipe updateRecipe(Recipe recipe, Long id);

    Recipe deleteRecipe(Long id);

    Image uploadImage(UploadDto image);

    Optional<Image> getImage();

    byte[] downloadPdf(Long id);

//    void insertcategories();
}
