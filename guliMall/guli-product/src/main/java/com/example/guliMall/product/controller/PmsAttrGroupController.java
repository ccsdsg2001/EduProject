package com.example.guliMall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.guliMall.product.entity.PmsAttrEntity;
import com.example.guliMall.product.service.PmsAttrAttrgroupRelationService;
import com.example.guliMall.product.service.PmsAttrService;
import com.example.guliMall.product.service.PmsCategoryService;
import com.example.guliMall.product.vo.AttrGroupRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("product/attrgroup")
public class PmsAttrGroupController {
    @Autowired
    private PmsAttrGroupService pmsAttrGroupService;
    @Autowired
    private PmsCategoryService categoryService;

    @Autowired
    PmsAttrService attrService;

    @Autowired
    PmsAttrAttrgroupRelationService relationService;

    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo>  vos){
        relationService.saveBatch(vos);
        return R.ok();
    }





    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId){

//        PageUtils page = pmsAttrGroupService.queryPage(params);

        PageUtils page=pmsAttrGroupService.queryPage(params,catelogId);

        return R.ok().put("page", page);
    }


    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrealtion(@PathVariable("attrgroupId")Long attrgroupId){
      List<PmsAttrEntity> entities= attrService.getRelation(attrgroupId);
      return R.ok().put("data", entities);

    }

    @GetMapping(value = "/{attrgroupId}/noattr/relation")
    public R attrNoattrRelation(@RequestParam Map<String, Object> params,
                                @PathVariable("attrgroupId") Long attrgroupId) {

        // List<AttrEntity> entities = attrService.getRelationAttr(attrgroupId);

        PageUtils page = attrService.getNoRelationAttr(params,attrgroupId);

        return R.ok().put("page",page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		PmsAttrGroupEntity pmsAttrGroup = pmsAttrGroupService.getById(attrGroupId);

        Long catelogId = pmsAttrGroup.getCatelogId();
        Long[] path= categoryService.findCatelogPath(catelogId);
        pmsAttrGroup.setCatelogPath(path);

        return R.ok().put("attrGroup", pmsAttrGroup);
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
