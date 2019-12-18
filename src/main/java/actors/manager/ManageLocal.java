package actors.manager;

import javafx.util.Pair;

import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;

public class ManageLocal {

    private String localID;
    private Map<String, Pair<ReviewFile, SummaryFile>> files; // Filename to review and summary

    public ManageLocal(String localID) {
        this.localID = localID;
        this.files = new ConcurrentHashMap<>();
    }

    public void addReviewFile(ReviewFile file, String filename) {
        this.files.put(filename, new Pair<>(file, new SummaryFile(file.getNumOfReviews())));
    }

    public void addToSummaryFile(String filename, Review review) {
        files.get(filename).getValue().addReview(review);
    }

    public void addSentiment(String filename, String reviewId, String sentiment) {
        files.get(filename).getValue().updateReviewSentiment(reviewId, sentiment);
    }

    public void addEntity(String filename, String reviewId, String entity) {
        files.get(filename).getValue().updateReviewEntity(reviewId, entity);
    }

    public boolean ready() {
        return files.values().stream().map(Pair::getValue).allMatch(SummaryFile::isReady);
    }
}
