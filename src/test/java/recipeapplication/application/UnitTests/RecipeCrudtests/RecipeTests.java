//package recipeapplication.application.UnitTests.RecipeCrudtests;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.multipart.MultipartFile;
//
//import recipeapplication.application.exceptions.NotificationCollector;
//import recipeapplication.application.exceptions.RecipeErrorCodes;
//import recipeapplication.application.dto.UpdateRecipeModel;
//import recipeapplication.application.dto.UploadDto;
//import recipeapplication.application.models.Image;
//import recipeapplication.application.models.Recipe;
//import recipeapplication.application.models.Role;
//import recipeapplication.application.models.User;
//import recipeapplication.application.repository.ImageRepository;
//import recipeapplication.application.repository.RecipeRepository;
//import recipeapplication.application.repository.UserRepository;
//import recipeapplication.application.services.RecipeService;
//import recipeapplication.application.services.TokenService;
//
//@SpringBootTest
//public class RecipeTests {
//	@Mock
//	private RecipeRepository recipeRepository;
//
//	@Mock
//	private UserRepository userRepository;
//
//	@Mock
//	private ImageRepository imageRepository;
//
//	@InjectMocks
//	private RecipeService recipeService;
//
//	@InjectMocks
//	TokenService tokenService;
//
//	@Test
//	void TestCheckIfUserIsAuthorized() {
//		var user = SetAuthentication(Role.USER);
//
//		when(userRepository.findById(1)).thenReturn(Optional.of(user));
//
//		assertFalse(recipeService.checkIfUserIsAuthorized(GetRecipe(0)));
//	}
//
//	@Test
//	void TestCheckIfAdminIsAuthorized() {
//		var user = SetAuthentication(Role.ADMIN);
//		when(userRepository.findById(1)).thenReturn(Optional.of(user));
//
//		assertTrue(recipeService.checkIfUserIsAuthorized(GetRecipe(0)));
//	}
//
//	@Test
//	void GetAllRecipes() {
//		ArrayList<Recipe> list = new ArrayList<>();
//		list.add(GetRecipe(0));
//		list.add(GetRecipe(1));
//		list.add(GetRecipe(2));
//		list.add(GetRecipe(3));
//
//		when(recipeRepository.findAll()).thenReturn(list);
//		var result = recipeRepository.findAll();
//
//		assertEquals(result, list);
//	}
//
//	@Test
//	void GetRecipeById() {
//		when(recipeRepository.findById(1L)).thenReturn(Optional.of(GetRecipe(0)));
//
//		var result = recipeRepository.findById(1L);
//
//		assertEquals(result.get(), GetRecipe(0));
//	}
//
//	@Test
//	void TestInsertRecipe() {
//		SetAuthentication(Role.ADMIN);
//		var recipe = GetRecipe(0);
//		when(recipeRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(new ArrayList<>());
//		when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);
//		recipeService.insertRecipe(recipe);
//
//		Mockito.verify(recipeRepository).save(recipe);
//	}
//
//	@Test
//	void TestInsertDuplicateRecipe() {
//		Recipe recipe = GetRecipe(0);
//		ArrayList<Recipe> list = new ArrayList<>();
//		list.add(recipe);
//
//		when(recipeRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(list);
//		ResponseEntity<?> response = recipeService.insertRecipe(recipe);
//
//		assertEquals(response, ResponseEntity.badRequest().body(RecipeErrorCodes.DataAlreadyExists));
//	}
//
//	@Test
//	void GetRecipeByName() {
//		List<Recipe> list = new ArrayList<>();
//		list.add(GetRecipe(0));
//
//		when(recipeRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(list);
//		var result = recipeRepository.findByNameContainingIgnoreCase(anyString());
//
//		assertEquals(result, list);
//	}
//
//	@Test
//	void TestUploadImageSuccess() {
//		UploadDto file = new UploadDto(null, 1L, GetRecipe(0));
//		file.SetId(1L);
//		file.SetRecipe(GetRecipe(0));
//		byte[] fileContent = "".getBytes();
//		MultipartFile multipartFile = new MockMultipartFile("file", "filename.txt", "text/plain", fileContent);
//		file.SetFile(multipartFile);
//
//		ResponseEntity<?> responseEntity = recipeService.uploadImage(file);
//
//		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//		verify(imageRepository, times(1)).save(any());
//	}
//
//	@Test
//	void TestUploadImageFailure() {
//		UploadDto file = new UploadDto(null, null, null);
//
//		ResponseEntity<?> responseEntity = recipeService.uploadImage(file);
//
//		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//		assertEquals(RecipeErrorCodes.CannotBeUploaded, responseEntity.getBody());
//	}
//
//	@Test
//	void UpdateRecipeSuccess() {
//        User user = SetAuthentication(Role.ADMIN);
//		UpdateRecipeModel updateRecipeModel = new UpdateRecipeModel(GetRecipe(0), GetRecipe(1));
//
//        NotificationCollector collection = new NotificationCollector();
//
//        when(userRepository.findById(1)).thenReturn(Optional.of(user));
//        when(recipeRepository.findById(1L)).thenReturn(Optional.of(GetRecipe(0)));
//
//        recipeService.updateRecipe(collection, updateRecipeModel);
//
//        assertFalse(collection.HasErrors());
//    }
//
//	@Test
//	void UpdateRecipeFailure() {
//        User user = SetAuthentication(Role.ADMIN);
//		UpdateRecipeModel updateRecipeModel = new UpdateRecipeModel(GetRecipe(0), GetRecipe(1));
//
//        NotificationCollector collection = new NotificationCollector();
//
//        when(userRepository.findById(1)).thenReturn(Optional.of(user));
//        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());
//
//        recipeService.updateRecipe(collection, updateRecipeModel);
//
//        assertTrue(collection.HasErrors());
//    }
//
//	@Test
//	void DeleteRecipeSuccess() {
//        User user = SetAuthentication(Role.ADMIN);
//
//        NotificationCollector collection = new NotificationCollector();
//
//        when(userRepository.findById(1)).thenReturn(Optional.of(user));
//        when(recipeRepository.findById(1L)).thenReturn(Optional.of(GetRecipe(0)));
//
//        recipeService.deleteRecipe(collection, GetRecipe(0).id);
//
//        assertFalse(collection.HasErrors());
//    }
//
//	@Test
//	void DeleteRecipeFailure() {
//        User user = SetAuthentication(Role.ADMIN);
//
//        NotificationCollector collection = new NotificationCollector();
//
//        when(userRepository.findById(1)).thenReturn(Optional.of(user));
//        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());
//
//        recipeService.deleteRecipe(collection, GetRecipe(0).id);
//
//        assertTrue(collection.HasErrors());
//    }
//
//	@Test
//	void TestGetImage() {
//		Image mockImage = new Image(1L, GetRecipe(0), "base64Image");
//
//		when(imageRepository.findById(1L)).thenReturn(Optional.of(mockImage));
//		Optional<Image> image = recipeService.getImage();
//
//		assertEquals(mockImage, image.orElse(null));
//	}
//
//	@Test
//	void TestDownloadPdfSuccess() {
//		NotificationCollector collection = new NotificationCollector();
//
//		when(recipeRepository.findById(1l)).thenReturn(Optional.of(GetRecipe(0)));
//		when(recipeService.getRecipeById(collection, 1L)).thenReturn(Optional.of(GetRecipe(0)));
//		var result = recipeService.downloadPdf(1L);
//
//		assertEquals(result.getStatusCode(), HttpStatus.OK);
//		assertFalse(collection.HasErrors());
//	}
//
//	@Test
//	void TestDownloadPdfFailure() {
//		NotificationCollector collection = new NotificationCollector();
//		when(recipeService.getRecipeById(collection, 1L)).thenReturn(Optional.empty());
//		recipeService.downloadPdf(1L);
//
//		assertTrue(collection.HasErrors());
//	}
//
//	private User SetAuthentication(Role role) {
//        SecurityContext securityContext = mock(SecurityContext.class);
//        SecurityContextHolder.setContext(securityContext);
//
//        User user = new User();
//        user.setEmail("test@test.com");
//        user.setFirstname("test");
//        user.setLastname("test");
//        user.setRole(role);
//        user.setId(1);
//
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getPrincipal()).thenReturn(user);
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        return user;
//    }
//
//	private Recipe GetRecipe(int index) {
//		Recipe[] recipes = new Recipe[] {
//				new Recipe(1L, "Pasta Carbonara",
//						"Cook pasta. In a separate pan, cook pancetta until crispy. Mix eggs, cheese, and pepper. Combine everything.",
//						30, 2, 4,
//						"Calories: 500, Protein: 20g, Fat: 25g", "Gluten, Dairy", 4, 2, "Pan, Pot",
//						"400g Spaghetti, 150g Pancetta, 2 Eggs, 1 cup Parmesan Cheese, Black Pepper"),
//
//				new Recipe(1L, "Pasta Carbonara v2",
//						"Cook pasta. In a separate pan, cook pancetta until crispy. Mix eggs, cheese, and pepper. Combine everything.",
//						60, 4, 4,
//						"Calories: 500, Protein: 40g, Fat: 25g", "Gluten, Dairy", 4, 2, "Pan, Pot",
//						"400g Spaghetti, 150g Pancetta, 2 Eggs, 1 cup Parmesan Cheese, Black Pepper"),
//
//				new Recipe(3L, "Chicken Caesar Salad",
//						"Grill chicken. Toss lettuce with dressing. Add chicken and croutons. Mix well.",
//						35, 4, 5,
//						"Calories: 450, Protein: 30g, Fat: 20g", "Dairy", 2, 1, "Grill, Bowl",
//						"250g Chicken Breast, Romaine Lettuce, Caesar Dressing, Croutons"),
//
//				new Recipe(4L, "Homemade Pizza",
//						"Prepare pizza dough. Add sauce, cheese, and toppings. Bake in the oven until crust is golden.",
//						40, 4, 5,
//						"Calories: 600, Protein: 22g, Fat: 30g", "Gluten, Dairy", 4, 3, "Oven, Rolling Pin",
//						"Pizza Dough, Tomato Sauce, Mozzarella Cheese, Pepperoni, Mushrooms"),
//
//				new Recipe(5L, "Berry Smoothie",
//						"Blend berries, yogurt, and honey until smooth. Pour into a glass and enjoy.",
//						10, 1, 2,
//						"Calories: 200, Protein: 5g, Fat: 3g", "Dairy", 1, 1, "Blender, Glass",
//						"1 cup Mixed Berries, 1 cup Yogurt, 2 tbsp Honey")
//		};
//		return recipes[index];
//	}
//}
