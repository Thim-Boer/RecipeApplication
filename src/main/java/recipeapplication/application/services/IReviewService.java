package recipeapplication.application.services;

import org.springframework.http.ResponseEntity;

import recipeapplication.application.dto.UpdateReviewModel;
import recipeapplication.application.models.Review;

public interface IReviewService {
    boolean checkIfUserIsAuthorized(Review review);

    Review getReviewById(Long id);

    Review insertReview(Review review);

    Review updateReview(UpdateReviewModel review);

    Review deleteReview(Long id);
}
