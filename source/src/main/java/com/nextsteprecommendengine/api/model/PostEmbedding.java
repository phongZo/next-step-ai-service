package com.nextsteprecommendengine.api.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_post_embedding")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class PostEmbedding extends Auditable<String>{
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.nextsteprecommendengine.api.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;

    private Long postId;

    private String description;

    @Column(name = "embedding", columnDefinition = "LONGTEXT")
    private String embedding;
}
