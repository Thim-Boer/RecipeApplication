package recipeapplication.application.Models;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class Image {
    @Id
    public Long id;
    public int recipeId;
    public String base64;   

    public Image(Long id, int recipeId, String base64) {
        this.id = id;
        this.recipeId = recipeId;
        this.base64 = base64;
    }
}
