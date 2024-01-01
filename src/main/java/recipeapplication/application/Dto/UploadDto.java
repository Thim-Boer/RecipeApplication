package recipeapplication.application.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class UploadDto {
    @Id
    public Long id;
    public int recipeId;
    public MultipartFile image;

    public UploadDto(MultipartFile image, Long id, int recipeId) {
        this.image = image;
        this.id = id;
        this.recipeId = recipeId;
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

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}