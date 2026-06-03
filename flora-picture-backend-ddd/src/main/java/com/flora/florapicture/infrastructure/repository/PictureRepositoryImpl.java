package com.flora.florapicture.infrastructure.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flora.florapicture.domain.picture.entity.Picture;
import com.flora.florapicture.domain.picture.repository.PictureRepository;
import com.flora.florapicture.infrastructure.mapper.PictureMapper;
import org.springframework.stereotype.Service;

/**
 * 图片仓储实现
 */
@Service
public class PictureRepositoryImpl extends ServiceImpl<PictureMapper, Picture> implements PictureRepository {
}