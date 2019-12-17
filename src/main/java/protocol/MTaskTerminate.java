package protocol;

import static protocol.ProtocolManager.getString;

public class MTaskTerminate extends MTask {

    public MTaskTerminate(int terminatorID, String content) {
        super.id = terminatorID;
    }

    @Override
    public String toString() {
        return getString(getClass().getName(), super.id, "");
    }

    @Override
    public void handle(Executable e) {
        e.execute(super.id, "");
    }
}
