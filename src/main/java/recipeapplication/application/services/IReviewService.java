package recipeapplication.application.services;

import recipeapplication.application.models.Review;

public interface IReviewService {
    boolean checkIfUserIsAuthorized(Review review);

    Review getReviewById(Long id);

    Review insertReview(Review review);

    Review updateReview(Review updatedReview, Long id);

    Review deleteReview(Long id);
}
