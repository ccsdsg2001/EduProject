package com.example.guliMall.product.dao;

import com.example.guliMall.product.entity.PmsAttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author cc
 * @email ccsdsg2016@gmail.com
 * @date 2023-02-17 21:28:00
 */
@Mapper
public interface PmsAttrAttrgroupRelationDao extends BaseMapper<PmsAttrAttrgroupRelationEntity> {

    void deleteBatchRelation(List<PmsAttrAttrgroupRelationEntity> entities);
}
