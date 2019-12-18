package utils;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Session;

import static utils.Utils.REGION;

public class SQSListener {

    private String queueName;
    private MessageListener listener;
    private SQSConnection connection;

    public SQSListener(String name, MessageListener listener) {
        this.queueName = name;
        this.listener = listener;
        this.connection = createConnection();

    }

    private SQSConnection createConnection() {
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(new ProfileCredentialsProvider().getCredentials());
        SQSConnectionFactory factory = new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClientBuilder.standard().withRegion(REGION).withCredentials(credentialsProvider));
        try {
            this.connection = factory.createConnection();
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            session.createConsumer(session.createQueue(this.queueName)).setMessageListener(this.listener);
            return connection;
        } catch (JMSException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public void start() {
        try {
            this.connection.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            this.connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
