package recipeapplication.application.Models;

public class UpdateRecipeModel {
    Recipe orginalRecipe;
    Recipe updatedRecipe;

    public UpdateRecipeModel(Recipe orginalRecipe, Recipe updatedRecipe) {
        this.orginalRecipe = orginalRecipe;
        this.updatedRecipe = updatedRecipe;
    }

    public Recipe getOriginal() {
        return this.orginalRecipe;
    }

    public Recipe getUpdated() {
        return this.updatedRecipe;
    }

    public boolean doIdsMatch() {
        if(orginalRecipe.id != updatedRecipe.id) {
            return false;
        }
        return true;
    }
}