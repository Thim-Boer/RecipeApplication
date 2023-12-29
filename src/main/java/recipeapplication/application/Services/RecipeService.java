package recipeapplication.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import recipeapplication.application.models.Recipe;
import recipeapplication.application.models.UpdateRecipeModel;
import recipeapplication.application.repository.RecipeRepository;

@Service
public class RecipeService implements IRecipeService {

    private RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
    
    @Override
    public void insertEntity(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    @Override
    public ResponseEntity<?> updateRecipe(UpdateRecipeModel recipes) {
        var orginalRecipe = recipes.getOriginal();
        if(!recipes.doIdsMatch()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        var recipe = recipeRepository.findById(orginalRecipe.id);
        if(!recipe.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        recipeRepository.save(recipes.getUpdated());
        return ResponseEntity.ok().build();
    }
}
