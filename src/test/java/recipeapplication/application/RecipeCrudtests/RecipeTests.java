package recipeapplication.application.RecipeCrudtests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import recipeapplication.application.models.Recipe;
import recipeapplication.application.models.UpdateRecipeModel;
import recipeapplication.application.repository.ImageRepository;
import recipeapplication.application.repository.RecipeRepository;
import recipeapplication.application.services.RecipeService;
import recipeapplication.application.services.TokenService;

@SpringBootTest
public class RecipeTests {
	@Mock
	private RecipeRepository recipeRepository;

	@Mock
	private ImageRepository imageRepository;

	@InjectMocks
	private RecipeService recipeService;

	@InjectMocks
	TokenService tokenService;

	@Test
	public void testInsertRecipe() {
		var recipe = GetRecipe(0);
		when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

		recipeService.insertRecipe(recipe);

		Mockito.verify(recipeRepository).save(recipe);
	}

	@Test
	public void testDoIdsNotMatch() {
		Recipe originalRecipe = GetRecipe(0);
		Recipe updatedRecipe = GetRecipe(3);

		UpdateRecipeModel updateModel = new UpdateRecipeModel(originalRecipe, updatedRecipe);

		assertFalse(updateModel.doIdsMatch(), "IDs should not match for different recipes");
	}

	@Test
	public void testDoIdsMatch() {
		Recipe originalRecipe = GetRecipe(0);
		Recipe updatedRecipe = GetRecipe(1);

		UpdateRecipeModel updateModel = new UpdateRecipeModel(originalRecipe, updatedRecipe);

		assertTrue(updateModel.doIdsMatch(), "IDs should match for the same recipe");
	}



	@Test 
	public void testIfUpdateModelGetReturned() {
		UpdateRecipeModel updateModel = new UpdateRecipeModel(GetRecipe(0), GetRecipe(1));
		assertEquals(updateModel.getUpdated(), GetRecipe(1));
	}
	
	@Test 
	public void testIfOriginalRecipeGetReturned() {
		UpdateRecipeModel updateModel = new UpdateRecipeModel(GetRecipe(0), GetRecipe(1));
		assertEquals(updateModel.getOriginal(), GetRecipe(0));

	}

	private Recipe GetRecipe(int index) {
		Recipe[] recipes = new Recipe[] {
				new Recipe(1L, "Pasta Carbonara",
						"Cook pasta. In a separate pan, cook pancetta until crispy. Mix eggs, cheese, and pepper. Combine everything.",
						30, 2, 4,
						"Calories: 500, Protein: 20g, Fat: 25g", "Gluten, Dairy", 4, 2, "Pan, Pot",
						"400g Spaghetti, 150g Pancetta, 2 Eggs, 1 cup Parmesan Cheese, Black Pepper"),

				new Recipe(1L, "Pasta Carbonara v2",
						"Cook pasta. In a separate pan, cook pancetta until crispy. Mix eggs, cheese, and pepper. Combine everything.",
						60, 4, 4,
						"Calories: 500, Protein: 40g, Fat: 25g", "Gluten, Dairy", 4, 2, "Pan, Pot",
						"400g Spaghetti, 150g Pancetta, 2 Eggs, 1 cup Parmesan Cheese, Black Pepper"),

				new Recipe(3L, "Chicken Caesar Salad",
						"Grill chicken. Toss lettuce with dressing. Add chicken and croutons. Mix well.",
						35, 4, 5,
						"Calories: 450, Protein: 30g, Fat: 20g", "Dairy", 2, 1, "Grill, Bowl",
						"250g Chicken Breast, Romaine Lettuce, Caesar Dressing, Croutons"),

				new Recipe(4L, "Homemade Pizza",
						"Prepare pizza dough. Add sauce, cheese, and toppings. Bake in the oven until crust is golden.",
						40, 4, 5,
						"Calories: 600, Protein: 22g, Fat: 30g", "Gluten, Dairy", 4, 3, "Oven, Rolling Pin",
						"Pizza Dough, Tomato Sauce, Mozzarella Cheese, Pepperoni, Mushrooms"),

				new Recipe(5L, "Berry Smoothie",
						"Blend berries, yogurt, and honey until smooth. Pour into a glass and enjoy.",
						10, 1, 2,
						"Calories: 200, Protein: 5g, Fat: 3g", "Dairy", 1, 1, "Blender, Glass",
						"1 cup Mixed Berries, 1 cup Yogurt, 2 tbsp Honey")
		};
		return recipes[index];
	}
}


