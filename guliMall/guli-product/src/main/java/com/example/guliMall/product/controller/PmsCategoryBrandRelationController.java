package com.example.guliMall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.guliMall.product.entity.PmsBrandEntity;
import com.example.guliMall.product.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.guliMall.product.entity.PmsCategoryBrandRelationEntity;
import com.example.guliMall.product.service.PmsCategoryBrandRelationService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author cc
 * @email ccsdsg2016@gmail.com
 * @date 2023-02-17 21:28:00
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class PmsCategoryBrandRelationController {
    @Autowired
    private PmsCategoryBrandRelationService pmsCategoryBrandRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("product:pmscategorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pmsCategoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }

    //只处理请求接受和校验数据
    //service接受controller传来的数据 进行业务处理
    //controller 接受service处理书,封装页面指定vo
    @GetMapping("/brands/list")
    public R relationBrandList(@RequestParam(value = "catId",required = true) Long catId){

      List<PmsBrandEntity>  vos= pmsCategoryBrandRelationService.getBrandbycatID(catId);
        List<BrandVo> collect = vos.stream().map(item -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(item.getBrandId());
            brandVo.setBrandName(item.getName());
            return brandVo;
        }).collect(Collectors.toList());

        return R.ok().put("data", collect);


    }

    @GetMapping("/catelog/list")
    //獲取當前品牌關聯所有分類列表
//    @RequiresPermissions("product:pmscategorybrandrelation:list")
    public R listcatelog(@RequestParam("brandId") Long brandId){
        List<PmsCategoryBrandRelationEntity> data =pmsCategoryBrandRelationService.list(
                new QueryWrapper<PmsCategoryBrandRelationEntity>().eq("brand_id", brandId)
        );



        return R.ok().put("data", data);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("product:pmscategorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		PmsCategoryBrandRelationEntity pmsCategoryBrandRelation = pmsCategoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", pmsCategoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("product:pmscategorybrandrelation:save")
    public R save(@RequestBody PmsCategoryBrandRelationEntity pmsCategoryBrandRelation){



		pmsCategoryBrandRelationService.saveDetail(pmsCategoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:pmscategorybrandrelation:update")
    public R update(@RequestBody PmsCategoryBrandRelationEntity pmsCategoryBrandRelation){
		pmsCategoryBrandRelationService.updateById(pmsCategoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
//    @RequiresPermissions("product:pmscategorybrandrelation:delete")
    public R delete(@RequestBody Long[] catIds){

        //1检查删除菜单 是否引用
        pmsCategoryBrandRelationService.removeMenuByids(Arrays.asList(catIds));



        return R.ok();
    }

}
