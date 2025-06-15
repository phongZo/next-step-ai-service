package com.nextsteprecommendengine.api.service.rabbit;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextsteprecommendengine.api.constant.NextStepRecommendengineConstant;
import com.nextsteprecommendengine.api.controller.ABasicController;
import com.nextsteprecommendengine.api.form.BaseSendMsgForm;
import com.nextsteprecommendengine.api.model.PostEmbedding;
import com.nextsteprecommendengine.api.repository.PostEmbeddingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@Slf4j
public class RabbitMQListener extends ABasicController {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RabbitService rabbitService;
    @Value("${rabbitmq.queue.complete-process-cv}")
    private String completeProcessCvQueue;
    @Autowired
    private PostEmbeddingRepository postEmbeddingRepository;

    @RabbitListener(queues = "${rabbitmq.queue.process-cv}")
    public void handleListenUploadCv(String json) {
        try {
           
            BaseSendMsgForm<Map<String,Object>> form =
                    objectMapper.readValue(
                            json,
                            new TypeReference<BaseSendMsgForm<Map<String,Object>>>() {}
                    );

            // 2. Chỉ quan tâm tới đúng PROCESS_EMBEDDING
            if (NextStepRecommendengineConstant.PROCESS_EMBEDDING.equals(form.getCmd())
                    && NextStepRecommendengineConstant.PROCESS_EMBEDDING.equals(form.getSubCmd())) {

                Map<String,Object> data = form.getData();
                Long postId       = ((Number)data.get("postId")).longValue();
                String description= (String)data.get("description");
                String token      = form.getToken();

                log.info("📥 Received PROCESS_EMBEDDING for postId={}, desc={}", postId, description);

                PostEmbedding postEmbedding = new PostEmbedding();
                postEmbedding.setPostId(postId);
                postEmbedding.setDescription(description);
                postEmbedding.setEmbedding("test");
                postEmbeddingRepository.save(postEmbedding);


                rabbitService.handleSendMsg(
                        form.getApp(),
                        completeProcessCvQueue,
                        data,
                        form.getCmd(),
                        form.getSubCmd(),
                        form.getResponseCode(),
                        token
                );

                log.info("Forwarded to data-embedding: {}", data);
            }

        } catch (JsonProcessingException e) {
            log.error("Failed to parse incoming JSON", e);
        } catch (RuntimeException e) {
            log.error("Error processing message", e);
        }
    }
}

