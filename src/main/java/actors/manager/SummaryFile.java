package actors.manager;

import org.json.JSONArray;

import java.util.Vector;

public class SummaryFile {

    private Vector<Review> reviews;
    private int maximum;

    public SummaryFile(int maximum) {
        this.maximum = maximum;
        this.reviews = new Vector<>();
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public void updateReviewSentiment(String reviewID, String sentiment) {
        for (Review review : reviews)
            if (review.getId().equals(reviewID))
                review.setSentiment(sentiment);
    }

    public void updateReviewEntity(String reviewID, String entity) {
        for (Review review : reviews)
            if (review.getId().equals(reviewID))
                review.setEntities(entity);
    }

    public boolean isReady() {
        return reviews.size() == maximum && reviews.stream().allMatch(Review::isReady);
    }

    public JSONArray getAsJSON() {
        JSONArray arr = new JSONArray();
        reviews.forEach(x -> arr.put(x.getAsJson()));
        return arr;
    }

    @Override
    public String toString() {
        return getAsJSON().toString();
    }
}
