package recipeapplication.application.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import recipeapplication.application.exceptions.*;
import recipeapplication.application.models.Image;
import recipeapplication.application.models.Recipe;
import recipeapplication.application.models.User;
import recipeapplication.application.repository.ImageRepository;
import recipeapplication.application.repository.RecipeRepository;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;


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
    public boolean checkIfUserIsAuthorized(Recipe recipe) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            User userDetails = (User) authentication.getPrincipal();
            return recipe.userId == userDetails.getId() || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
        }
        return false;
    }

    @Override
    public Recipe insertRecipe(Recipe recipe) {
        if (!recipeRepository.findByNameContainingIgnoreCase(recipe.name).isEmpty()) {
            throw new EntityIsNotUniqueException("Er bestaat al een recept met deze naam: " + recipe.name);
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        recipe.userId = user.getId();
        return recipeRepository.save(recipe);
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Het recept met de identifier: " + id + " is niet gevonden"));
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

        recipe.id = id;
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
    public Image deleteImage(Long id) {
        Recipe foundRecipe = getRecipeById(id);
        if (!checkIfUserIsAuthorized(foundRecipe)) {
            throw new UserIsNotAuthorizedException("Je hebt geen rechten om deze afbeelding te verwijderen");
        }

        Image image = imageRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Er bestaat geen foto met deze identifier"));
        imageRepository.delete(image);
        return image;
    }

    @Override
    public Image uploadImage(MultipartFile file, Long id) {
            Recipe foundRecipe = getRecipeById(id);
            if (!checkIfUserIsAuthorized(foundRecipe)) {
                throw new UserIsNotAuthorizedException("Je hebt geen rechten om bij dit recept een foto te uploaden");
            }

            if(imageRepository.findById(id).isPresent()){
                throw new EntityIsNotUniqueException("Dit recept bevat al een afbeelding");
            }
            try {
                byte[] fileBytes = file.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(fileBytes);
                Image image = new Image(id, foundRecipe, base64Image);
                imageRepository.save(image);
                return image;
            }
            catch (Exception e) {
                throw new FileUploadException(e.getMessage());
            }
    }

    private Image getImage(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("De afbeelding met de identifier: " + id + " is niet gevonden"));
    }

    @Override
    public byte[] downloadPdf(Long id) {
        Recipe foundRecipe = getRecipeById(id);
        String image = "";
        try {
            image = getImage(id).getImage();
        }
        catch (RecordNotFoundException e){
            return generatePdfWithoutImage(foundRecipe).toByteArray();
        }
        return generatePdf(foundRecipe, image).toByteArray();
    }
    private ByteArrayOutputStream generatePdfWithoutImage(Recipe recipe) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

            PdfPTable table = new PdfPTable(1);
            createPdfTextContent(recipe, table, font);

            document.add(table);
            document.close();
        } catch (Exception e) {
            throw new FileUploadException(e.getMessage());
        }
        return baos;
    }

    private ByteArrayOutputStream generatePdf(Recipe recipe, String base64Image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

            PdfPTable table = new PdfPTable(2);
            createPdfTextContent(recipe, table, font);

            PdfPCell imageCell = new PdfPCell();
            imageCell.setBorder(Rectangle.NO_BORDER);

                byte[] imageData = Base64.getDecoder().decode(base64Image);
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(imageData);
            image.scaleToFit(300, 300);

            imageCell.addElement(image);
            table.addCell(imageCell);

            document.add(table);
            document.close();
        } catch (Exception e) {
            throw new FileUploadException(e.getMessage());
        }
        return baos;
    }

    private static void createPdfTextContent(Recipe recipe, PdfPTable table, Font font) {
        table.setWidthPercentage(100);

        PdfPCell textCell = new PdfPCell();
        textCell.setBorder(Rectangle.NO_BORDER);

        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(recipe.name).append("\n\n")
                .append("Allergies: ").append(recipe.allergies).append("\n")
                .append("Nutritional Information: ").append(recipe.nutritionalInformation).append("\n")
                .append("Portion Size: ").append(recipe.portionSize).append("\n")
                .append("Difficulty: ").append(recipe.difficulty).append("\n\n");

        contentBuilder.append("Ingredients:\n\n");
        String[] ingredients = recipe.ingredients.split(",");
        for (String ingredient : ingredients) {
            contentBuilder.append("* ").append(ingredient.trim()).append("\n");
        }

        contentBuilder.append("\nInstructions: \n\n");
        String[] instructions = recipe.instructions.split("\\.");
        for (String instruction : instructions) {
            contentBuilder.append("*  ").append(instruction.trim()).append("\n");
        }

        Paragraph paragraph = new Paragraph(contentBuilder.toString(), font);
        textCell.addElement(paragraph);
        table.addCell(textCell);
    }
}
