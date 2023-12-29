package recipeapplication.application.Models;

import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class Review {
    @Id
    public Long id;
    public int userId;
    public int recipeId;   
    public Date date;
    public int review;   

    public Review(Long id, int userId, int recipeId, Date date, int review) {
        this.id = id;
        this.userId = userId;
        this.recipeId = recipeId;
        this.date = date;
        this.review = review;
    }
}
