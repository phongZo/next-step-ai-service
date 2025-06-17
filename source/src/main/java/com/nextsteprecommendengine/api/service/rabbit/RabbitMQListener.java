package com.nextsteprecommendengine.api.service.rabbit;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextsteprecommendengine.api.constant.NextStepRecommendengineConstant;
import com.nextsteprecommendengine.api.controller.ABasicController;
import com.nextsteprecommendengine.api.dto.cvembedding.CvEmbeddingDto;
import com.nextsteprecommendengine.api.dto.postembedding.PostEmbeddingDto;
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
            JsonNode root          = objectMapper.readTree(json);
            String cmd             = root.path("cmd").asText();
            JsonNode dataNode      = root.path("data");
            String app             = root.path("app").asText();
            String subCmd          = root.path("subCmd").asText();
            String responseCode    = root.path("responseCode").asText();
            String token           = root.path("token").asText();

            if (NextStepRecommendengineConstant.PROCESS_EMBEDDING.equals(cmd)) {
                PostEmbeddingDto data = objectMapper.treeToValue(dataNode, PostEmbeddingDto.class);
                Long postId           = data.getPostId();
                String description    = data.getDescription();

                log.info("📥 Received PROCESS_EMBEDDING for postId={}, desc={}", postId, description);

                PostEmbedding pe = new PostEmbedding();
                pe.setPostId(postId);
                pe.setDescription(description);
                pe.setEmbedding("test");
                postEmbeddingRepository.save(pe);

                rabbitService.handleSendMsg(
                        app,
                        completeProcessCvQueue,
                        data,
                        cmd,
                        subCmd,
                        responseCode,
                        token
                );

                log.info("Forwarded to data-embedding: {}", data);

            } else if (NextStepRecommendengineConstant.EXTRACT_CV.equals(cmd)) {
                CvEmbeddingDto data = objectMapper.treeToValue(dataNode, CvEmbeddingDto.class);
                Long candidateId    = data.getCandidateId();
                String cv           = data.getCv();

                log.info("📥 Received EXTRACT_CV for candidateId={}, cv={}", candidateId, cv);

                CandidateCv candidateCv = new CandidateCv();
                candidateCv.setCandidateId(candidateId);
                candidateCv.setCvPath(cv);
                candidateCv.setExtractedCv("test");
                candidateCvRepository.save(candidateCv);

                rabbitService.handleSendMsg(
                        app,
                        completeProcessCvQueue,
                        data,
                        cmd,
                        subCmd,
                        responseCode,
                        token
                );

                log.info("Forwarded to data-embedding: {}", data);
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

