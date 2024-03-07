package recipeapplication.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import recipeapplication.application.dto.UpdateReviewModel;
import recipeapplication.application.exceptions.NotificationCollector;
import recipeapplication.application.exceptions.RecipeErrorCodes;
import recipeapplication.application.models.Review;
import recipeapplication.application.models.Role;
import recipeapplication.application.models.User;
import recipeapplication.application.repository.RecipeRepository;
import recipeapplication.application.repository.ReviewRepository;
import recipeapplication.application.repository.UserRepository;

@Service
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Autowired
    ReviewService(UserRepository userRepository, ReviewRepository reviewRepository, RecipeRepository recipeRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public boolean checkIfUserIsAuthorized(Review review) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDetails = (User) principal;
        var foundUser = userRepository.findById(userDetails.getId());
        var userRole = userDetails.getRole();
        var foundReview = reviewRepository.findById(review.id);
        if(!foundReview.isPresent()) {
            return false;
        }
        if (!foundUser.isPresent()) {
            return false;
        } else {
            if (!foundUser.get().getId().equals(foundReview.get().userId)) {
                if (userRole.equals(Role.ADMIN)) {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public ResponseEntity<?> insertReview(NotificationCollector collection, Review review) {
        if (reviewRepository.findById(review.id).isPresent()) {
            collection.AddErrorToCollection(RecipeErrorCodes.DataAlreadyExists);
        }
        if (recipeRepository.findById(review.recipeId).isEmpty()) {
            collection.AddErrorToCollection(RecipeErrorCodes.DataNotFound);
        }
        if (collection.HasErrors()) {
            return null;
        }
        var user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        review.userId = user.getId();
        reviewRepository.save(review);
        return ResponseEntity.ok().body("De review is aangemaakt");
    }

    @Override
    public ResponseEntity<?> updateReview(NotificationCollector collection, UpdateReviewModel reviews) {
        var orginalReview = reviews.GetOriginal();
        if (!reviews.DoIdsMatch()) {
            collection.AddErrorToCollection(RecipeErrorCodes.IdsDonotMatch);
        }
        var review = reviewRepository.findById(orginalReview.id);
        
        if (!review.isPresent()) {
            collection.AddErrorToCollection(RecipeErrorCodes.DataNotFound);
            return null;
        }
        if(!checkIfUserIsAuthorized(review.get())) {
            collection.AddErrorToCollection(RecipeErrorCodes.NotAuthorized);
        }
        if (collection.HasErrors()) {
            return null;
        }
        reviewRepository.save(reviews.GetUpdated());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> deleteReview(NotificationCollector collection, Long id) {
        var foundReview = reviewRepository.findById(id);
        if(!foundReview.isPresent()) {
            collection.AddErrorToCollection(RecipeErrorCodes.DataNotFound);
            return null;
        }

        if(!checkIfUserIsAuthorized(foundReview.get())) {
            collection.AddErrorToCollection(RecipeErrorCodes.NotAuthorized);
        }
        if(collection.HasErrors()) {
            return null;
        }
        reviewRepository.delete(foundReview.get());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> getReviewById(NotificationCollector collection, Long id) {
        var foundReview = reviewRepository.findById(id);
        if(!foundReview.isPresent()) {
            collection.AddErrorToCollection(RecipeErrorCodes.DataNotFound);
            return null;
        }
        
        return ResponseEntity.ok().body(foundReview.get());
    }
}
