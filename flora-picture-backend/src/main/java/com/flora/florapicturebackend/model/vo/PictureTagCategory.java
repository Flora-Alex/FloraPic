package com.flora.florapicturebackend.model.vo;

import java.util.List;

import lombok.Data;

@Data
public class PictureTagCategory {
    private List<String> tagList;

    private List<String> categoryList;

}
