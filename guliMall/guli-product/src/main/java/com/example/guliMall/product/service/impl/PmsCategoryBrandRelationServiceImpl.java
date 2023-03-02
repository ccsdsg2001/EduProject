package com.example.guliMall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.guliMall.product.dao.PmsBrandDao;
import com.example.guliMall.product.dao.PmsCategoryDao;
import com.example.guliMall.product.entity.PmsBrandEntity;
import com.example.guliMall.product.entity.PmsCategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.guliMall.product.dao.PmsCategoryBrandRelationDao;
import com.example.guliMall.product.entity.PmsCategoryBrandRelationEntity;
import com.example.guliMall.product.service.PmsCategoryBrandRelationService;


@Service("pmsCategoryBrandRelationService")
public class PmsCategoryBrandRelationServiceImpl extends ServiceImpl<PmsCategoryBrandRelationDao, PmsCategoryBrandRelationEntity> implements PmsCategoryBrandRelationService {

    @Autowired
    PmsBrandDao brandDao;

    @Autowired
    PmsCategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsCategoryBrandRelationEntity> page = this.page(
                new Query<PmsCategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<PmsCategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void removeMenuByids(List<Long> asList) {
        //TODO check annotate
        //逻辑删除


        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public void saveDetail(PmsCategoryBrandRelationEntity pmsCategoryBrandRelation) {
        Long brandId = pmsCategoryBrandRelation.getBrandId();
        Long catelogId = pmsCategoryBrandRelation.getCatelogId();
        //1.查詢詳細名字
        PmsBrandEntity brandEntity = brandDao.selectById(brandId);
        PmsCategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        pmsCategoryBrandRelation.setBrandName(brandEntity.getName());
        pmsCategoryBrandRelation.setCatelogName(categoryEntity.getName());

        this.save(pmsCategoryBrandRelation);


    }

    @Override
    public void updateBrand(Long brandId, String name) {
        PmsCategoryBrandRelationEntity relationEntity = new PmsCategoryBrandRelationEntity();
        relationEntity.setBrandId(brandId);
        relationEntity.setBrandName(name);

        this.update(relationEntity,new UpdateWrapper<PmsCategoryBrandRelationEntity>().eq("brand_id", brandId));
    }

    @Override
    public void updateCategory(Long catId, String name) {
        this.baseMapper.updateCategory(catId,name);
    }

}