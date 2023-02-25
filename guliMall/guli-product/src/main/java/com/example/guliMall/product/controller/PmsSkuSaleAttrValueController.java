//package com.example.guliMall.product.controller;
//
//import java.util.Arrays;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.guliMall.product.entity.PmsSkuSaleAttrValueEntity;
//import com.example.guliMall.product.service.PmsSkuSaleAttrValueService;
//import com.example.common.utils.PageUtils;
//import com.example.common.utils.R;
//
//
//
///**
// * sku销售属性&值
// *
// * @author cc
// * @email ccsdsg2016@gmail.com
// * @date 2023-02-17 21:47:14
// */
//@RestController
//@RequestMapping("product/pmsskusaleattrvalue")
//public class PmsSkuSaleAttrValueController {
//    @Autowired
//    private PmsSkuSaleAttrValueService pmsSkuSaleAttrValueService;
//
//    /**
//     * 列表
//     */
//    @RequestMapping("/list")
//    public R list(@RequestParam Map<String, Object> params){
//        PageUtils page = pmsSkuSaleAttrValueService.queryPage(params);
//
//        return R.ok().put("page", page);
//    }
//
//
//    /**
//     * 信息
//     */
//    @RequestMapping("/info/{id}")
//    public R info(@PathVariable("id") Long id){
//		PmsSkuSaleAttrValueEntity pmsSkuSaleAttrValue = pmsSkuSaleAttrValueService.getById(id);
//
//        return R.ok().put("pmsSkuSaleAttrValue", pmsSkuSaleAttrValue);
//    }
//
//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//    public R save(@RequestBody PmsSkuSaleAttrValueEntity pmsSkuSaleAttrValue){
//		pmsSkuSaleAttrValueService.save(pmsSkuSaleAttrValue);
//
//        return R.ok();
//    }
//
//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    public R update(@RequestBody PmsSkuSaleAttrValueEntity pmsSkuSaleAttrValue){
//		pmsSkuSaleAttrValueService.updateById(pmsSkuSaleAttrValue);
//
//        return R.ok();
//    }
//
//    /**
//     * 删除
//     */
//    @RequestMapping("/delete")
//    public R delete(@RequestBody Long[] ids){
//		pmsSkuSaleAttrValueService.removeByIds(Arrays.asList(ids));
//
//        return R.ok();
//    }
//
//}
