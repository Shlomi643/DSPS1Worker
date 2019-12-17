package actors;

import utils.SQSConn;

public class Worker {

    private SQSConn conn;

    public Worker() {

    }

    public static void main(String[] args) {
        Worker w = new Worker();
    }

}
