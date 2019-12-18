package protocol;

import static protocol.ProtocolManager.getString;

public class MResponseSentiment extends MResponse {

    private String sentiment;
    private String filename;
    private String reviewID;

    public MResponseSentiment(String id, String filename, String reviewID, String sentiment) {
        super.id = id;
        this.sentiment = sentiment;
        this.filename = filename;
        this.reviewID = reviewID;
    }

    public String getSentiment() {
        return sentiment;
    }

    public String getFilename() {
        return filename;
    }

    public String getReviewID() {
        return reviewID;
    }

    @Override
    public String toString() {
        return getString(getClass().getName(), super.id, this.filename, this.reviewID, this.sentiment);
    }
}
