package com.example.guliMall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.guliMall.product.entity.PmsCategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author cc
 * @email ccsdsg2016@gmail.com
 * @date 2023-02-17 21:28:00
 */
public interface PmsCategoryService extends IService<PmsCategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PmsCategoryEntity> listWithTree();

}

