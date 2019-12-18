package actors.control;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import protocol.MReady;
import protocol.MTaskDownload;
import protocol.MTaskTerminate;
import protocol.ProtocolManager;
import utils.SQSConn;
import utils.SQSListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static utils.Utils.*;

public class Control {

    private static final int NUM_OF_LOCALS = 100;
    private final Object sqsLock = new Object();
    //    private String[] inputs;
//    private String[] outputs;
    private Map<String, String> insToOuts;
    private int numOfFiles;
    private int counter;
    private int n;
    private final String id;
    private boolean terminate;
    private AWSCredentialsProvider credentialsProvider;
    private AmazonS3 s3;
    private SQSConn out;

    public Control(String[] inputs, String[] outputs, int n, boolean terminate) {
        insToOuts = new HashMap<String, String>() {{
            for (int i = 0; i < inputs.length; i++)
                insToOuts.put(inputs[0], outputs[0]);
        }};

        this.n = n;
        this.terminate = terminate;
        this.numOfFiles = inputs.length;
        this.counter = this.numOfFiles;
        this.id = UUID.randomUUID().toString();
        this.credentialsProvider = new AWSStaticCredentialsProvider(new ProfileCredentialsProvider().getCredentials());
        this.s3 = getS3();

        createResources();

        this.out = new SQSConn(QUEUE_CONTROL_MANAGER);

    }

    private void createResources() {
        queueNames.forEach(SQSConn::createQueue);
    }

    private AmazonS3 getS3() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(REGION)
                .build();
    }

    private void commManager() {
        sendFiles();

        receiveFiles(); // Blocking

        if (this.terminate) {
            out.send(new MTaskTerminate(this.id, "").toString());
        }

        s3.deleteBucket(BUCKET_NAME);
//        deleteSQSs();

    }

    private void deleteSQSs() {
        conns.forEach(SQSConn::deleteQueue);
    }

    private void receiveFiles() {
        SQSListener in = new SQSListener(QUEUE_MANAGER_CONTROL, this::messageFromManager);
        in.start();

        //Main Thread block until all finished
        while (this.counter > 0) {
            synchronized (sqsLock) {
                try {
                    sqsLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        in.stop();
    }

    private void messageFromManager(Message message) {
        try {
            MReady msg = (MReady) ProtocolManager.parse(((TextMessage) message).getText());

            if (msg == null || this.id.equals(msg.getId()))
                return;

            String location = msg.getLocation();
            S3Object object = s3.getObject(new GetObjectRequest(BUCKET_NAME, location));

            message.acknowledge();

            synchronized (sqsLock) {
                this.counter--;
                createHTML(object.getObjectContent(), location);
                sqsLock.notify();
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void createHTML(S3ObjectInputStream objectContent, String location) {
        try {
            String fileName = location.substring(location.lastIndexOf("/") + 1);
            HTMLBuilder.build(getTextInputStream(objectContent), insToOuts.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFiles() {
        for (String filename : insToOuts.keySet()) {
            File file = new File(filename);
            String key = this.id + "/formanager/" + file.getName()
                    .replace('\\', '_').replace('/', '_').replace(':', '_');
            s3.putObject(new PutObjectRequest(BUCKET_NAME, key, file));
            out.send(new MTaskDownload(this.id, filename).toString());
        }
    }

    public int getNumOfFiles() {
        return numOfFiles;
    }

    public static void main(String[] args) {
        if (args.length > 0)
            startAsJar(args);
        else
            start();

//        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(new ProfileCredentialsProvider().getCredentials());
//
//        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
//                .withCredentials(credentialsProvider)
//                .withRegion(REGION)
//                .build();
//        for (Bucket bucket : s3.listBuckets()) {
//            System.out.println(" - " + bucket.getName());
//        }

    }

    private static void start() {
        String s = System.getProperty("user.dir") + "\\src\\main\\resources\\";
        Control control = new Control(new String[]{s + "0689835604.json"}, new String[]{""}, 0, false);
        control.commManager();
    }

    private static void startAsJar(String[] args) {
        int size = args.length;
        int N = size / 2;
        int n;
        boolean terminate = false;
        if (size % 2 == 0) {
            N--;
            terminate = true;
            n = Integer.parseInt(args[args.length - 2]);
        } else
            n = Integer.parseInt(args[args.length - 1]);

        Control control = new Control(Arrays.copyOfRange(args, 0, N),
                Arrays.copyOfRange(args, N, args.length - 1), n, terminate);
        control.commManager();
    }
}
