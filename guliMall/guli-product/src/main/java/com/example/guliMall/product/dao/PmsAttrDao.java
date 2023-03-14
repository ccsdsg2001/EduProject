package com.example.guliMall.product.dao;

import com.example.guliMall.product.entity.PmsAttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 * 
 * @author cc
 * @email ccsdsg2016@gmail.com
 * @date 2023-02-17 21:28:00
 */
@Mapper
public interface PmsAttrDao extends BaseMapper<PmsAttrEntity> {

    List<Long> selectSearchAttrs(@Param("attrIds") List<Long> attrIds);

}
