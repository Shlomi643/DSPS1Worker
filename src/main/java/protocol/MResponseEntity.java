package protocol;

import static protocol.ProtocolManager.getString;

public class MResponseEntity extends MResponse {

    private String entity;
    private String filename;
    private String reviewID;

    public MResponseEntity(String id, String filename, String reviewID, String entity) {
        super.id = id;
        this.filename = filename;
        this.entity = entity;
        this.reviewID = reviewID;
    }

    public String getEntity() {
        return entity;
    }

    public String getFilename() {
        return filename;
    }

    public String getReviewID() {
        return reviewID;
    }

    @Override
    public String toString() {
        return getString(getClass().getName(), super.id, this.filename, this.reviewID, this.entity);
    }
}
