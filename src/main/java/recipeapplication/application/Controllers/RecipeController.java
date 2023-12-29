package recipeapplication.application.Controllers;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import recipeapplication.application.Models.Recipe;
import recipeapplication.application.Models.UpdateRecipeModel;
import recipeapplication.application.Services.IRecipeService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/recipes")
@RestController
public class RecipeController {
    ArrayList<Recipe> recipeList = new ArrayList<>();

    private final IRecipeService recipeService;
    
    @Autowired
    public RecipeController(IRecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/addRecipe")
    public ResponseEntity<String> addRecipeToList(@RequestBody Recipe recipe) {
        recipeList.add(recipe);
        recipeService.insertEntity(recipe);
        return ResponseEntity.ok("This recipe has been added");
    }
    
    @PutMapping("/updateRecipe")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VIEWER', 'USER')")
    public ResponseEntity<?> changeRecipe(@RequestBody UpdateRecipeModel updateModel) {
        return recipeService.updateRecipe(updateModel);
    }
    
    @GetMapping("/searchRecipes")
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }
    
    @GetMapping("/searchRecipeById/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VIEWER', 'USER')")
    public ResponseEntity<?> getRecipeById(@PathVariable Long id) {
        var result = recipeService.getRecipeById(id);
        if(result.isEmpty()) {
            return ResponseEntity.badRequest().body("No entity found");
        }
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/deleteRecipe")
    public ResponseEntity<String> deleteRecipe(@RequestBody Recipe recipe) {
        var removed = recipeList.remove(recipe);
        if (removed) {
            return ResponseEntity.ok("Recipe successfully deleted");
        } else {
            return ResponseEntity.ok("Recipe not found");
        }
    }

    @PostConstruct
    public void executeOnStartup() {
        Recipe entity1 = new Recipe(42L,"Random Recipe","Mix all ingredients and cook for 30 minutes.",45,3,4,"Calories: 300, Protein: 15g, Fat: 10g","Gluten, Dairy",5,101,"Mixing bowl, Pan","Flour, Eggs, Milk, Sugar, Salt");
        Recipe entity2 = new Recipe(123456789L,"New Recipe Name","Combine ingredients and bake at 350°F for 45 minutes.",30,5,2,"Calories: 250, Protein: 20g, Fat: 8g","Gluten-free, Vegan",7,205,"Baking dish, Mixing bowl, Oven","Flour, Sugar, Baking powder, Salt, Almond milk");
        recipeService.insertEntity(entity1);
        recipeService.insertEntity(entity2);
    }
}
