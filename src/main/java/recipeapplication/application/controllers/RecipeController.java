package recipeapplication.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
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
    private IRecipeService recipeService;

    @Autowired
    public RecipeController(IRecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/addRecipe")
    public ResponseEntity<?> AddRecipeToList(@RequestBody Recipe recipe) {
        return recipeService.InsertRecipe(recipe);
    }

    @PutMapping("/updateRecipe")
    public ResponseEntity<?> ChangeRecipe(@RequestBody UpdateRecipeModel updateModel) {
        NotificationCollector collection = new NotificationCollector();
        var result = recipeService.UpdateRecipe(collection, updateModel);
        if (collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.ReturnErrors());
        }
        return result;
    }

    @GetMapping("/searchRecipes")
    public List<Recipe> GetAllRecipes() {
        return recipeService.GetAllRecipes();
    }

    @GetMapping("/searchRecipeById/{id}")
    public ResponseEntity<?> GetRecipeById(@PathVariable Long id) {
        NotificationCollector collection = new NotificationCollector();
        var result = recipeService.GetRecipeById(collection, id);
        if (collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.ReturnErrors());
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> HandleFileUpload(@ModelAttribute UploadDto file) {
        return this.recipeService.UploadImage(file);
    }

    @GetMapping("/downloadImage")
    public ResponseEntity<?> DownloadPdf(@PathVariable Long id) {
        return this.recipeService.DownloadPdf(id);
    }

    @GetMapping("/searchRecipeByName/{searchterm}")
    public ResponseEntity<?> GetRecipeByName(@PathVariable String searchterm) {
        NotificationCollector collection = new NotificationCollector();
        var result = recipeService.GetRecipeByName(collection, searchterm);

        if (collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.ReturnErrors());
        }
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/deleteRecipe/{id}")
    public ResponseEntity<?> DeleteRecipe(@PathVariable Long id) {
        NotificationCollector collection = new NotificationCollector();

        var result = recipeService.DeleteRecipe(collection, id);
        if (collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.ReturnErrors());
        }
        return result;
    }
}
