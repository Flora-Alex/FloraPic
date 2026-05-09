package com.flora.florapicturebackend.model.dto.picture;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data

public class PictureEditRequest implements Serializable {
    private Long id;

    private String name;

    private String introduction;

    private String categery;

    private List<String> tags;

    private static final long serialVersionUID = 1L;

}
