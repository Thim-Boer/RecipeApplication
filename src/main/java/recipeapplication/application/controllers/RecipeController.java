package recipeapplication.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import recipeapplication.application.exceptions.ValidationException;
import recipeapplication.application.models.Image;
import recipeapplication.application.models.Recipe;
import recipeapplication.application.services.IRecipeService;

import java.net.URI;
import java.util.ArrayList;
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
    public ResponseEntity<?> addRecipeToList(@RequestBody @Validated Recipe recipe, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        Recipe result = recipeService.insertRecipe(recipe);
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/" + result.id).toUriString());
        return ResponseEntity.created(uri).body(result);
    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody @Validated Recipe recipe, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
            throw new ValidationException(errors);
        }

        return ResponseEntity.ok().body(recipeService.updateRecipe(recipe, id));
    }

    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        return ResponseEntity.ok().body(recipeService.getRecipeById(id));
    }

    @PostMapping("/recipe/{id}/document")
    public ResponseEntity<Image> handleFileUpload(@RequestParam(value = "image", required = true) MultipartFile file, @PathVariable Long id) {
        return ResponseEntity.ok().body(this.recipeService.uploadImage(file, id));
    }

    @GetMapping("/recipe/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        byte[] pdfBytes = this.recipeService.downloadPdf(id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
    }

    @GetMapping("/recipe?searchterm={searchterm}")
    public ResponseEntity<List<Recipe>> getRecipeByName(@PathVariable String searchterm) {
        var result = recipeService.getRecipeByName(searchterm);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<Recipe> deleteRecipe(@PathVariable Long id) {
        var result = recipeService.deleteRecipe(id);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/recipe/{id}/image")
    public ResponseEntity<Image> deleteImage(@PathVariable Long id) {
        Image result = recipeService.deleteImage(id);
        return ResponseEntity.ok().body(result);
    }
}
