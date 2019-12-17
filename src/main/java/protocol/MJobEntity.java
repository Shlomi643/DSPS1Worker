package protocol;

import actors.worker.NLPHelper;

import static protocol.ProtocolManager.getString;

public class MJobEntity extends MJob {

    private String review;

    public MJobEntity(int id, String review) {
        super.id = id;
        this.review = review;
    }

    @Override
    public String handle(NLPHelper helper) {
        return helper.extractEntities(review);
    }

    @Override
    public String toString() {
        return getString(getClass().getName(), super.id, this.review);
    }

}
