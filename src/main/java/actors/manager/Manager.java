package actors.manager;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import protocol.MMessage;
import com.amazonaws.services.sqs.model.Message;
import protocol.ProtocolManager;
import utils.SQSConn;

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
        new Thread(() -> listenToLocal(new SQSConn(QUEUE_CONTROL_MANAGER)));
    }

    private void listenToLocal(SQSConn localConn) {
        while (true) {
            List<Message> messages = localConn.receive();

            messages.forEach(this::handleMessage);

            localConn.delete(messages);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMessage(Message message) {
        MMessage mMessage = ProtocolManager.parse(message.getBody());
        if (mMessage == null)
            return;
        
    }

    public void doTask(int controlID, String fileLocation) {
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

    public void terminate() {
//        ExecutorService
    }

    public static void main(String[] args) {
        Set<Integer> v = new HashSet<Integer>();
        v.add(1);
        v.add(2);
        v.add(3);
        System.out.println(v);
        Set<Integer> v1 = new HashSet<Integer>();
        v1.add(2);
        v1.add(1);
        v1.add(3);
        System.out.println(v1);
    }
}
