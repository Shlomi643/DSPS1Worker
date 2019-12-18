package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static final String BUCKET_NAME = "dsps1-dan-shlomi-eu";
    public static final String REGION = "eu-central-1";
    private static final String NAME = "dsps";
    public static final String QUEUE_CONTROL_MANAGER = NAME + "_" + "control_manager";
    public static final String QUEUE_MANAGER_CONTROL = NAME + "_" + "manager_control";
    public static final String QUEUE_MANAGER_WORKER = NAME + "_" + "manager_worker";
    public static final String QUEUE_WORKER_MANAGER = NAME + "_" + "worker_manager";

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

            s.append(line).append("\n");
        }
        return s.toString();
    }

    public static List<String> queueNames = new ArrayList<String>() {{
        add(QUEUE_CONTROL_MANAGER);
        add(QUEUE_MANAGER_CONTROL);
        add(QUEUE_WORKER_MANAGER);
        add(QUEUE_MANAGER_WORKER);
    }};

    public static List<SQSConn> conns = new ArrayList<SQSConn>() {{
        queueNames.forEach(x -> add(new SQSConn(x)));
    }};

    public static int roundUp(int dividend, int divisor) {
        return (int) Math.ceil((double) dividend / divisor);
    }

}
