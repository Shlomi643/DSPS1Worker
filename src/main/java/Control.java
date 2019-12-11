import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class Control {

    private String[] inputs;
    private String[] outputs;
    private int n;
    private boolean terminate;

    public Control(String[] inputs, String[] outputs, int n, boolean terminate) {
        this.inputs = inputs.clone();
        this.outputs = outputs.clone();
        this.n = n;
        this.terminate = terminate;
        commManager();
    }

    private void commManager() {
        Manager manager = Manager.getInstance();
        if (!manager.isActive())
            manager.activate();

        sendFiles();

        if(this.terminate);
    }

    private void sendFiles(){
        for(String filename : inputs){
            try {
                String toSend = new String(Files.readAllBytes(Paths.get("src/main/resources/" + filename)));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

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
