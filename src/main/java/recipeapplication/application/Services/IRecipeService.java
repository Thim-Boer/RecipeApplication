package recipeapplication.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import recipeapplication.application.models.Recipe;
import recipeapplication.application.models.UpdateRecipeModel;

public interface IRecipeService {

    void insertEntity(Recipe recipe);

    List<Recipe> getAllRecipes();

    Optional<Recipe> getRecipeById(Long id);

    ResponseEntity<?> updateRecipe(UpdateRecipeModel recipe);
}
