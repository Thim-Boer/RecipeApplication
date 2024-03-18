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


import recipeapplication.application.dto.UpdateRecipeModel;
import recipeapplication.application.dto.UploadDto;
import recipeapplication.application.exceptions.EntityIsNotUniqueException;
import recipeapplication.application.exceptions.IdentifiersDoNotMatchException;
import recipeapplication.application.exceptions.RecordNotFoundException;
import recipeapplication.application.exceptions.UserIsNotAuthorizedException;
import recipeapplication.application.models.Image;
import recipeapplication.application.models.Recipe;
import recipeapplication.application.models.User;
import recipeapplication.application.repository.RecipeRepository;
import recipeapplication.application.repository.UserRepository;
import recipeapplication.application.repository.CategoryRepository;
import recipeapplication.application.repository.ImageRepository;

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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDetails = (User) principal;
        var foundUser = userRepository.findById(userDetails.getId());
        var userRole = userDetails.getRole().name();

        if (foundUser.isEmpty()) {
            return false;
        } else {
            if (!foundUser.get().getId().equals(recipe.userId)) {
                return userRole.equals("ADMIN");
            }
            return true;
        }
    }

    @Override
    public Recipe insertRecipe(Recipe recipe) {
        if (!recipeRepository.findByNameContainingIgnoreCase(recipe.name).isEmpty()) {
            throw new EntityIsNotUniqueException("Er bestaat al een recept met deze naam: " + recipe.name);
        }

        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        recipe.userId = user.getId();
        recipeRepository.save(recipe);
        return recipe;
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Optional<Recipe> getRecipeById(Long id) {
        var result = recipeRepository.findById(id);
        if (result.isEmpty()) {
            throw new RecordNotFoundException("Het recept met de identifier: "+id+" is niet gevonden");
        }

        return result;
    }

    @Override
    public List<Recipe> getRecipeByName(String searchterm) {
        var result = recipeRepository.findByNameContainingIgnoreCase(searchterm);
        if (result.isEmpty()) {
            throw new RecordNotFoundException("Het recept met de naam: "+ searchterm + " is niet gevonden");
        }

        return result;
    }

    @Override
    public Recipe updateRecipe(UpdateRecipeModel recipes) {
        var orginalRecipe = recipes.GetOriginal();
        if (!checkIfUserIsAuthorized(orginalRecipe)) {
            throw new UserIsNotAuthorizedException("Je hebt geen rechten om dit recept: " + orginalRecipe + " te updaten");
        }
        if (!recipes.DoIdsMatch()) {
            throw new IdentifiersDoNotMatchException("De opgegeven identifiers komen niet overeen");
        }

        var recipe = recipeRepository.findById(orginalRecipe.id);

        if(recipe.isEmpty()) {
            throw new RecordNotFoundException("Het recept met de identifier: " + orginalRecipe.id + " is niet gevonden");
        }
        recipeRepository.save(recipes.GetUpdated());
        return recipe.get();
    }

    @Override
    public Recipe deleteRecipe(Long id) {
        var foundRecipe = recipeRepository.findById(id);
        if(foundRecipe.isEmpty()) {
            throw new RecordNotFoundException("Het recept met de identifier: "+id+" is niet gevonden");
        }
        if (!checkIfUserIsAuthorized(foundRecipe.get())) {
            throw new UserIsNotAuthorizedException("Je hebt geen rechten om dit recept te verwijderen");
        }
        recipeRepository.delete(foundRecipe.get());
        return foundRecipe.get();
    }

    @Override
    public Image uploadImage(UploadDto file) {
        try {
            byte[] fileBytes = file.image.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(fileBytes);
            Image image = new Image(file.GetId(), file.GetRecipe(), base64Image);
            imageRepository.save(image);
            return image;
        } catch (Exception e) {
            return null;
//            return ResponseEntity.badRequest().body(RecipeErrorCodes.CannotBeUploaded);
        }
    }
    
    @Override
    public Optional<Image> getImage() {
        return imageRepository.findById(1L);
    }
    
    @Override
    public byte[] downloadPdf(Long id) {
        Optional<Recipe> foundRecipeResponse = getRecipeById(id);
        
        if (foundRecipeResponse.isPresent()) {
            var recipe = foundRecipeResponse.get();
            String pdfContent = buildPdfContent(recipe);
            
            ByteArrayOutputStream pdfStream = generatePdf(pdfContent);
            
            if (pdfStream != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", recipe.name + ".pdf");
                
                headers.setContentLength(pdfStream.size());
                
                return pdfStream.toByteArray();
            }
//            else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating PDF");
//            }
//        } else{
////            return ResponseEntity.badRequest().body(RecipeErrorCodes.DataNotFound);
//        }
        }
            return null;
    }

//    @Override
//    public void insertcategories() {
//        Category[] categories = {
//                    new Category("Voorgerechten"),
//                    new Category("Hoofdgerechten"),
//                    new Category("Bijgerechten"),
//                    new Category("Salades"),
//                    new Category("Soepen"),
//                    new Category("Vegetarisch"),
//                    new Category("Veganistisch"),
//                    new Category("Glutenvrij"),
//                    new Category("Desserts"),
//                    new Category("Bakken"),
//                    new Category("Dranken"),
//                    new Category("Smoothies"),
//                    new Category("Internationale keuken"),
//                    new Category("Snacks"),
//                    new Category("Gezonde recepten"),
//                    new Category("Feestelijke gerechten"),
//                    new Category("Snel en makkelijk"),
//                    new Category("Budgetvriendelijk"),
//                    new Category("Seizoensgebonden gerechten"),
//                    new Category("Kinderrecepten")
//                };
//                for (Category category : categories) {
//                    categoryRepository.save(category);
//                }
//    }
    
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
            e.printStackTrace();
            return null;
        }
        return baos;
    }

}
