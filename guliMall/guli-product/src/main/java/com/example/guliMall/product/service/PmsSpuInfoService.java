package com.example.guliMall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.guliMall.product.entity.PmsSpuInfoDescEntity;
import com.example.guliMall.product.entity.PmsSpuInfoEntity;
import com.example.guliMall.product.vo.SpuSaveVo;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * spu信息
 *
 * @author cc
 * @email ccsdsg2016@gmail.com
 * @date 2023-02-17 21:28:00
 */
@Service("skuInfoService")
public interface PmsSpuInfoService extends IService<PmsSpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo vo);

    void saveBaseSquInfo(PmsSpuInfoEntity infoEntity);

    void saveSpuInfoDesc(PmsSpuInfoDescEntity descEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);
}

