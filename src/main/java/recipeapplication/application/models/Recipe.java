package recipeapplication.application.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.OneToOne;

@Entity
public class Recipe {
    @Id
    @GeneratedValue
    public Long id;

    @NotBlank
    @NotNull
    public String name;

    @NotBlank
    public String instructions;

    @Min(value = 1, message = "Duration must be greater than 0")
    public int duration;

    @Min(value = 1, message = "Difficulty must be greater than 0")
    @Max(value = 5, message = "Difficulty must not be greater than 5")
    public int difficulty;

    @Min(value = 1, message = "Portion size must be greater than 0")
    public int portionSize;

    public String nutritionalInformation;

    public String allergies;

    @Min(value = 0, message = "Category ID must be greater than -1")
    public int categoryId;

    public int userId;

    public String supplies;

    public String ingredients;

    @OneToOne(mappedBy = "recipe")
    private Image image;

    public Recipe(){}

    public Recipe(Long id, String name, String instructions, int duration, int difficulty, int portionSize, String nutritionalInformation, String allergies, int categoryId, int userId, String supplies, String ingredients) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.duration = duration;
        this.difficulty = difficulty;
        this.portionSize = portionSize;
        this.nutritionalInformation = nutritionalInformation;
        this.allergies = allergies;
        this.categoryId = categoryId;
        this.userId = userId;
        this.supplies = supplies;
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Recipe recipe = (Recipe) obj;
        return id.equals(recipe.id) && name.equals(recipe.name);
    }
}
