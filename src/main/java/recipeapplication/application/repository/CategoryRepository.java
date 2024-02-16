package recipeapplication.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import recipeapplication.application.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> { }
