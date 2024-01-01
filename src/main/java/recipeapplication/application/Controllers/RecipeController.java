package recipeapplication.application.controllers;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import recipeapplication.application.dto.UploadDto;
import recipeapplication.application.models.Image;
import recipeapplication.application.models.Recipe;
import recipeapplication.application.models.UpdateRecipeModel;
import recipeapplication.application.models.User;
import recipeapplication.application.services.IRecipeService;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/api/recipes")
@RestController
public class RecipeController {
    private IRecipeService recipeService;

    @Autowired
    public RecipeController(IRecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/addRecipe")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VIEWER')")
    public ResponseEntity<String> addRecipeToList(@RequestBody Recipe recipe) {
        recipeService.insertRecipe(recipe);
        return ResponseEntity.ok("This recipe has been added");
    }

    @PutMapping("/updateRecipe")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VIEWER')")
    public ResponseEntity<?> changeRecipe(@RequestBody UpdateRecipeModel updateModel) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDetails = (User) principal;
        if (!recipeService.checkIfUserIsAuthorized(userDetails, updateModel.getOriginal())) {
            return ResponseEntity.badRequest().body("You are not allowed to do this action");
        }
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
        if (result.isEmpty()) {
            return ResponseEntity.badRequest().body("No entity found");
        }
        return ResponseEntity.ok().body(result);
    }
    
    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VIEWER')")
    public ResponseEntity<?> handleFileUpload(@ModelAttribute UploadDto file) {
        try {
            byte[] fileBytes = file.getFile().getBytes();
            byte[] base64String = Base64.getEncoder().encode(fileBytes);
            Image image = new Image(file.getId(), file.getRecipeId(), base64String);
            return this.recipeService.UploadImage(image);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping("/searchRecipeByName/{searchterm}")
    public ResponseEntity<?> getRecipeByName(@PathVariable String searchterm) {
        var result = recipeService.getRecipeByName(searchterm);
        if (result.isEmpty()) {
            return ResponseEntity.badRequest().body("No entity found");
        }
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/deleteRecipe")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VIEWER')")
    public ResponseEntity<?> deleteRecipe(@RequestBody Recipe recipe) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDetails = (User) principal;
        if (!recipeService.checkIfUserIsAuthorized(userDetails, recipe)) {
            return ResponseEntity.badRequest().body("You are not allowed to do this action");
        }
        return recipeService.deleteRecipe(recipe);
    }

    @PostConstruct
    public void executeOnStartup() {
        Recipe entity1 = new Recipe(42L, "Random Recipe", "Mix all ingredients and cook for 30 minutes.", 45, 3, 4,
                "Calories: 300, Protein: 15g, Fat: 10g", "Gluten, Dairy", 5, 1, "Mixing bowl, Pan",
                "Flour, Eggs, Milk, Sugar, Salt");
        Recipe entity2 = new Recipe(123456789L, "New Recipe Name",
                "Combine ingredients and bake at 350°F for 45 minutes.", 30, 5, 2,
                "Calories: 250, Protein: 20g, Fat: 8g", "Gluten-free, Vegan", 7, 5, "Baking dish, Mixing bowl, Oven",
                "Flour, Sugar, Baking powder, Salt, Almond milk");
        recipeService.insertRecipe(entity1);
        recipeService.insertRecipe(entity2);
    }
}
