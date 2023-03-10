package com.example.guliMall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;
import com.example.common.utils.R;
import com.example.guliMall.product.dao.SkuInfoDao;
import com.example.guliMall.product.entity.PmsSpuInfoDescEntity;
import com.example.guliMall.product.entity.SkuImagesEntity;
import com.example.guliMall.product.entity.SkuInfoEntity;
import com.example.guliMall.product.feign.SeckillFeignService;
import com.example.guliMall.product.service.*;
import com.example.guliMall.product.vo.SeckillSkuVo;
import com.example.guliMall.product.vo.SkuItemSaleAttrVo;
import com.example.guliMall.product.vo.SkuItemVo;
import com.example.guliMall.product.vo.SpuItemAttrGroupVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Resource
    private SkuImagesService skuImagesService;

    @Resource
    private PmsSpuInfoDescService spuInfoDescService;

    @Resource
    private PmsAttrGroupService attrGroupService;

    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private SeckillFeignService seckillFeignService;
//
//    @Resource
//    private ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageCondition(Map<String, Object> params) {

        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key) && !"0".equalsIgnoreCase(key)) {
            queryWrapper.and((wrapper) -> {
                wrapper.eq("sku_id", key).or().like("sku_name", key);
            });
        }

        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            queryWrapper.eq("catalog_id", catelogId);
        }

        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            queryWrapper.eq("brand_id", brandId);
        }

        String min = (String) params.get("min");
        if (!StringUtils.isEmpty(min)) {
            queryWrapper.ge("price", min);
        }

        String max = (String) params.get("max");

        if (!StringUtils.isEmpty(max)) {
            try {
                BigDecimal bigDecimal = new BigDecimal(max);
                if (bigDecimal.compareTo(BigDecimal.ZERO) == 1) {
                    queryWrapper.le("price", max);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // key:
        // catelogId: 225
        // brandId: 9
        // min: 0
        // max: 0

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {

        List<SkuInfoEntity> skuInfoEntities = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));

        return skuInfoEntities;
    }


//    @Override
//    public SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException {

//        SkuItemVo skuItemVo = new SkuItemVo();
//
//        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
//            //1???sku?????????????????????  pms_sku_info
//            SkuInfoEntity info = this.getById(skuId);
//            skuItemVo.setInfo(info);
//            return info;
//        }, executor);
//
//
//        CompletableFuture<Void> saleAttrFuture = infoFuture.thenAcceptAsync((res) -> {
//            //3?????????spu?????????????????????
//            List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSaleAttrBySpuId(res.getSpuId());
//            skuItemVo.setSaleAttr(saleAttrVos);
//        }, executor);
//
//
//        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync((res) -> {
//            //4?????????spu?????????    pms_spu_info_desc
//            PmsSpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(res.getSpuId());
//            skuItemVo.setDesc(spuInfoDescEntity);
//        }, executor);
//
//
//        CompletableFuture<Void> baseAttrFuture = infoFuture.thenAcceptAsync((res) -> {
//            //5?????????spu?????????????????????
//            List<SpuItemAttrGroupVo> attrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(res.getSpuId(), res.getCatalogId());
//            skuItemVo.setGroupAttrs(attrGroupVos);
//        }, executor);
//
//
//        // Long spuId = info.getSpuId();
//        // Long catalogId = info.getCatalogId();
//
//        //2???sku???????????????    pms_sku_images
//        CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
//            List<SkuImagesEntity> imagesEntities = skuImagesService.getImagesBySkuId(skuId);
//            skuItemVo.setImages(imagesEntities);
//        }, executor);
//
//        CompletableFuture<Void> seckillFuture = CompletableFuture.runAsync(() -> {
//            //3???????????????????????????sku??????????????????????????????
//            R skuSeckilInfo = seckillFeignService.getSkuSeckilInfo(skuId);
//            if (skuSeckilInfo.getCode() == 0) {
//                //????????????
//                SeckillSkuVo seckilInfoData = skuSeckilInfo.getData("data", new TypeReference<SeckillSkuVo>() {
//                });
//                skuItemVo.setSeckillSkuVo(seckilInfoData);
//
//                if (seckilInfoData != null) {
//                    long currentTime = System.currentTimeMillis();
//                    if (currentTime > seckilInfoData.getEndTime()) {
//                        skuItemVo.setSeckillSkuVo(null);
//                    }
//                }
//            }
//        }, executor);
//
//
//        //???????????????????????????
//        CompletableFuture.allOf(saleAttrFuture,descFuture,baseAttrFuture,imageFuture,seckillFuture).get();
//
//        return skuItemVo;

//    }

}