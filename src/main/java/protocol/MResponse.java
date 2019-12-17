package protocol;

import static protocol.ProtocolManager.getString;

public class MResponse extends MMessage {

    private String response;

    public MResponse(int id, String response) {
        super.id = id;
        this.response = response;
    }

    public static String name() {
        return "MResponse";
    }

    @Override
    public String toString() {
        return getString(getClass().getName(), super.id, this.response);
    }
}
