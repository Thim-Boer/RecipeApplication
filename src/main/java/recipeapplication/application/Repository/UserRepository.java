package recipeapplication.application.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import recipeapplication.application.Models.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);
  
}