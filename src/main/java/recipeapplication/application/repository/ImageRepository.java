package recipeapplication.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import recipeapplication.application.models.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
