package protocol;

import actors.worker.NLPHelper;

import static protocol.ProtocolManager.getString;

public class MJobEntity extends MJob {

    private String review;
    private String fileName;
    private String reviewID;

    public MJobEntity(String id, String fileName, String reviewID, String review) {
        super.id = id;
        this.review = review;
        this.fileName = fileName;
        this.reviewID = reviewID;
    }

    public String getReview() {
        return review;
    }

    public String getFileName() {
        return fileName;
    }

    public String getReviewID() {
        return reviewID;
    }

    @Override
    public MResponse handle(NLPHelper helper) {
        return new MResponseEntity(id, fileName, reviewID, helper.extractEntities(review));
    }

    @Override
    public String toString() {
        return getString(getClass().getName(), super.id, this.fileName, this.reviewID, this.review);
    }


}
