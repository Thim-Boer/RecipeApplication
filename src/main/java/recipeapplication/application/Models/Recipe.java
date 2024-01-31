package recipeapplication.application.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Recipe {
    @Id
    @NotNull
    public Long id;

    @NotBlank
    @NotNull
    public String name;

    @NotBlank
    public String instructions;

    @Min(value = 1, message = "Duur moet groter zijn dan 0")
    public int duration;

    @Min(value = 1, message = "Moeilijkheidsgraad moet groter zijn dan 0")
    @Max(value = 5, message = "Moeilijkheidsgraad mag niet groter zijn dan 5")
    public int difficulty;

    @Min(value = 1, message = "Portiegrootte moet groter zijn dan 0")
    public int portionSize;

    public String nutritionalInformation;

    public String allergies;

    @Min(value = 0, message = "Categorie ID moet groter zijn dan -1")
    public int categoryId;

    @Min(value = 1, message = "Gebruikers-ID moet groter zijn dan 0")
    public int userId;

    public String supplies;

    public String ingredients;

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
        return id == recipe.id && name.equals(recipe.name);
    }
}