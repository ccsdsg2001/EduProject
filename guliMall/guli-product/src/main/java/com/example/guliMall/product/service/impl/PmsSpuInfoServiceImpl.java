package com.example.guliMall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.example.common.constant.ProductConstant;
import com.example.common.to.SkuHasStockVo;
import com.example.common.to.SkuReductionTo;
import com.example.common.to.SpuBoundTo;
import com.example.common.to.es.SkuEsModel;
import com.example.common.utils.R;
import com.example.guliMall.product.entity.*;
import com.example.guliMall.product.feign.CouponFeignService;
import com.example.guliMall.product.feign.SearchFeignService;
import com.example.guliMall.product.feign.WareFeignService;
import com.example.guliMall.product.service.*;
import com.example.guliMall.product.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.guliMall.product.dao.PmsSpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsSpuInfoService")
public class PmsSpuInfoServiceImpl extends ServiceImpl<PmsSpuInfoDao, PmsSpuInfoEntity> implements PmsSpuInfoService {

    @Autowired
    PmsSpuInfoDescService spuInfoDescService;

    @Autowired
    PmsSpuImagesService spuImagesService;

    @Autowired
    PmsAttrService attrService;

    @Autowired
    ProductAttrValueService attrValueService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    PmsBrandService brandService;

    @Autowired
    PmsCategoryService categoryService;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    SearchFeignService searchFeignService;



    @Autowired
    SkuImagesService skuImagesService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsSpuInfoEntity> page = this.page(
                new Query<PmsSpuInfoEntity>().getPage(params),
                new QueryWrapper<PmsSpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveSpuInfo(SpuSaveVo vo) {
        //保存spu基本信息
        PmsSpuInfoEntity infoEntity = new PmsSpuInfoEntity();
        BeanUtils.copyProperties(vo, infoEntity);
        infoEntity.setCreateTime(new Date());
        infoEntity.setUpdateTime(new Date());
        this.saveBaseSquInfo(infoEntity);

        //保存spu描述图片
        List<String> decript = vo.getDecript();
        PmsSpuInfoDescEntity descEntity = new PmsSpuInfoDescEntity();
        descEntity.setSpuId(infoEntity.getId());
        descEntity.setDecript(String.join(",",decript));
        spuInfoDescService.saveSpuInfoDesc(descEntity);

        //保存spu图片集
        List<String> images = vo.getImages();
        spuImagesService.saveImages(infoEntity.getId(),images);


        //保存spu规格参数
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrId(attr.getAttrId());
            PmsAttrEntity id = attrService.getById(attr.getAttrId());
            productAttrValueEntity.setAttrName(id.getAttrName());
            productAttrValueEntity.setAttrValue(attr.getAttrValues());
            productAttrValueEntity.setQuickShow(attr.getShowDesc());
            productAttrValueEntity.setSpuId(infoEntity.getId());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        attrValueService.saveProductAttr(collect);

        List<Skus> skus = vo.getSkus();
        if(skus!=null&&skus.size()>0){
            skus.forEach(item->{
                String defaultImg="";
               for(Images image:item.getImages()){

                    if(image.getDefaultImg()==1){
                        defaultImg =image.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item,skuInfoEntity);
                skuInfoEntity.setBrandId(infoEntity.getBrandId());
                skuInfoEntity.setCatalogId(infoEntity.getCatalogId());
                skuInfoEntity.setSpuId(infoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(skuInfoEntity);
                Long skuId = skuInfoEntity.getSkuId();


                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity->{
                    //true 为要 false为过滤
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                skuImagesService.saveBatch(imagesEntities);

                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> collect1 = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity attrValue = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, attrValue);
                    attrValue.setSkuId(skuId);
                    return attrValue;
                }).collect(Collectors.toList());
                //TODO:无图片路径的无需保存
                skuSaleAttrValueService.saveBatch(collect1);


                Bounds bounds = vo.getBounds();
                SpuBoundTo spuBoundTo = new SpuBoundTo();
                BeanUtils.copyProperties(bounds,spuBoundTo);
                spuBoundTo.setSpuId(infoEntity.getId());

                R r = couponFeignService.saveSpuBounds(spuBoundTo);
                if(r.getCode()==0){
                    log.error("远程保存spu失败");
                }

                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item,skuReductionTo);
                if(skuReductionTo.getFullCount() >0|| skuReductionTo.getFullPrice().compareTo(new BigDecimal("0"))==1){

                }
                skuReductionTo.setSkuId(skuId);

                couponFeignService.saveSkuReduction(skuReductionTo);


            });
        }

    }

