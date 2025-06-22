package com.nextsteprecommendengine.api.form.category;


import com.nextsteprecommendengine.api.validation.CategoryKind;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel
public class CreateCategoryForm {
    @NotEmpty(message = "name cant not be null")
    @ApiModelProperty(name = "name", required = true)
    private String name;

    @ApiModelProperty(name = "description", required = false)
    private String description;

    @ApiModelProperty(name = "image", required = false)
    private String image;

    @ApiModelProperty(name = "ordering", required = false)
    private Integer ordering;

    @ApiModelProperty(name = "kind", required = true)
    @CategoryKind(allowNull = false)
    private Integer kind;

    @ApiModelProperty(name = "tag", required = true)
    private String tag;

}
