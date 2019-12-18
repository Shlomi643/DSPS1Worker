package protocol;

import static protocol.ProtocolManager.getString;

public class MReady extends MMessage {

    private String location;

    public MReady(int id, String content) {

    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return getString(getClass().getName(), super.id, this.location);
    }
}
