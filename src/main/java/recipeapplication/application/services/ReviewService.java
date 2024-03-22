package recipeapplication.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import recipeapplication.application.exceptions.EntityIsNotUniqueException;
import recipeapplication.application.exceptions.IdentifiersDoNotMatchException;
import recipeapplication.application.exceptions.RecordNotFoundException;
import recipeapplication.application.exceptions.UserIsNotAuthorizedException;
import recipeapplication.application.models.Review;
import recipeapplication.application.models.User;
import recipeapplication.application.repository.ReviewRepository;

@Service
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public boolean checkIfUserIsAuthorized(Review review) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            User userDetails = (User) authentication.getPrincipal();
            return review.userId == userDetails.getId() || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
        }
        return false;
    }

    @Override
    public Review insertReview(Review review) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(reviewRepository.findReviewByRecipeIdAndUserId(review.recipeId, user.getId()).isPresent()) {
            throw new EntityIsNotUniqueException("Je hebt al een review achter gelaten op dit recept: " + review.recipeId);
        }

        review.userId = user.getId();
        return reviewRepository.save(review);
    }

    @Override
    public Review updateReview(Review review, Long id) {
        Review foundReview = getReviewById(id);
        if (!checkIfUserIsAuthorized(foundReview)) {
            throw new UserIsNotAuthorizedException("Je hebt geen rechten om deze review aan te passen");
        }

        if (!foundReview.id.equals(id)) {
            throw new IdentifiersDoNotMatchException("De identifiers komen niet overeen");
        }

        review.id = id;
        review.userId = foundReview.userId;

        return reviewRepository.save(review);
    }

    @Override
    public Review deleteReview(Long id) {
        Review foundReview = getReviewById(id);

        if (!checkIfUserIsAuthorized(foundReview)) {
            throw new UserIsNotAuthorizedException("Je hebt geen rechten om deze review te verwijderen");
        }

        reviewRepository.delete(foundReview);
        return foundReview;
    }

    @Override
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("De review met de identifier: " + id + " is niet gevonden"));
    }
}
