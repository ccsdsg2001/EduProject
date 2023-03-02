package com.example.guliMall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.example.common.validator.group.AddGroup;
import com.example.common.validator.group.UpdateGroup;
import com.example.common.validator.group.UpdateStatusGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.guliMall.product.entity.PmsBrandEntity;
import com.example.guliMall.product.service.PmsBrandService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;

import javax.validation.Valid;


/**
 * 品牌
 *
 * @author cc
 * @email ccsdsg2016@gmail.com
 * @date 2023-02-17 21:47:13
 */
@RestController
@RequestMapping("product/brand")
public class PmsBrandController {
    @Autowired
    private PmsBrandService pmsBrandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pmsBrandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
		PmsBrandEntity pmsBrand = pmsBrandService.getById(brandId);

        return R.ok().put("pmsBrand", pmsBrand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@Validated({AddGroup.class}) @RequestBody PmsBrandEntity pmsBrand, BindingResult result){
//        if(result.hasErrors()){
//            Map<String,String > map = new HashMap<>();
//            result.getFieldErrors().forEach((item)->{
//                String message = item.getDefaultMessage();
//                String field = item.getField();
//                map.put(field,message);
//            });
//
//            R.error(400,"数据不合法").put("data", map);
//        }else {
            pmsBrandService.save(pmsBrand);
//        }


        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@Validated(UpdateGroup.class) @RequestBody PmsBrandEntity pmsBrand){
		pmsBrandService.updateDetail(pmsBrand);

        return R.ok();
    }
//    @RequestMapping("/update")
//    public R updatestatus(@Validated(UpdateStatusGroup.class) @RequestBody PmsBrandEntity pmsBrand){
//		pmsBrandService.updateById(pmsBrand);
//
//        return R.ok();
//    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		pmsBrandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
