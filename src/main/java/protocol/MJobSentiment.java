package protocol;

import actors.worker.NLPHelper;

import static protocol.ProtocolManager.getString;

public class MJobSentiment extends MJob {

    private String review;
    private String filename;
    private String reviewID;

    public MJobSentiment(String id, String review, String reviewID, String filename) {
        super.id = id;
        this.review = review;
        this.reviewID = reviewID;
        this.filename = filename;
    }

    public String getReview() {
        return review;
    }

    public String getFileName() {
        return filename;
    }

    public String getReviewID() {
        return reviewID;
    }

    @Override
    public MResponse handle(NLPHelper helper) {
        return new MResponseSentiment(id, filename, reviewID, helper.findSentiment(review));
    }

    @Override
    public String toString() {
        return getString(getClass().getName(), super.id, this.filename, this.reviewID, this.review);
    }
}
