package recipeapplication.application.dto;

import recipeapplication.application.models.Review;

public class UpdateReviewModel {
    Review orginalReview;
    Review updatedReview;

    public UpdateReviewModel(Review orginalReview, Review updatedReview) {
        this.orginalReview = orginalReview;
        this.updatedReview = updatedReview;
    }

    public Review GetOriginal() {
        return this.orginalReview;
    }

    public Review GetUpdated() {
        return this.updatedReview;
    }

    public boolean DoIdsMatch() {
        if(orginalReview.id != updatedReview.id) {
            return false;
        }
        return true;
    }
}