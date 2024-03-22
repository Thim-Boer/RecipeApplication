package recipeapplication.application.models;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @GeneratedValue
    @Id
    public Long id;
    public int userId;
    public Long recipeId;
    public Date date;

    @Min(value = 1, message = "Review must be at least 1")
    @Max(value = 5, message = "Review must not be greater than 5")
    public int review;
}