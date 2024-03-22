package recipeapplication.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import recipeapplication.application.models.Review;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.recipeId = :recipeId AND r.userId = :userId")
    Optional<Review> findReviewByRecipeIdAndUserId(Long recipeId, int userId);
}

