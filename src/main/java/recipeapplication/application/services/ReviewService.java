package recipeapplication.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import recipeapplication.application.exceptions.*;
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
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var foundReview = reviewRepository.findById(review.id);

        if (foundReview.isEmpty()) {
            return false;
        }

        var foundUser = userRepository.findById(userDetails.getId());
        var userRole = userDetails.getRole();

        return foundUser.map(user -> user.getId().equals(foundReview.get().userId) || user.getRole().equals(Role.ADMIN))
                .orElse(false);
    }

    @Override
    public Review insertReview(Review review) {
        if (reviewRepository.existsById(review.id)) {
            throw new EntityIsNotUniqueException("Er bestaat al een review met de identifier: " + review.id);
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        review.userId = user.getId();
        return reviewRepository.save(review);
    }

    @Override
    public Review updateReview(Review review, Long id) {
        if (!review.id.equals(id)) {
            throw new IdentifiersDoNotMatchException("De identifiers komen niet overeen");
        }

        Review foundReview = getReviewById(id);

        if (!checkIfUserIsAuthorized(foundReview)) {
            throw new UserIsNotAuthorizedException("Je hebt geen rechten om deze review aan te passen");
        }

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
