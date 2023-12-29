package recipeapplication.application.Models;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class Category {
    @Id
    public Long id;
    public String name;

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}