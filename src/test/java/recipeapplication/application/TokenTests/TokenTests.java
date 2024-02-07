package recipeapplication.application.TokenTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import recipeapplication.application.models.Role;
import recipeapplication.application.models.User;
import recipeapplication.application.services.TokenService;

@SpringBootTest
class TokenTests {
    @Test
    public void testGenerateTokenForUser() {
        TokenService tokenService = mock(TokenService.class);

        User user = new User();
        user.setFirstname("user");
        user.setLastname("user");
        user.setPassword("password");
        user.setRole(Role.USER);

        String mockToken = "mockedToken";
        when(tokenService.generateToken(user)).thenReturn(mockToken);

        String generatedToken = tokenService.generateToken(user);

        assertEquals(mockToken, generatedToken, "Het gegenereerde token komt niet overeen met het verwachte token.");
    }
}