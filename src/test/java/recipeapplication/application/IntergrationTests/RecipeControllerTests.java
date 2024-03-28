package recipeapplication.application.IntergrationTests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import recipeapplication.application.models.Role;
import recipeapplication.application.models.User;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class RecipeControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    Authentication authentication;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setFirstname("test");
        user.setLastname("test");
        user.setRole(Role.USER);
        user.setId(1);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
    }

    @Test
    void GetAllRecipes() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/recipes/recipes"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].instructions").exists());
    }

    @Test
    void AddRecipe() throws Exception {
        String requestJson = """
                            {
                                "name": "Random Recipe456",
                                "instructions": "Mix all ingredients and cook for 30 minutes.",
                                "duration": 45,
                                "difficulty": 3,
                                "portionSize": 4,
                                "nutritionalInformation": "Calories: 300, Protein: 15g, Fat: 10g",
                                "allergies": "Gluten, Dairy",
                                "categoryId": 5,
                                "userId": 1,
                                "supplies": "Mixing bowl, Pan",
                                "ingredients": "Flour, Eggs, Milk, Sugar, Salt"
                            }
                            """;

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/recipes/recipe")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }
}
