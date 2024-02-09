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

    public MultipartFile getFile() {
        return image;
    }

    public void setFile(MultipartFile image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}