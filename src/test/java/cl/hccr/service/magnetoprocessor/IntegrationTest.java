package cl.hccr.service.magnetoprocessor;

import cl.hccr.service.magnetoprocessor.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SpringBootTest(webEnvironment = NONE)
@ActiveProfiles("test")
class IntegrationTest {

    @MockBean
    private SqsClient sqsClient;

    @Autowired
    private MessageService messageService;







    @Test
    void receiveAndProcessMessage() {
        //arrange


        String messageString = "{\"dna\":[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"],\"mutant\":true}";
        given(sqsClient.receiveMessage(any(ReceiveMessageRequest.class)))
                .willReturn(ReceiveMessageResponse.builder().messages(
                        Message.builder()
                                .body(messageString)
                                .build())
                        .build());
        try{
            messageService.processMessages();
        }catch (Exception e){
            Assertions.fail();
        }




    }
}
