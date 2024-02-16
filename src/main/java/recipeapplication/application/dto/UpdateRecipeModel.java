package recipeapplication.application.dto;

import recipeapplication.application.models.Recipe;

public class UpdateRecipeModel {
    Recipe orginalRecipe;
    Recipe updatedRecipe;

    public UpdateRecipeModel(Recipe orginalRecipe, Recipe updatedRecipe) {
        this.orginalRecipe = orginalRecipe;
        this.updatedRecipe = updatedRecipe;
    }

    public Recipe GetOriginal() {
        return this.orginalRecipe;
    }

    public Recipe GetUpdated() {
        return this.updatedRecipe;
    }

    public boolean DoIdsMatch() {
        if(orginalRecipe.id != updatedRecipe.id) {
            return false;
        }
        return true;
    }
}