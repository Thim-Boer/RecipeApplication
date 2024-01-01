package recipeapplication.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import recipeapplication.application.models.Image;
import recipeapplication.application.models.Recipe;
import recipeapplication.application.models.UpdateRecipeModel;
import recipeapplication.application.models.User;

public interface IRecipeService {
    boolean checkIfUserIsAuthorized(User user, Recipe recipe);

    void insertRecipe(Recipe recipe);

    List<Recipe> getAllRecipes();

    Optional<Recipe> getRecipeById(Long id);

    List<Recipe> getRecipeByName(String searchTerm);

    ResponseEntity<?> updateRecipe(UpdateRecipeModel recipe);

    ResponseEntity<?> deleteRecipe(Recipe recipe);

    ResponseEntity<?> UploadImage(Image image);
}
