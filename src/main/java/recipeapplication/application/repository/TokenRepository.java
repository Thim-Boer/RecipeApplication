package recipeapplication.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import recipeapplication.application.models.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {

  @Query("SELECT t FROM Token t JOIN t.user u WHERE u.id = :id AND (t.expired = false OR t.revoked = false)")
  List<Token> findAllValidTokenByUser(Integer id);

  Optional<Token> findByToken(String token);
}
