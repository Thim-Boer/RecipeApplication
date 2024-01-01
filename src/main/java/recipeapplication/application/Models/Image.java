package recipeapplication.application.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class Image {
    @Id
    public Long id;
    public int recipeId;
    public byte[] image;   

    public Image(){}

    public Image(Long id, int recipeId, byte[] image) {
        this.id = id;
        this.recipeId = recipeId;
        this.image = image;
    }
}
