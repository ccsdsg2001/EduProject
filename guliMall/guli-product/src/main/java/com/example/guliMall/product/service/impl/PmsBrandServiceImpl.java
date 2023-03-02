package com.example.guliMall.product.service.impl;

import com.example.guliMall.product.service.PmsCategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.guliMall.product.dao.PmsBrandDao;
import com.example.guliMall.product.entity.PmsBrandEntity;
import com.example.guliMall.product.service.PmsBrandService;


@Service("pmsBrandService")
public class PmsBrandServiceImpl extends ServiceImpl<PmsBrandDao, PmsBrandEntity> implements PmsBrandService {

    @Autowired
    PmsCategoryBrandRelationService categoryBrandRelationService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
//        1.获取key
        String key = (String) params.get("key");
        QueryWrapper<PmsBrandEntity> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(key)){
            queryWrapper.eq("brand_id", key).or().like("name", key);
        }


        IPage<PmsBrandEntity> page = this.page(
                new Query<PmsBrandEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void updateDetail(PmsBrandEntity pmsBrand) {
        //保證數據字段一致
        this.updateById(pmsBrand);

        if(!StringUtils.isEmpty(pmsBrand.getName())){
            //同步其他數據
            categoryBrandRelationService.updateBrand(pmsBrand.getBrandId(),pmsBrand.getName());
        }
    }

}