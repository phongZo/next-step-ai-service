package com.nextsteprecommendengine.api.dto.category;

import com.nextsteprecommendengine.api.dto.ABasicAdminDto;
import lombok.Data;


@Data
public class CategoryDto extends ABasicAdminDto {
    private String name;
    private String description;
    private String image;
    private Integer ordering;
    private Integer kind;
    private String tag;
}
