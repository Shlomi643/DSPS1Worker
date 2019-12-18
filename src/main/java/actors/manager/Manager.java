package actors.manager;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.json.JSONArray;
import protocol.*;
import utils.SQSConn;
import utils.SQSListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static utils.Utils.*;

public class Manager {

    private static final int MAX_WORKERS = 20;
    private int n;
    private Map<String, ManageLocal> manageLocals;
    private List<Integer> workers;
    private ExecutorService summaryPool;
    private ExecutorService workersPool;
    private ExecutorService localsPool;
    private AmazonS3 s3;
    private SQSListener controlListener;
    private SQSListener workersListener;
    private SQSConn workersConn;

    public Manager(int n) {
        this.n = n;
        this.manageLocals = new ConcurrentHashMap<>();
        this.summaryPool = Executors.newCachedThreadPool();
        this.workersPool = Executors.newCachedThreadPool();
        this.localsPool = Executors.newCachedThreadPool();
        this.workers = new ArrayList<>();
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(new ProfileCredentialsProvider().getCredentials());
        this.s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(REGION)
                .build();
        this.controlListener = new SQSListener(QUEUE_CONTROL_MANAGER, this::messageFromLocal);
        this.workersListener = new SQSListener(QUEUE_WORKER_MANAGER, this::messageFromWorker);
        this.workersConn = new SQSConn(QUEUE_MANAGER_WORKER);
    }

    private void start() {
        this.controlListener.start();
        this.workersListener.start();
    }

    private void messageFromWorker(Message message) {
        try {
            MResponse response = (MResponse) ProtocolManager.parse(((TextMessage) message).getText());

            summaryPool.execute(executeResponse(response));
            message.acknowledge();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void messageFromLocal(Message message) {
        try {
            MTask task = parseMessage(((TextMessage) message).getText());

            if (task instanceof MTaskTerminate)
                task.handle(this::terminate);
            else
                task.handle(this::downloadAndDistribute);

            message.acknowledge(); // To delete messages
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private MTask parseMessage(String text) {
        return (MTask) ProtocolManager.parse(text);
    }

    // TODO
    private void terminate(String... ids) {
        String id = ids[0];
    }

    private void downloadAndDistribute(String controlID, String fileName) {
        localsPool.execute(() -> {

            String fileLocation = controlID + "/formanager/" + fileName;
            S3Object object = s3.getObject(new GetObjectRequest(BUCKET_NAME, fileLocation));

            manageLocals.putIfAbsent(controlID, new ManageLocal(controlID));

            try {
                ReviewFile file = new ReviewFile(getTextInputStream(object.getObjectContent()));
                manageLocals.get(controlID)
                        .addReviewFile(file, fileName);
                file.getReviews().forEach((id, review) ->
                        workersPool.execute(work(controlID, fileName, id, review)));
                workersPool.execute(createWorkers(roundUp(file.getNumOfReviews(), n))); //TODO maybe different pool
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    private Runnable work(String controlId, String fileName, String reviewID, String review) {
        return () -> {
            workersConn.send(new MJobEntity(controlId, fileName, reviewID, review).toString());
            workersConn.send(new MJobSentiment(controlId, fileName, reviewID, review).toString());
        };
    }

    // TODO
    private Runnable createWorkers(int number) {
        return () -> {

        };
    }

    private Runnable executeResponse(MResponse response) {
        return () -> {
            if (response instanceof MResponseSentiment) {
                MResponseSentiment m = (MResponseSentiment) response;
                ManageLocal local = manageLocals.get(m.getId());
                local.addSentiment(m.getFilename(), m.getReviewID(), m.getSentiment()); // TODO
            } else {
                MResponseEntity m = (MResponseEntity) response;
                ManageLocal local = manageLocals.get(m.getId());
                local.addSentiment(m.getFilename(), m.getReviewID(), m.getEntity());
            }
        };
    }

    public static void main(String[] args) {
        Manager m = new Manager(0);
        m.start();
    }
}
