package com.example.guliMall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.guliMall.product.entity.PmsCategoryEntity;
import com.example.guliMall.product.service.PmsCategoryService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 商品三级分类
 *
 * @author cc
 * @email ccsdsg2016@gmail.com
 * @date 2023-02-17 21:47:13
 */
@RestController
@RequestMapping("product/category")
public class PmsCategoryController {
    @Autowired
    private PmsCategoryService pmsCategoryService;

    /**
     * 列表  查出所有分类子分类 以树形结构组装起来
     */
    @RequestMapping("/list/tree")
    public R list( ){
        List<PmsCategoryEntity> entities = pmsCategoryService.listWithTree();

        return R.ok().put("data", entities);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId){
		PmsCategoryEntity pmsCategory = pmsCategoryService.getById(catId);

        return R.ok().put("data", pmsCategory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PmsCategoryEntity pmsCategory){
		pmsCategoryService.save(pmsCategory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PmsCategoryEntity pmsCategory){
		pmsCategoryService.updateById(pmsCategory);

        return R.ok();
    }

    @RequestMapping("/update/sort")
    public R updatesort(@RequestBody PmsCategoryEntity[] pmsCategory){
        pmsCategoryService.updateBatchById(Arrays.asList(pmsCategory));

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds){
		pmsCategoryService.removeByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
