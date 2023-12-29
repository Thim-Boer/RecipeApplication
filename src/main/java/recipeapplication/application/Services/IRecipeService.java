package recipeapplication.application.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import recipeapplication.application.Models.Recipe;
import recipeapplication.application.Models.UpdateRecipeModel;

public interface IRecipeService {

    void insertEntity(Recipe recipe);

    List<Recipe> getAllRecipes();

    Optional<Recipe> getRecipeById(Long id);

    ResponseEntity<?> updateRecipe(UpdateRecipeModel recipe);
}
