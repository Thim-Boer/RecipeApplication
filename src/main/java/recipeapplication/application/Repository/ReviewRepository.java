package recipeapplication.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import recipeapplication.application.models.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}

