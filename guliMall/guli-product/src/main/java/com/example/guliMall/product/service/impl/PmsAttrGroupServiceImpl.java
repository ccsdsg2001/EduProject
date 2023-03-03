package com.example.guliMall.product.service.impl;

import com.example.guliMall.product.entity.PmsAttrEntity;
import com.example.guliMall.product.entity.PmsCategoryEntity;
import com.example.guliMall.product.service.PmsAttrService;
import com.example.guliMall.product.vo.AttrGroupWithAttrsVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.guliMall.product.dao.PmsAttrGroupDao;
import com.example.guliMall.product.entity.PmsAttrGroupEntity;
import com.example.guliMall.product.service.PmsAttrGroupService;


@Service("pmsAttrGroupService")
public class PmsAttrGroupServiceImpl extends ServiceImpl<PmsAttrGroupDao, PmsAttrGroupEntity> implements PmsAttrGroupService {

    @Autowired
    PmsAttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsAttrGroupEntity> page = this.page(
                new Query<PmsAttrGroupEntity>().getPage(params),
                new QueryWrapper<PmsAttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        String key = (String) params.get("key");
        QueryWrapper<PmsAttrGroupEntity> wrapper = new QueryWrapper<PmsAttrGroupEntity>();
        if(!StringUtils.isEmpty(key)){
            wrapper.and((obj)->{
                obj.eq("arrt_group_id", key).or().eq("attr_group_name", key);
            });
        }
        if(catelogId==0){
            IPage<PmsAttrGroupEntity> page = this.page(new Query<PmsAttrGroupEntity>().getPage(params)
                    , new QueryWrapper<PmsAttrGroupEntity>());

            return new PageUtils(page);
        }else {

            wrapper.eq("catelog_id", catelogId);
            IPage<PmsAttrGroupEntity> page = this.page(new Query<PmsAttrGroupEntity>().getPage(params), wrapper);
            return  new PageUtils(page);
        }

    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCateLogId(Long catelogId) {

        //查询分组信息
        List<PmsAttrGroupEntity> catelogId1 = this.list(new QueryWrapper<PmsAttrGroupEntity>().eq("catelog_id", catelogId));

        List<AttrGroupWithAttrsVo> collect = catelogId1.stream().map(group -> {
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(group,attrGroupWithAttrsVo);
            List<PmsAttrEntity> attrs = attrService.getRelation(attrGroupWithAttrsVo.getAttrGroupId());
            attrGroupWithAttrsVo.setAttrs(attrs);
            return attrGroupWithAttrsVo;
        }).collect(Collectors.toList());


        return collect;

    }

    //找到catelogid得完整路径 父子孙


}