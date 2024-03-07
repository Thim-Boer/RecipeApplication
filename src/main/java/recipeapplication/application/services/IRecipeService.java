package recipeapplication.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import recipeapplication.application.exceptions.NotificationCollector;
import recipeapplication.application.dto.UpdateRecipeModel;
import recipeapplication.application.dto.UploadDto;
import recipeapplication.application.models.Image;
import recipeapplication.application.models.Recipe;

public interface IRecipeService {
    boolean checkIfUserIsAuthorized(Recipe recipe);

    ResponseEntity<?> insertRecipe(Recipe recipe);

    List<Recipe> getAllRecipes();

    Optional<Recipe> getRecipeById(NotificationCollector collection, Long id);

    List<Recipe> getRecipeByName(NotificationCollector collection, String searchTerm);

    ResponseEntity<?> updateRecipe(NotificationCollector collection, UpdateRecipeModel recipe);

    ResponseEntity<?> deleteRecipe(NotificationCollector collection, Long id);

    ResponseEntity<?> uploadImage(UploadDto image);

    Optional<Image> getImage();

    ResponseEntity<?> downloadPdf(Long id);

    void insertcategories();
}
