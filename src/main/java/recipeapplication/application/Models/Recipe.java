package recipeapplication.application.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Recipe {
    @Id
    public Long id;
    public String name;
    public String instructions;
    public int duration;
    public int difficulty;
    public int portionSize;
    public String nutritionalInformation;
    public String allergies;
    public int categoryId;
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