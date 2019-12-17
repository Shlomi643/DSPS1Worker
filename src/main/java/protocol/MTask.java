package protocol;

import static protocol.ProtocolManager.getString;

public class MTask extends MMessage {

    private String location;

    public MTask(int bossID, String location) {
        super.id = bossID;
        this.location = location;
    }

    @Override
    public String toString() {
        return getString(getClass().getName(), super.id, this.location);
    }
}