    @Override
    public void saveBaseSquInfo(PmsSpuInfoEntity infoEntity) {
        this.baseMapper.insert(infoEntity);

    }

    @Override
    public void saveSpuInfoDesc(PmsSpuInfoDescEntity descEntity) {

    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<PmsSpuInfoEntity> queryWrapper = new QueryWrapper<>();


        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((wrapper) -> {
                wrapper.eq("id",key).or().like("spu_name",key);
            });
        }

        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            queryWrapper.eq("publish_status",status);
        }

        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            queryWrapper.eq("brand_id",brandId);
        }

        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            queryWrapper.eq("catalog_id",catelogId);
        }

        IPage<PmsSpuInfoEntity> page = this.page(new Query<PmsSpuInfoEntity>().getPage(params), queryWrapper);

        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {
        //1、查出当前spuId对应的所有sku信息,品牌的名字
        List<SkuInfoEntity> skuInfoEntities = skuInfoService.getSkusBySpuId(spuId);

        //TODO 4、查出当前sku的所有可以被用来检索的规格属性
        List<ProductAttrValueEntity> baseAttrs = attrValueService.baseAttrListforspu(spuId);

        List<Long> attrIds = baseAttrs.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        List<Long> searchAttrIds = attrService.selectSearchAttrs(attrIds);
        //转换为Set集合
        Set<Long> idSet = searchAttrIds.stream().collect(Collectors.toSet());

        List<SkuEsModel.Attrs> attrsList = baseAttrs.stream().filter(item -> {
            return idSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs);
            return attrs;
        }).collect(Collectors.toList());



        List<Long> skuIdList = skuInfoEntities.stream()
                .map(SkuInfoEntity::getSkuId)
                .collect(Collectors.toList());
        //TODO 1、发送远程调用，库存系统查询是否有库存
        Map<Long, Boolean> stockMap = null;
        try {
            R skuHasStock = wareFeignService.getskuStorge(skuIdList);
            //TODO: nullpoint exception
            TypeReference<List<SkuHasStockVo>> typeReference = new TypeReference<List<SkuHasStockVo>>() {};
            stockMap = skuHasStock.getData(typeReference).stream()
                    .collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));

        } catch (Exception e) {
            log.error("库存服务查询异常：原因{}",e);
        }

        //2、封装每个sku的信息
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> collect = skuInfoEntities.stream().map(sku -> {
            //组装需要的数据
            SkuEsModel esModel = new SkuEsModel();
            esModel.setSkuPrice(sku.getPrice());
            esModel.setSkuImg(sku.getSkuDefaultImg());

            //设置库存信息
            if (finalStockMap == null) {
                esModel.setHasStock(true);
            } else {
                esModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }

            //TODO 2、热度评分。0
            esModel.setHotScore(0L);

            //TODO 3、查询品牌和分类的名字信息
            PmsBrandEntity brandEntity = brandService.getById(sku.getBrandId());
            esModel.setBrandName(brandEntity.getName());
            esModel.setBrandId(brandEntity.getBrandId());
            esModel.setBrandImg(brandEntity.getLogo());

            PmsCategoryEntity categoryEntity = categoryService.getById(sku.getCatalogId());
            esModel.setCatalogId(categoryEntity.getCatId());
            esModel.setCatalogName(categoryEntity.getName());

            //设置检索属性
            esModel.setAttrs(attrsList);

            BeanUtils.copyProperties(sku,esModel);

            return esModel;
        }).collect(Collectors.toList());

        //TODO 5、将数据发给es进行保存：gulimall-search
        R r = searchFeignService.productStausup(collect);

        if (r.getCode() == 0) {
            //远程调用成功
            //TODO 6、修改当前spu的状态
            this.baseMapper.updateSpuStatus(spuId, ProductConstant.ProductStatusEnum.SPU_UP.getCode());
        } else {
            //远程调用失败
            //TODO 7、重复调用？接口幂等性:重试机制
        }
    }
