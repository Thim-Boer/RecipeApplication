package recipeapplication.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
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
        if (!userId.equals(recipe.userId)) {
            if (userRole.equals("ADMIN")) {
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
        if (!recipes.doIdsMatch()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        var recipe = recipeRepository.findById(orginalRecipe.id);
        if (!recipe.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        recipeRepository.save(recipes.getUpdated());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> deleteRecipe(Recipe recipe) {
        var foundRecipe = recipeRepository.findById(recipe.id);
        if (!foundRecipe.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        recipeRepository.delete(recipe);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> uploadImage(Image image) {
        imageRepository.save(image);
        return ResponseEntity.ok().build();
    }

    @Override
    public Optional<Image> getImage() {
        return imageRepository.findById(1L);
    }

    @Override
    public ResponseEntity<?> downloadPdf(Long id) {
        Optional<Recipe> foundRecipe = getRecipeById(id);

        if (foundRecipe.isPresent()) {

            String pdfContent = buildPdfContent(foundRecipe.get());
            ByteArrayOutputStream pdfStream = generatePdf(pdfContent);

            if (pdfStream != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", foundRecipe.get().name + ".pdf");
                headers.setContentLength(pdfStream.toByteArray().length);

                return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating PDF");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private String buildPdfContent(Recipe recipe) {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(recipe.name).append("\n\n")
                .append("Allergies: ").append(recipe.allergies).append("\n")
                .append("Nutritional Information: ").append(recipe.nutritionalInformation).append("\n")
                .append("Portion Size: ").append(recipe.portionSize).append("\n")
                .append("Difficulty: ").append(recipe.difficulty).append("\n");
                
                contentBuilder.append("\n\nIngredients:\n\n");
                String[] ingredients = recipe.ingredients.split("\\,");
                for (String ingredient : ingredients) {
                    contentBuilder.append("* " + ingredient.trim()).append("\n");
                }
                
                contentBuilder.append("\nInstructions: \n\n");
        String[] instructions = recipe.instructions.split("\\.");
        for (String instruction : instructions) {
            contentBuilder.append("*  " + instruction.trim()).append("\n");
        }

        return contentBuilder.toString();
    }

    private ByteArrayOutputStream generatePdf(String content) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

            Paragraph paragraph = new Paragraph(content, font);
            document.add(paragraph);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return baos;
    }
}
