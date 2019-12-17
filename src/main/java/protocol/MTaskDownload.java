package protocol;

import static protocol.ProtocolManager.getString;

public class MTaskDownload extends MTask {

    private String location;

    public MTaskDownload(int bossID, String location) {
        super.id = bossID;
        this.location = location;
    }

    @Override
    public String toString() {
        return getString(getClass().getName(), super.id, this.location);
    }

    public void handle(Executable e) {
        e.execute(super.id, this.location);
    }
}
