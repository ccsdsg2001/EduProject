package com.example.guliMall.product.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.guliMall.product.entity.PmsAttrGroupEntity;
import com.example.guliMall.product.service.PmsAttrGroupService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 属性分组
 *
 * @author cc
 * @email ccsdsg2016@gmail.com
 * @date 2023-02-17 21:47:13
 */
@RestController
@RequestMapping("product/pmsattrgroup")
public class PmsAttrGroupController {
    @Autowired
    private PmsAttrGroupService pmsAttrGroupService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pmsAttrGroupService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		PmsAttrGroupEntity pmsAttrGroup = pmsAttrGroupService.getById(attrGroupId);

        return R.ok().put("pmsAttrGroup", pmsAttrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PmsAttrGroupEntity pmsAttrGroup){
		pmsAttrGroupService.save(pmsAttrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PmsAttrGroupEntity pmsAttrGroup){
		pmsAttrGroupService.updateById(pmsAttrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		pmsAttrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
