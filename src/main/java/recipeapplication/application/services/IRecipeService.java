package recipeapplication.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import recipeapplication.application.Exceptions.NotificationCollector;
import recipeapplication.application.dto.UploadDto;
import recipeapplication.application.models.Image;
import recipeapplication.application.models.Recipe;
import recipeapplication.application.models.UpdateRecipeModel;

public interface IRecipeService {
    boolean checkIfUserIsAuthorized(Recipe recipe);

    ResponseEntity<?> insertRecipe(Recipe recipe);

    List<Recipe> getAllRecipes();

    Optional<Recipe> getRecipeById(NotificationCollector collection, Long id);

    List<Recipe> getRecipeByName(NotificationCollector collection, String searchTerm);

    ResponseEntity<?> updateRecipe(NotificationCollector collection, UpdateRecipeModel recipe);

    ResponseEntity<?> deleteRecipe(NotificationCollector collection, Recipe recipe);

    ResponseEntity<?> uploadImage(UploadDto image);

    Optional<Image> getImage();

    ResponseEntity<?> downloadPdf(Long id);
}
