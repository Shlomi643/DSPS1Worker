package protocol;

import static protocol.ProtocolManager.getString;

public class MReady extends MMessage {

    private String location;

    public MReady(String id, String content) {
        super.id = id;
        this.location = content;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return getString(getClass().getName(), super.id, this.location);
    }
}
