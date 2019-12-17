package protocol;

import actors.worker.NLPHelper;

import static protocol.ProtocolManager.getString;

public class MJobSentiment extends MJob {

    private String review;

    public MJobSentiment(int workerID, String review) {
        super.id = workerID;
        this.review = review;
    }

    @Override
    public String handle(NLPHelper helper) {
        return helper.findSentiment(review);
    }

    @Override
    public String toString() {
        return getString(getClass().getName(), super.id, this.review);
    }
}
