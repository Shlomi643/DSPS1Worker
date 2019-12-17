package protocol;

import static protocol.ProtocolManager.getString;

public class MReady extends MMessage {

    private String content;

    public MReady(int id, String content) {

    }

    @Override
    public String toString() {
        return getString(getClass().getName(), super.id, this.content);
    }
}
