package cl.hccr.service.magnetoprocessor.service;

import cl.hccr.service.magnetoprocessor.domain.MutantRequest;
import cl.hccr.service.magnetoprocessor.repository.MutantRequestDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class SimpleMessageService implements MessageService {

    @Value("${cl.hccr.service.magnetoprocessor.queueUrl}")
    private String queueUrl;

    @Value("${cl.hccr.service.magnetoprocessor.maxNumberOfMessages}")
    private int maxNumberOfMessages;

    private SqsClient sqsClient;
    private MutantRequestDAO mutantRequestDAO;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10) ;


    public SimpleMessageService(SqsClient sqsClient, MutantRequestDAO mutantRequestDAO) {
        this.sqsClient = sqsClient;
        this.mutantRequestDAO = mutantRequestDAO;
    }


    @PostConstruct
    public void postConstruct(){
        executor.scheduleAtFixedRate(()->processMessages(), 100,100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void processMessages() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(maxNumberOfMessages)
                .build();


        List<Message> messageList = sqsClient.receiveMessage(receiveMessageRequest).messages();
        for (Message message : messageList) {
            threadPoolExecutor.execute(()->{
                try {
                    final MutantRequest mutantRequest = objectMapper.readValue(message.body(), MutantRequest.class);
                    mutantRequestDAO.insert(mutantRequest);
                    DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .receiptHandle(message.receiptHandle())
                            .build();
                    sqsClient.deleteMessage(deleteMessageRequest);

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        }

    }


    @PreDestroy
    public void preDestroy(){
        executor.shutdown();
        try {
            executor.awaitTermination(1,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
