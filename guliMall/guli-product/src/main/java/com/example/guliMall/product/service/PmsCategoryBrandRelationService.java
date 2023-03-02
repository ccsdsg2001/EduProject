package com.example.guliMall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.guliMall.product.entity.PmsCategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author cc
 * @email ccsdsg2016@gmail.com
 * @date 2023-02-17 21:28:00
 */
public interface PmsCategoryBrandRelationService extends IService<PmsCategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void removeMenuByids(List<Long> asList);

    void saveDetail(PmsCategoryBrandRelationEntity pmsCategoryBrandRelation);

    void updateBrand(Long brandId, String name);

    void updateCategory(Long catId, String name);
}

