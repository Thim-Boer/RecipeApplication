package recipeapplication.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"recipeapplication.application.controllers", "recipeapplication.application.services", "recipeapplication.application.config" })
@EntityScan({"recipeapplication.application.models"})

@EnableJpaRepositories({"recipeapplication.application.repository"})
public class RecipeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecipeApplication.class, args);
    }
}
