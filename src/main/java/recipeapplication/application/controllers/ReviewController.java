package recipeapplication.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
private IReviewService reviewService;
    
    @Autowired
    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/getReviewById/{id}")
    public ResponseEntity<?> GetReviewById(@PathVariable Long id) {
        NotificationCollector collection = new NotificationCollector();
        var result = reviewService.GetReviewById(collection, id);
        if(collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.ReturnErrors());
        } else {
            return result;
        }
    }

    @PostMapping("/addReview")
    public ResponseEntity<?> AddReviewToReview(@RequestBody Review review) {
        NotificationCollector collection = new NotificationCollector();
        var result = reviewService.InsertReview(collection, review);
        if(collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.ReturnErrors());
        } 
        return result;
    }
    
    @PutMapping("/updateReview")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<?> ChangeReview(@RequestBody UpdateReviewModel updateModel) {
        NotificationCollector collection = new NotificationCollector();
        var result = reviewService.UpdateReview(collection, updateModel);
        if(collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.ReturnErrors());
        } 
        return result;
    }
    
    @DeleteMapping("/deleteReview/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<?> DeleteReview(@PathVariable Long id) {
        NotificationCollector collection = new NotificationCollector();
        var result = reviewService.DeleteReview(collection, id);
        if(collection.HasErrors()) {
            return ResponseEntity.badRequest().body(collection.ReturnErrors());
        } 
        return result;
    }
}
