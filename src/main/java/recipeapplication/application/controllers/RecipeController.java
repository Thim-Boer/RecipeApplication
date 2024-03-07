package recipeapplication.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import recipeapplication.application.exceptions.NotificationCollector;
import recipeapplication.application.dto.UpdateRecipeModel;
import recipeapplication.application.dto.UploadDto;
import recipeapplication.application.models.Recipe;
import recipeapplication.application.services.IRecipeService;

import java.util.List;

@Controller
@RequestMapping("/api/recipes")
@RestController
public class RecipeController {
    private final IRecipeService recipeService;

    @Autowired
    public RecipeController(IRecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/recipe")
    public ResponseEntity<?> addRecipeToList(@RequestBody Recipe recipe) {
        return recipeService.insertRecipe(recipe);
    }

    @PutMapping("/recipe")
    public ResponseEntity<?> changeRecipe(@RequestBody UpdateRecipeModel updateModel) {
        NotificationCollector collection = new NotificationCollector();
        var result = recipeService.updateRecipe(collection, updateModel);
        if (collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.ReturnErrors());
        }
        return result;
    }

    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable Long id) {
        NotificationCollector collection = new NotificationCollector();
        var result = recipeService.getRecipeById(collection, id);
        if (collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.ReturnErrors());
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/recipe/{id}/document")
    public ResponseEntity<?> handleFileUpload(@ModelAttribute UploadDto file) {
        return this.recipeService.uploadImage(file);
    }

    @GetMapping("/recipe/{id}/pdf")
    public ResponseEntity<?> downloadPdf(@PathVariable Long id) {
        return this.recipeService.downloadPdf(id);
    }

    @GetMapping("/recipe?searchterm={searchterm}")
    public ResponseEntity<?> getRecipeByName(@PathVariable String searchterm) {
        NotificationCollector collection = new NotificationCollector();
        var result = recipeService.getRecipeByName(collection, searchterm);

        if (collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.ReturnErrors());
        }
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        NotificationCollector collection = new NotificationCollector();

        var result = recipeService.deleteRecipe(collection, id);
        if (collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.ReturnErrors());
        }
        return result;
    }
}
