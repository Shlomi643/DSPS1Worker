package actors.manager;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import protocol.*;
import utils.SQSConn2;

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
    private Map<Integer, String> filesContols;
    private Map<String, ReviewFile> files;
    private List<Integer> workers;
    private ExecutorService workerPool;
    private ExecutorService localsPool;
    private AmazonS3 s3;
    private SQSConn2 inputConn;

    public Manager(int n) {
        this.n = n;
        this.workerPool = Executors.newCachedThreadPool();
        this.localsPool = Executors.newCachedThreadPool();
        this.workers = new ArrayList<>();
        this.files = new ConcurrentHashMap<>();
        this.filesContols = new ConcurrentHashMap<>();
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(new ProfileCredentialsProvider().getCredentials());
        this.s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(REGION)
                .build();
        this.inputConn = new SQSConn2(QUEUE_CONTROL_MANAGER, this::messageFromLocal);

    }

    private void start() {
        this.inputConn.start();
    }

    private void messageFromLocal(Message message) {
        try {
            MTask task = parseMessage(((TextMessage) message).getText());

            if (task instanceof MTaskTerminate)
                task.handle(this::terminate);
            else if (task instanceof MTaskDownload)
                task.handle(this::downloadAndDistribute);


            message.acknowledge(); // To delete messages
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private MTask parseMessage(String text) {
        return null;
    }

    private void terminate(int id, String content) {

    }

    private void downloadAndDistribute(int controlID, String fileLocation) {
        localsPool.execute(() -> {
            if (files.containsKey(fileLocation))
                return;
            S3Object object = s3.getObject(new GetObjectRequest(BUCKET_NAME, fileLocation));
            try {
                synchronized (this) {
                    filesContols.put(controlID, fileLocation);
                    files.put(fileLocation, new ReviewFile(getTextInputStream(object.getObjectContent())));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            workerPool.execute(this::assignJobs);
        });
    }

    private void assignJobs() {

    }


    public static void main(String[] args) {
        Manager m = new Manager(0);
        m.start();
    }
}
