package recipeapplication.application.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import recipeapplication.application.exceptions.EntityIsNotUniqueException;
import recipeapplication.application.exceptions.RecordNotFoundException;
import recipeapplication.application.exceptions.UserIsNotAuthorizedException;
import recipeapplication.application.models.Review;
import recipeapplication.application.models.Role;
import recipeapplication.application.models.User;
import recipeapplication.application.repository.ReviewRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ReviewServiceTest {

    @Mock
	private ReviewRepository reviewRepository;

    @InjectMocks
	private ReviewService reviewService;
    @Test
    @DisplayName("Check if you are authorized as an user that is not your review")
    void checkIfUserIsAuthorized() {
        setAuthentication(Role.USER);

        boolean result = reviewService.checkIfUserIsAuthorized(getReview(0));

        assertFalse(result);
    }
    @Test
    @DisplayName("Check if you are authorized as an admin that is not your review")
    void checkIfAdminIsAuthorized() {
        setAuthentication(Role.ADMIN);

        boolean result = reviewService.checkIfUserIsAuthorized(getReview(0));

        assertTrue(result);
    }
    @Test
    @DisplayName("Check if your authorized if your not authenticated")
    void checkIfYouAreAuthorizedWhenNotLoggedIn() {
        boolean result = reviewService.checkIfUserIsAuthorized(getReview(0));

        assertFalse(result);
    }

    @Test
    @DisplayName("Add a review")
    void insertReview() {
        setAuthentication(Role.USER);
        Review review = getReview(0);
        when(reviewRepository.findReviewByRecipeIdAndUserId(1L, 1)).thenReturn(Optional.empty());
        when(reviewRepository.save(review)).thenReturn(review);

        Review result = reviewService.insertReview(review);

        assertNotNull(result);
        assertEquals(result, review);
    }
    @Test
    @DisplayName("Add a duplicate review")
    void insertDuplicateReview() {
        setAuthentication(Role.USER);
        Review review = getReview(0);
        when(reviewRepository.findReviewByRecipeIdAndUserId(1L, 1)).thenReturn(Optional.of(review));
        when(reviewRepository.save(review)).thenReturn(review);

        String exceptionMessage = "Je hebt al een review achter gelaten op dit recept: " + review.recipeId;

        EntityIsNotUniqueException exception = assertThrows(EntityIsNotUniqueException.class, () -> {
            reviewService.insertReview(review);
        });

        assertNotNull(exception);
        assertEquals(exception.getMessage(), exceptionMessage);
    }

    @Test
    @DisplayName("Updating a review that is yours")
    void updateReview() {
        setAuthentication(Role.USER);
        Review review = getReview(4);
        when(reviewRepository.findById(review.id)).thenReturn(Optional.of(review));
        when(reviewRepository.save(review)).thenReturn(review);

        Review result = reviewService.updateReview(review, 5L);

        assertNotNull(result);
        assertEquals(result, review);
    }

    @Test
    @DisplayName("Updating a review that isn't yours")
    void updateReviewWithWrongUser() {
            setAuthentication(Role.USER);
            Review review = getReview(2);
            when(reviewRepository.findById(review.id)).thenReturn(Optional.of(review));
            when(reviewRepository.save(review)).thenReturn(review);

            String exceptionMessage = "Je hebt geen rechten om deze review aan te passen";

            UserIsNotAuthorizedException exception = assertThrows(UserIsNotAuthorizedException.class, () -> {
                reviewService.updateReview(review, review.id);
            });

            assertEquals(exception.getMessage(), exceptionMessage);
    }

    @Test
    @DisplayName("Delete a review as an admin")
    void deleteReview() {
        setAuthentication(Role.ADMIN);
        Review review = getReview(1);
        when(reviewRepository.findById(review.id)).thenReturn(Optional.of(review));

        Review result = reviewService.deleteReview(1L);

        assertNotNull(result);
        assertEquals(result, review);
    }
    @Test
    @DisplayName("Delete a review with a different user than the one that created the review")
    void deleteReviewWithWrongUser() {
        setAuthentication(Role.USER);
        Review review = getReview(3);
        when(reviewRepository.findById(review.id)).thenReturn(Optional.of(review));

        String exceptionMessage = "Je hebt geen rechten om deze review te verwijderen";

        UserIsNotAuthorizedException exception = assertThrows(UserIsNotAuthorizedException.class, () -> {
            reviewService.deleteReview(review.id);
        });

        assertEquals(exception.getMessage(), exceptionMessage);
    }

    @Test
    @DisplayName("Get a specific review by id")
    void getReviewById() {
        setAuthentication(Role.USER);
        Review review = getReview(3);
        when(reviewRepository.findById(review.id)).thenReturn(Optional.of(review));

        Review result = reviewService.getReviewById(review.id);

        assertNotNull(result);
        assertEquals(result, review);
    }
    @Test
    @DisplayName("Get a specific review by id that is not present")
    void getReviewThatIsNotPresentById() {
        setAuthentication(Role.USER);
        Review review = getReview(3);
        when(reviewRepository.findById(review.id)).thenReturn(Optional.empty());

        String exceptionMessage = "De review met de identifier: " + review.id + " is niet gevonden";

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            reviewService.getReviewById(review.id);
        });

        assertNotNull(exception);
        assertEquals(exception.getMessage(), exceptionMessage);
    }

    private void setAuthentication(Role role) {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setEmail("test@test.com");
        user.setFirstname("test");
        user.setLastname("test");
        user.setRole(role);
        user.setId(1);

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role.toString()));

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getAuthorities()).thenAnswer((Answer<List<GrantedAuthority>>) invocation -> authorities);

        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    private Review getReview(int index) {
        Review[] reviews = new Review[] {
            new Review(1L, 2, 1L, null, 5),
            new Review(1L, 4, 2L, null, 3),
            new Review(3L, 5, 5L, null, 4),
            new Review(4L, 2, 10L, null, 5),
            new Review(5L, 1, 12L, null, 3)
        };
        return reviews[index];
    }
}