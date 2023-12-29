package recipeapplication.application.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import recipeapplication.application.Models.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
