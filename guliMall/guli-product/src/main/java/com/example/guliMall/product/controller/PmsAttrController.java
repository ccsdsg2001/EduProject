package com.example.guliMall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.guliMall.product.entity.ProductAttrValueEntity;
import com.example.guliMall.product.service.ProductAttrValueService;
import com.example.guliMall.product.vo.AttrGroupRelationVo;
import com.example.guliMall.product.vo.attrresponsevo;
import com.example.guliMall.product.vo.attrvo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.guliMall.product.entity.PmsAttrEntity;
import com.example.guliMall.product.service.PmsAttrService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 商品属性
 *
 * @author cc
 * @email ccsdsg2016@gmail.com
 * @date 2023-02-17 21:47:14
 */
@RestController
@RequestMapping("product/attr")
public class PmsAttrController {
    @Autowired
    private PmsAttrService pmsAttrService;


    @Autowired
    private ProductAttrValueService productAttrValueService;


    @GetMapping("/base/listforspu/{spuId}")
    public R baseAttrlist(@PathVariable("spuId")Long spuId){

        List<ProductAttrValueEntity> productAttrValueEntities = productAttrValueService.baseAttrListforspu(spuId);

        return R.ok().put("data", productAttrValueEntities);
    }

    @GetMapping("/{attrType}/list/{catelogId}")
    public R attrList(@RequestParam Map<String,Object> params,@PathVariable("attrType") String type,
                      @PathVariable("catelogId") Long catelogId){
        PageUtils page =pmsAttrService.queryBaseAttrPage(params,catelogId,type);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pmsAttrService.queryPage(params);

        return R.ok().put("page", page);
    }


    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] vos){
        pmsAttrService.deletRelation(vos);
        return R.ok();

    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
//		PmsAttrEntity pmsAttr = pmsAttrService.getById(attrId);

        attrresponsevo responVo=pmsAttrService.getattrInfo(attrId);
        return R.ok().put("attr", responVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody attrvo pmsAttr){
		pmsAttrService.saveAttr(pmsAttr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody attrvo pmsAttr){
		pmsAttrService.updateattr(pmsAttr);

        return R.ok();
    }

    @RequestMapping("/update/{spuId}")
    public R updatespuid(@PathVariable("spuId") Long spuId
    ,List<ProductAttrValueEntity> entities){
        productAttrValueService.updateSpuAttr(spuId,entities);
        return R.ok();
    }
    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		pmsAttrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
