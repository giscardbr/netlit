package br.com.netlit.accounts.domain.account.parent.resource;

import java.util.UUID;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;

import br.com.netlit.accounts.domain.account.general.entity.AccountRole;

public class AmazonSNSSender {

    // AWS credentials -- replace with your credentials
    static String ACCESS_KEY = "AKIAW6XF2QZI56OHM2MH";
    static String SECRET_KEY = "WGRTL8lciINsDVNMVqT/6lkxrMCpPydOODrw2YzS";

    // Sender loop
    public static void main(String... args) throws Exception {

        // Create a client
        AmazonSNSClient service = new AmazonSNSClient(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY));

        // Create a topic
        CreateTopicRequest createReq = new CreateTopicRequest()
            .withName("credentials-created-topic");
        CreateTopicResult createRes = service.createTopic(createReq);

        
        CredentialsCreatedEvent event = new CredentialsCreatedEvent(
        		UUID.fromString("977d9dfc-d00a-41e3-933c-8d0a7bdb1718"),
        		"giscard.ti@gmail.com",
        		"password",
        		AccountRole.ADMIN);
        
        String message = "";
        
        
        for (;;) {

            // Publish to a topic
            PublishRequest publishReq = new PublishRequest()
                .withTopicArn(createRes.getTopicArn())
                .withMessage(message);
            service.publish(publishReq);

            Thread.sleep(1000);
        }
    }
}