package recipeapplication.application.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import recipeapplication.application.models.User;


public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);
  
}