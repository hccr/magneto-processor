package cl.hccr.service.magnetoprocessor.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface MessageService {
    void processMessages() throws JsonProcessingException;
}
