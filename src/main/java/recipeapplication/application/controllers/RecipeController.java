package recipeapplication.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import recipeapplication.application.dto.UpdateRecipeModel;
import recipeapplication.application.dto.UploadDto;
import recipeapplication.application.models.Recipe;
import recipeapplication.application.services.IRecipeService;

import java.net.URI;
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
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/" + recipe.id).toUriString());
        return ResponseEntity.created(uri).body(recipeService.insertRecipe(recipe));
    }

    @PutMapping("/recipe")
    public ResponseEntity<?> changeRecipe(@RequestBody UpdateRecipeModel updateModel) {
        var result = recipeService.updateRecipe(updateModel);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable Long id) {
        var result = recipeService.getRecipeById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/recipe/{id}/document")
    public ResponseEntity<?> handleFileUpload(@ModelAttribute UploadDto file) {
        return ResponseEntity.ok().body(this.recipeService.uploadImage(file));
    }

    @GetMapping("/recipe/{id}/pdf")
    public ResponseEntity<?> downloadPdf(@PathVariable Long id) {
        return ResponseEntity.ok().body(this.recipeService.downloadPdf(id));
    }

    @GetMapping("/recipe?searchterm={searchterm}")
    public ResponseEntity<?> getRecipeByName(@PathVariable String searchterm) {
        var result = recipeService.getRecipeByName(searchterm);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        var result = recipeService.deleteRecipe(id);
        return ResponseEntity.ok().body(result);
    }
}
