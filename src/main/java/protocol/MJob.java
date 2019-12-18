package protocol;

import actors.worker.NLPHelper;

public abstract class MJob extends MMessage {

    public abstract MResponse handle(NLPHelper helper);

    public abstract String getFileName();

}
