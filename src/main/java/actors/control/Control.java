package actors.control;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.sqs.AmazonSQS;

import static utils.Utils.*;

public class Control {

    private String[] inputs;
    private String[] outputs;
    private int numOfFiles;
    private int n;
    private boolean terminate;
    private AmazonS3 s3;
    private AmazonSQS sqs;

    public Control(String[] inputs, String[] outputs, int n, boolean terminate) {
        this.inputs = inputs.clone();
        this.outputs = outputs.clone();
        this.n = n;
        this.terminate = terminate;
        this.numOfFiles = inputs.length;

        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(new ProfileCredentialsProvider().getCredentials());
        this.s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(REGION)
                .build();
        commManager();
    }

    private void commManager() {
        sendFiles();

        receiveFiles(); // Blocking

        if (this.terminate) {
            // TODO send SQS message to terminate
        }
    }

    private void receiveFiles() {
        int counter = numOfFiles;
        while (counter > 0) {
            String key = ""; // TODO from SQS
            S3Object object = s3.getObject(new GetObjectRequest(BUCKET_NAME, key));
            try {
                displayTextInputStream(object.getObjectContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter--;
        }
    }

    private void sendFiles() {
        for (String filename : inputs) {
            System.out.println(filename);
            File file = new File(filename);
            String key = file.getName().replace('\\', '_').replace('/', '_').replace(':', '_');
            PutObjectRequest req = new PutObjectRequest(BUCKET_NAME, key, file);
            s3.putObject(req);
        }
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
//                .withRegion("us-west-2")
//                .build();
//        for (Bucket bucket : s3.listBuckets()) {
//            System.out.println(" - " + bucket.getName());
//        }

    }

    private static void start() {
        String s = System.getProperty("user.dir") + "\\src\\main\\resources\\";
        Control control = new Control(new String[]{s + "0689835604.json"}, new String[]{""}, 0, false);
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
    }
}
