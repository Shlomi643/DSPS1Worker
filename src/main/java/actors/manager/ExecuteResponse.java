package actors.manager;

import protocol.MResponse;

public class ExecuteResponse implements Runnable {

    private MResponse response;

    public ExecuteResponse(MResponse response) {
        this.response = response;
    }

    @Override
    public void run() {

    }
}
