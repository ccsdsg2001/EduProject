package com.example.guliMall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.guliMall.product.entity.PmsBrandEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 品牌
 *
 * @author cc
 * @email ccsdsg2016@gmail.com
 * @date 2023-02-17 21:28:00
 */

public interface PmsBrandService extends IService<PmsBrandEntity> {



    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(PmsBrandEntity pmsBrand);
}