//            //组装数据
//            //查出对应spuid对应的sku信息名字
//            List<SkuInfoEntity> skus= skuInfoService.getSkusBySpuId(spuid);
//
//
//
//        List<ProductAttrValueEntity> baseattrs = attrValueService.baseAttrListforspu(spuid);
//        List<Long> attrIds = baseattrs.stream().map(attr -> {
//            return attr.getAttrId();
//        }).collect(Collectors.toList());
//
//        List<Long> searchattrIds=attrService.selectSearchAttrs(attrIds);
//        Set<Long> idSet=searchattrIds.stream().collect(Collectors.toSet());
//
//
//        List<SkuEsModel.Attrs> attrsList = baseattrs.stream().filter(item -> {
//            return idSet.contains(item.getAttrId());
//        }).map(item -> {
//            SkuEsModel.Attrs attrs1 = new SkuEsModel.Attrs();
//            BeanUtils.copyProperties(item, attrs1);
//            return attrs1;
//        }).collect(Collectors.toList());
//
//        List<Long> skuidlist = skus.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
//
//
//        Map<Long, Boolean> stockMap=null;
//
//        try {
//            R skuHasStock = wareFeignService.getskuStorge(skuidlist);
//            TypeReference<List<SkuHasStockVo>> typeReference = new TypeReference<List<SkuHasStockVo>>() {};
//            stockMap = skuHasStock.getData(typeReference).stream()
//                    .collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));
////            R<List<SkuHasStockVo>> listR = wareFeignService.getskuStorge(skuidlist);
////             stockMap = listR.getData().stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));
//        }catch (Exception e){
//            log.error("查询异常",e);
//        }
//
////        查库存
////TODO:NULL EXCEPTION
//        //封装每个slu信息
//        Map<Long, Boolean> finalStockMap = stockMap;
//        List<SkuEsModel> upProduct = skus.stream().map(sku -> {
//            SkuEsModel skuEsModel = new SkuEsModel();
//            BeanUtils.copyProperties(sku, skuEsModel);
//            //将es实体类 和 原来实体类进行处理
//            skuEsModel.setSkuPrice(sku.getPrice());
//            skuEsModel.setSkuImg(sku.getSkuDefaultImg());
////            远程调用 查询库存 是否有库存
//            if(finalStockMap ==null){
//                skuEsModel.setHasStock(true);
//            }else {
//                skuEsModel.setHasStock(finalStockMap.get((sku.getSkuId())));
//            }
//
//
//            //热度评分调用
//            skuEsModel.setHotScore(0L);
//
////            查询品牌和分类名字
//            PmsBrandEntity byId = brandService.getById(skuEsModel.getBrandId());
//            skuEsModel.setBrandName(byId.getName());
//            skuEsModel.setBrandImg(byId.getLogo());
//            PmsCategoryEntity category = categoryService.getById(skuEsModel.getCatalogId());
//            skuEsModel.setCatalogName(category.getName());
////设置检索属性
//            skuEsModel.setAttrs(attrsList);
//
//
//            return skuEsModel;
//        }).collect(Collectors.toList());
//
////        TODO 发送给es进行保存 guli-search
//        R r = searchFeignService.productStausup(upProduct);
//        if(r.getCode()==0){
////            TODO://修改spu状态
//            baseMapper.updateSpuStatus(spuid, ProductConstant.ProductStatusEnum.SPU_UP.getCode());
//        }else {
//            //失败
////            TODO://重复调用 接口幂等性
//        }
//
//    }

}