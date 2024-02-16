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
    boolean CheckIfUserIsAuthorized(Recipe recipe);

    ResponseEntity<?> InsertRecipe(Recipe recipe);

    List<Recipe> GetAllRecipes();

    Optional<Recipe> GetRecipeById(NotificationCollector collection, Long id);

    List<Recipe> GetRecipeByName(NotificationCollector collection, String searchTerm);

    ResponseEntity<?> UpdateRecipe(NotificationCollector collection, UpdateRecipeModel recipe);

    ResponseEntity<?> DeleteRecipe(NotificationCollector collection, Long id);

    ResponseEntity<?> UploadImage(UploadDto image);

    Optional<Image> GetImage();

    ResponseEntity<?> DownloadPdf(Long id);

    void InsertCategories();
}
