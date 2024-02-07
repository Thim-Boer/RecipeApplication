package recipeapplication.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import recipeapplication.application.Exceptions.NotificationCollector;
import recipeapplication.application.Exceptions.RecipeErrorCodes;
import recipeapplication.application.dto.UploadDto;
import recipeapplication.application.models.Image;
import recipeapplication.application.models.Recipe;
import recipeapplication.application.models.UpdateRecipeModel;
import recipeapplication.application.models.User;
import recipeapplication.application.repository.RecipeRepository;
import recipeapplication.application.repository.UserRepository;
import recipeapplication.application.repository.ImageRepository;

@Service
public class RecipeService implements IRecipeService {

    private final RecipeRepository recipeRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, ImageRepository imageRepository,
            UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean checkIfUserIsAuthorized(Recipe recipe) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDetails = (User) principal;
        var foundUser = userRepository.findById(userDetails.getId());
        var userRole = userDetails.getRole().name();

        if (!foundUser.isPresent()) {
            return false;
        } else {
            if (!foundUser.get().getId().equals(recipe.userId)) {
                if (userRole.equals("ADMIN")) {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public ResponseEntity<?> insertRecipe(Recipe recipe) {
        if (!recipeRepository.findByNameContainingIgnoreCase(recipe.name).isEmpty()) {
            return ResponseEntity.badRequest().body(RecipeErrorCodes.DataAlreadyExists);
        }

        recipeRepository.save(recipe);
        return ResponseEntity.ok().build();
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Optional<Recipe> getRecipeById(NotificationCollector collection, Long id) {
        var result = recipeRepository.findById(id);
        if (!result.isPresent()) {
            collection.AddErrorToCollection(RecipeErrorCodes.DataNotFound);
        }
        return result;
    }

    @Override
    public List<Recipe> getRecipeByName(NotificationCollector collection, String searchterm) {
        var result = recipeRepository.findByNameContainingIgnoreCase(searchterm);
        if (result.isEmpty()) {
            collection.AddErrorToCollection(RecipeErrorCodes.DataNotFound);
        }
        return result;
    }

    @Override
    public ResponseEntity<?> updateRecipe(NotificationCollector collection, UpdateRecipeModel recipes) {
        var orginalRecipe = recipes.getOriginal();
        if (!checkIfUserIsAuthorized(orginalRecipe)) {
            collection.AddErrorToCollection(RecipeErrorCodes.NotAuthorized);
        }
        if (!recipes.doIdsMatch()) {
            collection.AddErrorToCollection(RecipeErrorCodes.IdsDonotMatch);
        }
        var recipe = recipeRepository.findById(orginalRecipe.id);
        if (!recipe.isPresent()) {
            collection.AddErrorToCollection(RecipeErrorCodes.DataNotFound);
        }
        if (collection.HasErrors()) {
            return null;
        }
        recipeRepository.save(recipes.getUpdated());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> deleteRecipe(NotificationCollector collection, Recipe recipe) {
        if (!checkIfUserIsAuthorized(recipe)) {
            collection.AddErrorToCollection(RecipeErrorCodes.NotAuthorized);
        }
        var foundRecipe = recipeRepository.findById(recipe.id);
        if (!foundRecipe.isPresent()) {
            collection.AddErrorToCollection(RecipeErrorCodes.DataNotFound);
        }
        if (collection.HasErrors()) {
            return null;
        }
        recipeRepository.delete(recipe);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> uploadImage(UploadDto file) {
        try {
            byte[] fileBytes = file.image.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(fileBytes);
            Image image = new Image(file.getId(), file.getRecipe(), base64Image);
            imageRepository.save(image);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(RecipeErrorCodes.CannotBeUploaded);
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public Optional<Image> getImage() {
        return imageRepository.findById(1L);
    }

    @Override
    public ResponseEntity<?> downloadPdf(Long id) {
        Optional<Recipe> foundRecipeResponse = getRecipeById(new NotificationCollector(), id);

        if (foundRecipeResponse.isPresent()) {
            var recipe = foundRecipeResponse.get();
            String pdfContent = buildPdfContent(recipe);

            ByteArrayOutputStream pdfStream = generatePdf(pdfContent);

            if (pdfStream != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", recipe.name + ".pdf");

                headers.setContentLength(pdfStream.size());

                return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating PDF");
            }
        } else{
            return ResponseEntity.badRequest().body(RecipeErrorCodes.DataNotFound);
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
