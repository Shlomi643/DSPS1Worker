package actors.manager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class ReviewFile {

    // Review is JSON as in files
    private Map<String, String> reviews;
    private int numOfReviews;


    public ReviewFile(String reviews) {
        this.reviews = new ConcurrentHashMap<>();

        addReviews(reviews);

        this.numOfReviews = this.reviews.size();
    }

    private void addReviews(String reviews) {
        StringBuilder s = new StringBuilder();
        for (String line : reviews.split("\n")) {
            s.append(line);
            if (line.equals("}")) {
                JSONObject obj = new JSONObject(s.toString());
                JSONArray arr = obj.getJSONArray("reviews");
                arr.forEach(x -> this.reviews.put(getID(x), x.toString()));
                s = new StringBuilder();
            }

        }
    }

    private String getID(Object obj) {
        return ((JSONObject) obj).getString("id");
    }

    public Map<String, String> getReviews() {
        return reviews;
    }

    public int getNumOfReviews() {
        return numOfReviews;
    }

    @Override
    public String toString() {
        return "ReviewFile{" +
                "reviews=" + reviews +
                ", numOfReviews=" + numOfReviews +
                '}';
    }
}
