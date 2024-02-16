package recipeapplication.application.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import recipeapplication.application.models.Recipe;

@Entity
public class UploadDto {
    @Id
    public Long id;
    public Recipe recipe;
    public MultipartFile image;

    public UploadDto(MultipartFile image, Long id, Recipe recipe) {
        this.image = image;
        this.id = id;
        this.recipe = recipe;
    }

    public MultipartFile GetFile() {
        return image;
    }

    public void SetFile(MultipartFile image) {
        this.image = image;
    }

    public Long GetId() {
        return id;
    }

    public void SetId(Long id) {
        this.id = id;
    }

    public Recipe GetRecipe() {
        return recipe;
    }

    public void SetRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}