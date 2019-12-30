package cl.hccr.service.magnetoprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MagnetoProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MagnetoProcessorApplication.class, args);
    }

}
/*

System.out.println("\nReceive messages");
        // snippet-start:[sqs.java2.sqs_example.retrieve_messages]
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .build();
        List<Message> messages= sqsClient.receiveMessage(receiveMessageRequest).messages();
        // snippet-end:[sqs.java2.sqs_example.retrieve_messages]


        System.out.println("\nChange Message Visibility");
        for (Message message : messages) {
            ChangeMessageVisibilityRequest req = ChangeMessageVisibilityRequest.builder().queueUrl(queueUrl)
                    .receiptHandle(message.receiptHandle()).visibilityTimeout(100).build();
            sqsClient.changeMessageVisibility(req);
        }


        System.out.println("\nDelete Messages");
        // snippet-start:[sqs.java2.sqs_example.delete_message]
        for (Message message : messages) {
            DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(message.receiptHandle())
                    .build();
            sqsClient.deleteMessage(deleteMessageRequest);
        }
 */