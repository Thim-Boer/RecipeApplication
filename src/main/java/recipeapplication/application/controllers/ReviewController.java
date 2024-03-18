package recipeapplication.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import recipeapplication.application.dto.UpdateReviewModel;
import recipeapplication.application.models.Review;
import recipeapplication.application.services.IReviewService;

import java.net.URI;

@Controller
@RequestMapping("/api/review")
@RestController
public class ReviewController {
private final IReviewService reviewService;
    
    @Autowired
    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/review/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable Long id) {
        var result = reviewService.getReviewById(id);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/review")
    public ResponseEntity<?> addReviewToReview(@RequestBody Review review) {
        var result = reviewService.insertReview(review);
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/" + result.id).toUriString());

        return ResponseEntity.created(uri).body(result);
    }
    
    @PutMapping("/review")
    public ResponseEntity<?> changeReview(@RequestBody UpdateReviewModel updateModel) {
        var result = reviewService.updateReview(updateModel);

        return ResponseEntity.ok().body(result);
    }
    
    @DeleteMapping("/review/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        var result = reviewService.deleteReview(id);

        return ResponseEntity.ok().body(result);
    }
}
