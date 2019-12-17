package utils;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import java.util.List;

import static utils.Utils.*;

public class SQSConn {

    private String queueURL;
    private AmazonSQS sqs;

    public SQSConn(String name) {
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(new ProfileCredentialsProvider().getCredentials());
        this.sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(REGION)
                .build();
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
}
