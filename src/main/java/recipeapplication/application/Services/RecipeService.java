package recipeapplication.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import recipeapplication.application.models.Image;
import recipeapplication.application.models.Recipe;
import recipeapplication.application.models.UpdateRecipeModel;
import recipeapplication.application.models.User;
import recipeapplication.application.repository.RecipeRepository;
import recipeapplication.application.repository.ImageRepository;

@Service
public class RecipeService implements IRecipeService {

    private final RecipeRepository recipeRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, ImageRepository imageRepository) {
        this.recipeRepository = recipeRepository;
        this.imageRepository = imageRepository;
    }
    
    @Override
    public boolean checkIfUserIsAuthorized(User user, Recipe recipe) {
        var userId = user.getId();
        var userRole = user.getRole().name();
        if(!userId.equals(recipe.userId)) {
            if(userRole.equals("ADMIN")) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void insertRecipe(Recipe recipe) {
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
    public List<Recipe> getRecipeByName(String searchterm) {
        return recipeRepository.findByNameContainingIgnoreCase(searchterm);
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
    
    @Override
    public ResponseEntity<?> deleteRecipe(Recipe recipe) {
        var foundRecipe = recipeRepository.findById(recipe.id);
        if(!foundRecipe.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        recipeRepository.delete(recipe);
        return ResponseEntity.ok().build();
    }
    
    @Override
    public ResponseEntity<?> UploadImage(Image image) {
        imageRepository.save(image);
        return ResponseEntity.ok().build();
    }
}
