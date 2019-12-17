package protocol;

import actors.worker.NLPHelper;

public abstract class MJob extends MMessage {

    public abstract String handle(NLPHelper helper);

    public int getId(){
        return super.id;
    }
}
