package com.nextsteprecommendengine.api.service.rabbit;


import com.fasterxml.jackson.core.JsonProcessingException;
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
import com.nextsteprecommendengine.api.dto.AIProcessResponseDto;
import com.nextsteprecommendengine.api.service.feign.AIFeign;
import com.nextsteprecommendengine.api.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;
import java.io.FileInputStream;
import java.io.File;


import java.io.FileNotFoundException;
import java.io.IOException;
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
    @Autowired
    private AIFeign aiFeign;
    @Autowired
    private FileService fileService;

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
                CandidateCv candidateCv = new CandidateCv();
                log.info("Received EXTRACT_CV for candidateId={}, cv={}", candidateId, cv);
                Resource resource = fileService.loadFileAsResource(cv);
                if (resource != null && resource.exists()) {
                    File file = resource.getFile();
                    try (FileInputStream fis = new FileInputStream(file)) {
                        MultipartFile multipartFile = new MockMultipartFile(
                            file.getName(),
                            file.getName(),
                            "application/pdf",
                            fis
                        );
                        AIProcessResponseDto aiResponse = aiFeign.uploadAndProcess(multipartFile);
                        Object data = aiResponse.getData();
                        String jsonString = objectMapper.writeValueAsString(data);
                        candidateCv.setExtractedCv(jsonString);
                        log.info("AI Service response: {}", objectMapper.writeValueAsString(aiResponse));
                    } catch (FileNotFoundException e) {
                        try {
                            throw new RuntimeException(e);
                        } catch (RuntimeException ex) {
                            throw new RuntimeException(ex);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    log.error("File not found: {}/{}", cv);
                }
                candidateCv.setCandidateId(candidateId);
                candidateCv.setCvPath(cv);
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
                log.warn("Unrecognized cmd: {}", cmd);
            }

        } catch (JsonProcessingException e) {
            log.error("Failed to parse incoming JSON", e);
        } catch (RuntimeException | IOException e) {
            log.error("Error processing message", e);
        }
    }

}

