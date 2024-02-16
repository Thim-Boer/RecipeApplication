package recipeapplication.application.models;

import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    public Long id;
    public int userId;
    public Long recipeId;   
    public Date date;
    public int review;   
}
