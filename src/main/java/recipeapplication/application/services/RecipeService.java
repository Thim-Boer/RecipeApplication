package recipeapplication.application.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import recipeapplication.application.dto.UploadDto;
import recipeapplication.application.exceptions.*;
import recipeapplication.application.models.Image;
import recipeapplication.application.models.Recipe;
import recipeapplication.application.models.User;
import recipeapplication.application.repository.ImageRepository;
import recipeapplication.application.repository.RecipeRepository;
import recipeapplication.application.repository.UserRepository;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> foundUser = userRepository.findById(userDetails.getId());

        foundUser.map(user -> false);
        return false;
    }

    @Override
    public Recipe insertRecipe(Recipe recipe) {
        if (!recipeRepository.findByNameContainingIgnoreCase(recipe.name).isEmpty()) {
            throw new EntityIsNotUniqueException("Er bestaat al een recept met deze naam: " + recipe.name);
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        recipe.id = user.getId().longValue();
        return recipeRepository.save(recipe);
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> {
                    throw new RecordNotFoundException("Het recept met de identifier: " + id + " is niet gevonden");
                });
    }

    @Override
    public List<Recipe> getRecipeByName(String searchterm) {
        return recipeRepository.findByNameContainingIgnoreCase(searchterm);
    }

    @Override
    public Recipe updateRecipe(Recipe recipe, Long id) {
        Recipe foundRecipe = getRecipeById(id);

        if (!checkIfUserIsAuthorized(foundRecipe)) {
            throw new UserIsNotAuthorizedException("Je hebt geen rechten om dit recept te updaten");
        }

        if (!recipe.id.equals(id)) {
            throw new IdentifiersDoNotMatchException("De opgegeven identifiers komen niet overeen");
        }

        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe deleteRecipe(Long id) {
        Recipe foundRecipe = getRecipeById(id);

        if (!checkIfUserIsAuthorized(foundRecipe)) {
            throw new UserIsNotAuthorizedException("Je hebt geen rechten om dit recept te verwijderen");
        }

        recipeRepository.delete(foundRecipe);
        return foundRecipe;
    }

    @Override
    public Image uploadImage(UploadDto file) {
        try {
            byte[] fileBytes = file.image.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(fileBytes);
            Image image = new Image(file.id, file.recipe, base64Image);
            return imageRepository.save(image);
        } catch (Exception e) {
            throw new FileUploadException("Fout bij het uploaden van de afbeelding");
        }
    }

    @Override
    public Optional<Image> getImage() {
        return imageRepository.findById(1L);
    }

    @Override
    public byte[] downloadPdf(Long id) {
        Recipe foundRecipe = getRecipeById(id);
        String pdfContent = buildPdfContent(foundRecipe);
        ByteArrayOutputStream pdfStream = generatePdf(pdfContent);

        return pdfStream != null ? pdfStream.toByteArray() : null;
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
            contentBuilder.append("* ").append(ingredient.trim()).append("\n");
        }

        contentBuilder.append("\nInstructions: \n\n");
        String[] instructions = recipe.instructions.split("\\.");
        for (String instruction : instructions) {
            contentBuilder.append("*  ").append(instruction.trim()).append("\n");
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
            return null;
        }
        return baos;
    }

}
