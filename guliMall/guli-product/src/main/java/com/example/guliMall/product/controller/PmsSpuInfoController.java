package com.example.guliMall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.example.guliMall.product.vo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.guliMall.product.entity.PmsSpuInfoEntity;
import com.example.guliMall.product.service.PmsSpuInfoService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * spu信息
 *
 * @author cc
 * @email ccsdsg2016@gmail.com
 * @date 2023-02-17 21:47:13
 */
@RestController
@RequestMapping("product/spuinfo")
public class PmsSpuInfoController {
    @Autowired
    private PmsSpuInfoService pmsSpuInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pmsSpuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PmsSpuInfoEntity pmsSpuInfo = pmsSpuInfoService.getById(id);

        return R.ok().put("spuInfo", pmsSpuInfo);
    }

    @PostMapping("/{spuId}/up")
    public  R spuUp(@PathVariable("spuId") Long spuid ){
        pmsSpuInfoService.up(spuid);

        return R.ok();
    }
    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SpuSaveVo vo){
//		pmsSpuInfoService.save(pmsSpuInfo);

        pmsSpuInfoService.saveSpuInfo(vo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PmsSpuInfoEntity pmsSpuInfo){
		pmsSpuInfoService.updateById(pmsSpuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		pmsSpuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
