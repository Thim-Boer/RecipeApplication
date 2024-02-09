package recipeapplication.application.models;

public class UpdateReviewModel {
    Review orginalReview;
    Review updatedReview;

    public UpdateReviewModel(Review orginalReview, Review updatedReview) {
        this.orginalReview = orginalReview;
        this.updatedReview = updatedReview;
    }

    public Review getOriginal() {
        return this.orginalReview;
    }

    public Review getUpdated() {
        return this.updatedReview;
    }

    public boolean doIdsMatch() {
        if(orginalReview.id != updatedReview.id) {
            return false;
        }
        return true;
    }
}