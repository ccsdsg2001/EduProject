package com.example.guliMall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.guliMall.product.entity.PmsAttrEntity;
import com.example.guliMall.product.vo.AttrGroupRelationVo;
import com.example.guliMall.product.vo.attrresponsevo;
import com.example.guliMall.product.vo.attrvo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author cc
 * @email ccsdsg2016@gmail.com
 * @date 2023-02-17 21:28:00
 */
public interface PmsAttrService extends IService<PmsAttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(attrvo pmsAttr);


    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId,String type);

    attrresponsevo getattrInfo(Long attrId);

    void updateattr(attrvo pmsAttr);

    List<PmsAttrEntity> getRelation(Long attrgroupId);

    void deletRelation(AttrGroupRelationVo[] vos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    List<Long> selectSearchAttrs(List<Long> attrIds);

}

