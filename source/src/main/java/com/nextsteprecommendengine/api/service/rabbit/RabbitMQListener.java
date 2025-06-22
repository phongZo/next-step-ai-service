package com.nextsteprecommendengine.api.service.rabbit;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextsteprecommendengine.api.constant.NextStepRecommendengineConstant;
import com.nextsteprecommendengine.api.controller.ABasicController;
import com.nextsteprecommendengine.api.form.BaseSendMsgForm;
import com.nextsteprecommendengine.api.model.CandidateCv;
import com.nextsteprecommendengine.api.model.PostEmbedding;
import com.nextsteprecommendengine.api.repository.CandidateCvRepository;
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
    @Autowired
    private CandidateCvRepository candidateCvRepository;

    @RabbitListener(queues = "${rabbitmq.queue.process-cv}")
    public void handleListenUploadCv(String json) {
        try {
            BaseSendMsgForm<Map<String, Object>> form =
                    objectMapper.readValue(json, new TypeReference<>() {});

            String cmd       = form.getCmd();
            String app       = form.getApp();
            String subCmd    = form.getSubCmd();
            String token     = form.getToken();
            String response  = form.getResponseCode();
            Map<String, Object> dataMap = form.getData();

            if (NextStepRecommendengineConstant.PROCESS_EMBEDDING.equals(cmd)) {
                Long postId = ((Number) dataMap.get("postId")).longValue();
                String description = (String) dataMap.get("description");

                log.info("📥 Received PROCESS_EMBEDDING for postId={}, desc={}", postId, description);

                PostEmbedding pe = new PostEmbedding();
                pe.setPostId(postId);
                pe.setDescription(description);
                pe.setEmbedding("test");
                postEmbeddingRepository.save(pe);

                rabbitService.handleSendMsg(
                        app,
                        completeProcessCvQueue,
                        dataMap,
                        cmd,
                        subCmd,
                        response,
                        token
                );

                log.info("Forwarded to data-embedding: {}", dataMap);

            } else if (NextStepRecommendengineConstant.EXTRACT_CV.equals(cmd)) {
                Long candidateId = ((Number) dataMap.get("candidateId")).longValue();
                String cv = (String) dataMap.get("cv");

                log.info("📥 Received EXTRACT_CV for candidateId={}, cv={}", candidateId, cv);

                CandidateCv candidateCv = new CandidateCv();
                candidateCv.setCandidateId(candidateId);
                candidateCv.setCvPath(cv);
                candidateCv.setExtractedCv("test");
                candidateCvRepository.save(candidateCv);

                rabbitService.handleSendMsg(
                        app,
                        completeProcessCvQueue,
                        dataMap,
                        cmd,
                        subCmd,
                        response,
                        token
                );

                log.info("Forwarded to data-embedding: {}", dataMap);
            } else {
                log.warn("📥 Unrecognized cmd: {}", cmd);
            }

        } catch (JsonProcessingException e) {
            log.error("Failed to parse incoming JSON", e);
        } catch (RuntimeException e) {
            log.error("Error processing message", e);
        }
    }

}

