package recipeapplication.application.services;

import org.springframework.http.ResponseEntity;

import recipeapplication.application.dto.UpdateReviewModel;
import recipeapplication.application.exceptions.NotificationCollector;
import recipeapplication.application.models.Review;

public interface IReviewService {
    boolean CheckIfUserIsAuthorized(Review review);

    ResponseEntity<?> GetReviewById(NotificationCollector collection, Long id);

    ResponseEntity<?> InsertReview(NotificationCollector collection, Review review);

    ResponseEntity<?> UpdateReview(NotificationCollector collection, UpdateReviewModel review);

    ResponseEntity<?> DeleteReview(NotificationCollector collection, Long id);
}
