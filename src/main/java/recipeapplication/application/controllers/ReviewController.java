package recipeapplication.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import recipeapplication.application.models.Review;
import recipeapplication.application.models.UpdateReviewModel;
import recipeapplication.application.models.User;
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
    @PostMapping("/addReview")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<String> addReviewToReview(@RequestBody Review review) {
        return ResponseEntity.ok("This review has been added");
    }
    
    @PutMapping("/updateReview")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<?> changeReview(@RequestBody UpdateReviewModel updateModel) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDetails = (User) principal;
        if(!reviewService.checkIfUserIsAuthorized(userDetails, updateModel.getOriginal())){
            return ResponseEntity.badRequest().body("You are not allowed to do this action");
        }
        return reviewService.updateReview(updateModel);
    }
    
    @DeleteMapping("/deleteReview")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<?> deleteReview(@RequestBody Review review) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDetails = (User) principal;
        if(!reviewService.checkIfUserIsAuthorized(userDetails, review)){
            return ResponseEntity.badRequest().body("You are not allowed to do this action");
        }
        return reviewService.deleteReview(review);
    }
}
