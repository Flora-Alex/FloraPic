package com.flora.florapicture.infrastructure.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flora.florapicture.domain.space.entity.Space;
import com.flora.florapicture.domain.space.repository.SpaceRepository;
import com.flora.florapicture.infrastructure.mapper.SpaceMapper;
import org.springframework.stereotype.Service;

/**
 * 空间仓储实现
 */
@Service
public class SpaceRepositoryImpl extends ServiceImpl<SpaceMapper, Space> implements SpaceRepository {
}