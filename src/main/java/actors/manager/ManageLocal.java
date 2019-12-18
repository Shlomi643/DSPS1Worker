package actors.manager;

import javafx.util.Pair;
import org.json.JSONObject;

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
        SummaryFile tmp = new SummaryFile(file.getNumOfReviews());
        for (Map.Entry<String, String> entry : file.getReviews().entrySet()) {
            JSONObject obj = new JSONObject(entry.getValue());
            Review rev = new Review(obj.getString("id"), obj.getString("text"), obj.getInt("rating"));
            tmp.addReview(rev);
        }
        this.files.put(filename, new Pair<>(file, tmp));
    }

    public void addSentiment(String filename, String reviewId, String sentiment) {
        files.get(filename).getValue().updateReviewSentiment(reviewId, sentiment);
    }

    public void addEntity(String filename, String reviewId, String entity) {
        files.get(filename).getValue().updateReviewEntity(reviewId, entity);
    }

    public Map<String, Pair<ReviewFile, SummaryFile>> getFiles() {
        return files;
    }

    public boolean ready() {
        return files.values().stream().map(Pair::getValue).allMatch(SummaryFile::isReady);
    }

    @Override
    public String toString() {
        return "ManageLocal{" +
                "localID='" + localID + '\'' +
                ", files=" + files +
                '}';
    }
}
