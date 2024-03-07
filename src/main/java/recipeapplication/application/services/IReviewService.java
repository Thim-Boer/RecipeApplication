package recipeapplication.application.services;

import org.springframework.http.ResponseEntity;

import recipeapplication.application.dto.UpdateReviewModel;
import recipeapplication.application.exceptions.NotificationCollector;
import recipeapplication.application.models.Review;

public interface IReviewService {
    boolean checkIfUserIsAuthorized(Review review);

    ResponseEntity<?> getReviewById(NotificationCollector collection, Long id);

    ResponseEntity<?> insertReview(NotificationCollector collection, Review review);

    ResponseEntity<?> updateReview(NotificationCollector collection, UpdateReviewModel review);

    ResponseEntity<?> deleteReview(NotificationCollector collection, Long id);
}
