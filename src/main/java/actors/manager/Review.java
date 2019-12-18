package actors.manager;

import org.json.JSONObject;

import java.util.Objects;

public class Review {

    private String id;
    private int rating;
    private String review;
    private String sentiment;
    private String entities;
    private boolean ready;

    public Review(String id, String review, int rating) {
        this.id = id;
        this.review = review;
        this.rating = rating;
        this.sentiment = null;
        this.entities = null;
        this.ready = false;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
        if (entities != null)
            ready = true;
    }

    public void setEntities(String entities) {
        this.entities = entities;
        if (sentiment != null)
            ready = true;
    }

    public String getSentiment() {
        return sentiment;
    }

    public String getEntities() {
        return entities;
    }

    public String getId() {
        return id;
    }

    public boolean isReady() {
        return ready;
    }

    public JSONObject getAsJson() {
        JSONObject ret = new JSONObject();
        ret.put("id", this.id);
        ret.put("review", this.review);
        ret.put("rating", this.rating);
        ret.put("sentiment", this.sentiment);
        ret.put("entity", this.entities);
        return ret;
    }

    @Override
    public String toString() {
        return getAsJson().toString();
    }
}
