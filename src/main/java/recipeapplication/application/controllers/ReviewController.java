package recipeapplication.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import recipeapplication.application.exceptions.ValidationException;
import recipeapplication.application.models.Review;
import recipeapplication.application.services.IReviewService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        var result = reviewService.getReviewById(id);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/review")
    public ResponseEntity<Review> addReviewToReview(@RequestBody @Validated Review review, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
            throw new ValidationException(errors);
        }

        var result = reviewService.insertReview(review);
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/" + result.id).toUriString());

        return ResponseEntity.created(uri).body(result);
    }
    
    @PutMapping("/review/{id}")
    public ResponseEntity<Review> changeReview(@PathVariable Long id, @RequestBody Review updatedReview) {
        var result = reviewService.updateReview(updatedReview, id);

        return ResponseEntity.ok().body(result);
    }
    
    @DeleteMapping("/review/{id}")
    public ResponseEntity<Review> deleteReview(@PathVariable Long id) {
        var result = reviewService.deleteReview(id);

        return ResponseEntity.ok().body(result);
    }
}
