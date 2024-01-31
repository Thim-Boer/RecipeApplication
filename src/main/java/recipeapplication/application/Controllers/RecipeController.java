package recipeapplication.application.controllers;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import recipeapplication.application.Exceptions.NotificationCollector;
import recipeapplication.application.dto.UploadDto;
import recipeapplication.application.models.Recipe;
import recipeapplication.application.models.UpdateRecipeModel;
import recipeapplication.application.services.IRecipeService;

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
    public ResponseEntity<?> addRecipeToList(@RequestBody @Validated Recipe recipe) {
            return recipeService.insertRecipe(recipe);
    }

    @PutMapping("/updateRecipe")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VIEWER')")
    public ResponseEntity<?> changeRecipe(@RequestBody @Validated UpdateRecipeModel updateModel) {
        NotificationCollector collection = new NotificationCollector();   
        var result = recipeService.updateRecipe(collection, updateModel);
        if(collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.returnErrors());
        }
        return result;
    }
    
    @GetMapping("/searchRecipes")
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }
    
    @GetMapping("/searchRecipeById/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VIEWER', 'USER')")
    public ResponseEntity<?> getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }
    
    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VIEWER')")
    public ResponseEntity<?> handleFileUpload(@ModelAttribute UploadDto file) {
        return this.recipeService.uploadImage(file);
    }
    
    @GetMapping("/downloadImage")
    public ResponseEntity<?> downloadPdf(@PathVariable Long id) {
        return this.recipeService.downloadPdf(id);
    }

    @GetMapping("/searchRecipeByName/{searchterm}")
    public ResponseEntity<?> getRecipeByName(@PathVariable String searchterm) {
        NotificationCollector collection = new NotificationCollector();
        var result = recipeService.getRecipeByName(collection, searchterm);

        if (collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.returnErrors());
        }
        return ResponseEntity.ok().body(result);
    }
    
    @DeleteMapping("/deleteRecipe")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VIEWER')")
    public ResponseEntity<?> deleteRecipe(@RequestBody @Validated Recipe recipe) {
        NotificationCollector collection = new NotificationCollector();
        
        var result = recipeService.deleteRecipe(collection, recipe);
        if(collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.returnErrors()); 
        }
        return result;
    }
    
    @PostConstruct
    public void executeOnStartup() {
        Recipe entity1 = new Recipe(42L, "Random Recipe", "Mix all ingredients. Then cook for 30 minutes. Leave for another 5. You are done.", 45, 3, 4,
        "Calories: 300, Protein: 15g, Fat: 10g", "Gluten, Dairy", 5, 1, "Mixing bowl, Pan",
        "500g Flour, 4 Eggs, 500ml Milk, 75g Sugar, 25g Salt");
        Recipe entity2 = new Recipe(123456789L, "New Recipe Name",
                "Combine ingredients and bake at 350°F for 45 minutes.", 30, 5, 2,
                "Calories: 250, Protein: 20g, Fat: 8g", "Gluten-free, Vegan", 7, 5, "Baking dish, Mixing bowl, Oven",
                "Flour, Sugar, Baking powder, Salt, Almond milk");
                recipeService.insertRecipe(entity1);
                recipeService.insertRecipe(entity2);
            }
        }
        