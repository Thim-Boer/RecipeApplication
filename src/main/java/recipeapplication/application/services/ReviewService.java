package recipeapplication.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import recipeapplication.application.dto.UpdateReviewModel;
import recipeapplication.application.exceptions.EntityIsNotUniqueException;
import recipeapplication.application.exceptions.IdentifiersDoNotMatchException;
import recipeapplication.application.exceptions.RecordNotFoundException;
import recipeapplication.application.exceptions.UserIsNotAuthorizedException;
import recipeapplication.application.models.Review;
import recipeapplication.application.models.Role;
import recipeapplication.application.models.User;
import recipeapplication.application.repository.ReviewRepository;
import recipeapplication.application.repository.UserRepository;

@Service
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Autowired
    ReviewService(UserRepository userRepository, ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean checkIfUserIsAuthorized(Review review) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDetails = (User) principal;
        var foundUser = userRepository.findById(userDetails.getId());
        var userRole = userDetails.getRole();
        var foundReview = reviewRepository.findById(review.id);
        if(foundReview.isEmpty()) {
            return false;
        }
        if (foundUser.isEmpty()) {
            return false;
        } else {
            if (!foundUser.get().getId().equals(foundReview.get().userId)) {
                return userRole.equals(Role.ADMIN);
            }
            return true;
        }
    }

    @Override
    public Review insertReview(Review review) {

        if (reviewRepository.findById(review.id).isPresent()) {
            throw new EntityIsNotUniqueException("Er bestaat al een review met de identifier: " + review.id);
        }

        var user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        review.userId = user.getId();
        reviewRepository.save(review);
        return review;
    }

    @Override
    public Review updateReview(UpdateReviewModel reviews) {
        var orginalReview = reviews.GetOriginal();
        if (!reviews.DoIdsMatch()) {
            throw new IdentifiersDoNotMatchException("De identifier komen niet overen");
        }

        var review = reviewRepository.findById(orginalReview.id);
        
        if (review.isEmpty()) {
            throw new RecordNotFoundException("De review met de identifier: " + orginalReview.id + " is niet gevonden");
        }

        if(!checkIfUserIsAuthorized(review.get())) {
            throw new UserIsNotAuthorizedException("Je hebt geen rechten om deze review aan te passen");
        }

        reviewRepository.save(reviews.GetUpdated());
        return reviews.GetUpdated();
    }

    @Override
    public Review deleteReview(Long id) {
        var foundReview = reviewRepository.findById(id);
        if(foundReview.isEmpty()) {
            throw new RecordNotFoundException("De review met de identifier: " + id + " is niet gevonden");
        }

        if(!checkIfUserIsAuthorized(foundReview.get())) {
            throw new UserIsNotAuthorizedException("Je hebt geen rechten om deze review te verwijderen");
        }

        reviewRepository.delete(foundReview.get());
        return foundReview.get();
    }

    @Override
    public Review getReviewById(Long id) {
        var foundReview = reviewRepository.findById(id);
        if(foundReview.isEmpty()) {
          throw new RecordNotFoundException("De review met de identifier: " + id + " is niet gevonden");
        }

        return foundReview.get();
    }
}
