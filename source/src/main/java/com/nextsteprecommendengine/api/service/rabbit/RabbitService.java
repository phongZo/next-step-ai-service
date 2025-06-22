package com.nextsteprecommendengine.api.service.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextsteprecommendengine.api.form.BaseSendMsgForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private com.nextsteprecommendengine.api.service.rabbit.RabbitSender rabbitSender;
    @Value("${rabbitmq.queue.process-cv}")
    private String processCvQueue;

    public  <T> void handleSendMsg(String appName, String queueName, T data, String cmd, String subCmd,String responseCode, String token) {
        BaseSendMsgForm<T> form = new BaseSendMsgForm<>();
        form.setApp(appName);
        form.setCmd(cmd);
        form.setSubCmd(subCmd);
        form.setData(data);
        form.setResponseCode(responseCode);
        form.setToken(token);
        String msg;
        try {
            msg = objectMapper.writeValueAsString(form);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // create queue if existed
        createQueueIfNotExist(queueName);

        // push msg
        rabbitSender.send(queueName, msg);
    }

    private void createQueueIfNotExist(String queueName) {
        rabbitSender.createQueueIfNotExist(queueName);
    }
}
