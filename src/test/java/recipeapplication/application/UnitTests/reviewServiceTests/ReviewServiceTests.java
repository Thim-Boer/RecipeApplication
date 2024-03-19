//package recipeapplication.application.UnitTests.reviewServiceTests;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import recipeapplication.application.dto.UpdateReviewModel;
//import recipeapplication.application.exceptions.NotificationCollector;
//import recipeapplication.application.models.Recipe;
//import recipeapplication.application.models.Review;
//import recipeapplication.application.models.Role;
//import recipeapplication.application.models.User;
//import recipeapplication.application.repository.ImageRepository;
//import recipeapplication.application.repository.RecipeRepository;
//import recipeapplication.application.repository.ReviewRepository;
//import recipeapplication.application.repository.UserRepository;
//import recipeapplication.application.services.ReviewService;
//import recipeapplication.application.services.TokenService;
//
//@SpringBootTest
//public class ReviewServiceTests {
//	@Mock
//	private ReviewRepository reviewRepository;
//
//    @Mock
//	private RecipeRepository recipeRepository;
//
//	@Mock
//    private UserRepository userRepository;
//
//	@InjectMocks
//	private ReviewService reviewService;
//
//	@Test
//	void TestCheckIfUserIsNotAuthorized() {
//    	var user = SetAuthentication(Role.USER);
//    	when(userRepository.findById(1)).thenReturn(Optional.of(user));
//        when(reviewRepository.findById(1L)).thenReturn(Optional.of(GetReview(4)));
//
//    	assertFalse(reviewService.checkIfUserIsAuthorized(GetReview(4)));
//	}
//
//	@Test
//	void TestCheckIfAdminIsAuthorized() {
//        var user = SetAuthentication(Role.ADMIN);
//
//    	when(userRepository.findById(1)).thenReturn(Optional.of(user));
//    	when(reviewRepository.findById(1L)).thenReturn(Optional.of(GetReview(0)));
//
//    	assertTrue(reviewService.checkIfUserIsAuthorized(GetReview(0)));
//	}
//
//    @Test
//    void InsertReviewSuccess() {
//        SetAuthentication(Role.ADMIN);
//        NotificationCollector collection = new NotificationCollector();
//        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());
//        when(recipeRepository.findById(1L)).thenReturn(Optional.of(new Recipe()));
//
//        var result = reviewService.insertReview(collection, GetReview(0));
//
//        assertFalse(collection.HasErrors());
//        assertTrue(result.getStatusCode().is2xxSuccessful());
//    }
//
//    @Test
//    void InsertReviewFailure() {
//        NotificationCollector collection = new NotificationCollector();
//        when(reviewRepository.findById(1L)).thenReturn(Optional.of(GetReview(0)));
//        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());
//
//        var result = reviewService.insertReview(collection, GetReview(0));
//
//        assertTrue(collection.HasErrors());
//        assertEquals(result, null);
//    }
//
//    @Test
//    void UpdateReviewSuccess() {
//        User user = SetAuthentication(Role.ADMIN);
//
//        NotificationCollector collection = new NotificationCollector();
//        UpdateReviewModel updateModel = new UpdateReviewModel(GetReview(0), GetReview(1));
//
//        when(userRepository.findById(1)).thenReturn(Optional.of(user));
//        when(reviewRepository.findById(1L)).thenReturn(Optional.of(GetReview(0)));
//
//        var result = reviewService.updateReview(collection, updateModel);
//
//        assertTrue(result.getStatusCode().is2xxSuccessful());
//    }
//
//    @Test
//    void UpdateReviewFailure() {
//        User user = SetAuthentication(Role.ADMIN);
//
//        NotificationCollector collection = new NotificationCollector();
//        UpdateReviewModel updateModel = new UpdateReviewModel(GetReview(0), GetReview(1));
//
//        when(userRepository.findById(1)).thenReturn(Optional.of(user));
//        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());
//
//        reviewService.updateReview(collection, updateModel);
//
//        assertTrue(collection.HasErrors());
//    }
//
//    @Test
//    void DeleteReviewSuccess() {
//        User user = SetAuthentication(Role.ADMIN);
//
//        NotificationCollector collection = new NotificationCollector();
//
//        when(userRepository.findById(1)).thenReturn(Optional.of(user));
//        when(reviewRepository.findById(1L)).thenReturn(Optional.of(GetReview(0)));
//
//        reviewService.deleteReview(collection, GetReview(0).id);
//
//        assertFalse(collection.HasErrors());
//    }
//
//    @Test
//    void DeleteReviewFailure() {
//        User user = SetAuthentication(Role.ADMIN);
//
//        NotificationCollector collection = new NotificationCollector();
//
//        when(userRepository.findById(1)).thenReturn(Optional.of(user));
//        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());
//
//        reviewService.deleteReview(collection, GetReview(0).id);
//
//        assertTrue(collection.HasErrors());
//    }
//
//    @Test
//    void GetReviewByIdSuccess() {
//        Review review = GetReview(0);
//        NotificationCollector collection = new NotificationCollector();
//
//        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
//        var result = reviewService.getReviewById(collection, review.id);
//
//        assertFalse(collection.HasErrors());
//        assertTrue(result.getStatusCode().is2xxSuccessful());
//    }
//
//    @Test
//    void GetReviewByIdFailure() {
//        Review review = GetReview(0);
//        NotificationCollector collection = new NotificationCollector();
//
//        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());
//        var result = reviewService.getReviewById(collection, review.id);
//
//        assertTrue(collection.HasErrors());
//        assertTrue(result == null);
//    }
//
//    private User SetAuthentication(Role role) {
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
//	private Review GetReview(int index) {
//        Review[] reviews = new Review[] {
//            new Review(1L, 1, 1L, null, 5),
//            new Review(1L, 1, 2L, null, 3),
//            new Review(3L, 1, 5L, null, 4),
//            new Review(4L, 1, 10L, null, 5),
//            new Review(5L, 1, 12L, null, 3)
//        };
//        return reviews[index];
//	}
//}
