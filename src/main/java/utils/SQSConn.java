package utils;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import java.util.List;

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
        sqs.createQueue(new CreateQueueRequest(name));
    }

}
