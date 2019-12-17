package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {

    public static final String BUCKET_NAME = "dsps1-dan-shlomi";
    public static final String REGION = "us-east-1";
    public static final String NAME = "dsps1-dan-shlomi-queue";
    public static final String QUEUE_CONTROL_MANAGER = NAME + "-" + "manager-control.fifo";
    public static final String QUEUE_MANAGER_WORKER = NAME + "-" + "manager-worker.fifo";
    public static final String QUEUE_WORKER_MANAGER = NAME + "-" + "worker-manager.fifo";

    public static void displayTextInputStream(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            System.out.println("    " + line);
        }
        System.out.println();
    }

    public static String getTextInputStream(InputStream input) throws IOException {
        StringBuilder s = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            s.append(line);
        }
        return s.toString();
    }


}
