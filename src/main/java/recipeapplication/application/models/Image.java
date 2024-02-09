package recipeapplication.application.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Image {
    @Id
    public Long id;

    @OneToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Lob
    @Column(columnDefinition = "TEXT")
    public String image;   

    public Image(){}

    public Image(Long id, Recipe recipe, String image) {
        this.id = id;
        this.recipe = recipe;
        this.image = image;
    }
}
