package protocol;

public class Terminate extends MMessage {

    private int terminatorID;

    public Terminate(int terminatorID) {
        this.terminatorID = terminatorID;
    }
}
