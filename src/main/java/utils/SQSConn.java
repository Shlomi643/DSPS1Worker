package utils;


import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.Utils.*;

public class SQSConn {

    private String queueURL;
    private static AmazonSQS sqs = getSQS();

    private static AmazonSQS getSQS() {
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(new ProfileCredentialsProvider().getCredentials());
        return AmazonSQSClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(REGION)
                .build();
    }

    public SQSConn(String name) {

        createQueue(name);
        this.queueURL = sqs.getQueueUrl(name).getQueueUrl();
    }

    public void send(String message) {
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(this.queueURL)
                .withMessageBody(message)
                .withDelaySeconds(5);
        sqs.sendMessage(send_msg_request);
    }

    public List<Message> receive() {
        return sqs.receiveMessage(this.queueURL).getMessages();
    }

    public void delete(Message... messages) {
        for (Message m : messages) {
            sqs.deleteMessage(this.queueURL, m.getReceiptHandle());
        }
    }

    public void delete(List<Message> messages) {
        for (Message m : messages) {
            sqs.deleteMessage(this.queueURL, m.getReceiptHandle());
        }
    }

    public void deleteQueue() {
        sqs.deleteQueue(new DeleteQueueRequest(this.queueURL));
    }

    public static void createQueue(String name) {
        try {
            sqs.createQueue(new CreateQueueRequest(name));
        } catch (QueueNameExistsException ignored) {

        }
    }

    private static Map<String, String> getAttributes() {
        return new HashMap<String, String>() {{
            put("FifoQueue", "true");
            put("ContentBasedDeduplication", "true");
        }};
    }

}
