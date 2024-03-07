package recipeapplication.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import recipeapplication.application.dto.UpdateReviewModel;
import recipeapplication.application.exceptions.NotificationCollector;
import recipeapplication.application.models.Review;
import recipeapplication.application.services.IReviewService;

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
        NotificationCollector collection = new NotificationCollector();
        var result = reviewService.getReviewById(collection, id);
        if(collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.ReturnErrors());
        } else {
            return result;
        }
    }

    @PostMapping("/review")
    public ResponseEntity<?> addReviewToReview(@RequestBody Review review) {
        NotificationCollector collection = new NotificationCollector();
        var result = reviewService.insertReview(collection, review);
        if(collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.ReturnErrors());
        } 
        return result;
    }
    
    @PutMapping("/review")
    public ResponseEntity<?> changeReview(@RequestBody UpdateReviewModel updateModel) {
        NotificationCollector collection = new NotificationCollector();
        var result = reviewService.updateReview(collection, updateModel);
        if(collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.ReturnErrors());
        } 
        return result;
    }
    
    @DeleteMapping("/review/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        NotificationCollector collection = new NotificationCollector();
        var result = reviewService.deleteReview(collection, id);
        if(collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.ReturnErrors());
        } 
        return result;
    }
}
