package recipeapplication.application.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
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

    Image uploadImage(MultipartFile image, Long id);

    byte[] downloadPdf(Long id);

    Image deleteImage(Long id);
}
