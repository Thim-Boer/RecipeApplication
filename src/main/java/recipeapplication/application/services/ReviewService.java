package recipeapplication.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import recipeapplication.application.models.Review;
import recipeapplication.application.models.UpdateReviewModel;
import recipeapplication.application.models.User;
import recipeapplication.application.repository.ReviewRepository;

@Service
public class ReviewService implements IReviewService {
    private ReviewRepository reviewRepository;
    
    @Autowired
    ReviewService(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    @Override
    public boolean checkIfUserIsAuthorized(User user, Review review) {
        var userId = user.getId();
        var userRole = user.getRole().name();
        if(!userId.equals(review.userId)) {
            if(userRole.equals("ADMIN")) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void insertReview(Review review) {
        reviewRepository.save(review);
    }

    @Override
    public ResponseEntity<?> updateReview(UpdateReviewModel reviews) {
        var orginalReview = reviews.getOriginal();
        if(!reviews.doIdsMatch()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        var review = reviewRepository.findById(orginalReview.id);
        if(!review.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        reviewRepository.save(reviews.getUpdated());
        return ResponseEntity.ok().build();
    }
    
    @Override
    public ResponseEntity<?> deleteReview(Review review) {
        var foundReview = reviewRepository.findById(review.id);
        if(!foundReview.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        reviewRepository.delete(review);
        return ResponseEntity.ok().build();
    }
}
