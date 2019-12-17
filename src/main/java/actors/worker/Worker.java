package actors.worker;

import protocol.*;
import utils.SQSConn;
import utils.SQSConn2;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import static utils.Utils.QUEUE_MANAGER_WORKER;
import static utils.Utils.QUEUE_WORKER_MANAGER;

public class Worker {

    private SQSConn2 connIn;
    private SQSConn connOut;

    public Worker() {
        this.connIn = new SQSConn2(QUEUE_MANAGER_WORKER, this::messageFromManager);
        this.connOut = new SQSConn(QUEUE_WORKER_MANAGER);
    }

    private void messageFromManager(Message message) {
        try {
            MJob job = parseMessage(((TextMessage) message).getText());
            String result = handleMessage(job);
            connOut.send(new MResponse(job.getId(), result).toString());
            message.acknowledge(); // To delete messages
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private MJob parseMessage(String text) {
        return (MJob) ProtocolManager.parse(text);
    }

    private String handleMessage(MJob review) {
        NLPHelper helper = new NLPHelper();
        return review.handle(helper);
    }

    private void start() {
        connIn.start();
    }

    private void stop() {
        connIn.stop();
    }

    public static void main(String[] args) {
        Worker w = new Worker();
        w.start();
    }

}
