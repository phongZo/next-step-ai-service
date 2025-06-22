package com.nextsteprecommendengine.api.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_category")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Category extends Auditable<String>{
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.nextsteprecommendengine.api.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;
    private String name;
    @Column(name = "description" ,  columnDefinition = "TEXT")
    private String description;
    private String image;
    private Integer ordering;
    private Integer kind;
    private String tag;
}
