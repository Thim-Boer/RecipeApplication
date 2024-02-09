package recipeapplication.application.services;

import org.springframework.http.ResponseEntity;

import recipeapplication.application.models.Review;
import recipeapplication.application.models.UpdateReviewModel;
import recipeapplication.application.models.User;

public interface IReviewService {
    boolean checkIfUserIsAuthorized(User user, Review recipe);

    void insertReview(Review recipe);

    ResponseEntity<?> updateReview(UpdateReviewModel recipe);

    ResponseEntity<?> deleteReview(Review recipe);
}
