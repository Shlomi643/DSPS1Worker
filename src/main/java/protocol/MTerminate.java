package protocol;

import org.json.JSONObject;

import static protocol.ProtocolManager.getString;

public class MTerminate extends MMessage {

    public MTerminate(int terminatorID, String content) {
        super.id = terminatorID;
    }

    @Override
    public String toString() {
        return getString(getClass().getName(), super.id, "");
    }
}
