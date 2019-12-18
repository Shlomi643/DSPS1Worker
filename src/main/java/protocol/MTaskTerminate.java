package protocol;

import static protocol.ProtocolManager.getString;

public class MTaskTerminate extends MTask {

    public MTaskTerminate(String terminatorID, String content) {
        super.id = terminatorID;
    }

    @Override
    public void handle(Executable e) {
        e.execute(super.id, "");
    }

    @Override
    public String toString() {
        return getString(getClass().getName(), super.id, "");
    }

}
