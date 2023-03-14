package com.example.guliMall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.common.constant.ProductConstant;
import com.example.guliMall.product.dao.PmsAttrAttrgroupRelationDao;
import com.example.guliMall.product.dao.PmsAttrGroupDao;
import com.example.guliMall.product.dao.PmsCategoryDao;
import com.example.guliMall.product.entity.PmsAttrAttrgroupRelationEntity;
import com.example.guliMall.product.entity.PmsAttrGroupEntity;
import com.example.guliMall.product.entity.PmsCategoryEntity;
import com.example.guliMall.product.service.PmsCategoryService;
import com.example.guliMall.product.vo.AttrGroupRelationVo;
import com.example.guliMall.product.vo.attrresponsevo;
import com.example.guliMall.product.vo.attrvo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.guliMall.product.dao.PmsAttrDao;
import com.example.guliMall.product.entity.PmsAttrEntity;
import com.example.guliMall.product.service.PmsAttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsAttrService")
public class PmsAttrServiceImpl extends ServiceImpl<PmsAttrDao, PmsAttrEntity> implements PmsAttrService {

    @Autowired
    PmsAttrAttrgroupRelationDao relationDao;

    @Autowired
    PmsAttrGroupDao attrGroupDao;

    @Autowired
    PmsCategoryDao categoryDao;

    @Autowired
    PmsCategoryService categoryService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsAttrEntity> page = this.page(
                new Query<PmsAttrEntity>().getPage(params),
                new QueryWrapper<PmsAttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveAttr(attrvo Attr) {
        PmsAttrEntity pmsAttrEntity = new PmsAttrEntity();
        BeanUtils.copyProperties(Attr, pmsAttrEntity);

        this.save(pmsAttrEntity);
        if (Attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && Attr.getAttrGroupId()!=null) {
            PmsAttrAttrgroupRelationEntity RelationEntity = new PmsAttrAttrgroupRelationEntity();
            RelationEntity.setAttrGroupId(Attr.getAttrGroupId());
            RelationEntity.setAttrId(Attr.getAttrId());
            relationDao.insert(RelationEntity);

        }

        //保存关联关系


    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {
        QueryWrapper<PmsAttrEntity> wrapper = new QueryWrapper<PmsAttrEntity>().eq("attr_type", "base".equalsIgnoreCase(type) ? 1 : 0);
        if (catelogId != 0) {
            wrapper.eq("catelog_id", catelogId);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((wra) -> {
                wra.eq("attr_id", key).or().like("attr_name", key);

            });
        }
        IPage<PmsAttrEntity> page = this.page(
                new Query<PmsAttrEntity>().getPage(params),
                wrapper
        );

        PageUtils pageUtils = new PageUtils(page);
        List<PmsAttrEntity> records = page.getRecords();
        List<attrresponsevo> attr_id = records.stream().map((attrEntity) -> {
            attrresponsevo attrresponsevo = new attrresponsevo();
            BeanUtils.copyProperties(attrEntity, attrresponsevo);

            if ("base".equalsIgnoreCase(type)) {
                PmsAttrAttrgroupRelationEntity attrId = relationDao.selectOne(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                if (attrId != null && attrId.getAttrGroupId()!=null) {
                    PmsAttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrId.getAttrGroupId());
                    attrresponsevo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }

            PmsCategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrresponsevo.setCatelogName(categoryEntity.getName());
            }

            return attrresponsevo;

        }).collect(Collectors.toList());

        pageUtils.setList(attr_id);
        return pageUtils;
    }

    @Override
    public attrresponsevo getattrInfo(Long attrId) {
        attrresponsevo attrresponsevo = new attrresponsevo();

        PmsAttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity, attrresponsevo);
//设置分组信息
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            PmsAttrAttrgroupRelationEntity attr_id = relationDao.selectOne(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (attr_id != null) {
                attrresponsevo.setAttrGroupId(attr_id.getAttrGroupId());
                PmsAttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attr_id.getAttrGroupId());
                if (attrGroupEntity != null) {
                    attrresponsevo.setGroupName(attrGroupEntity.getAttrGroupName());
                }

            }
        }

        //设置分类信息
        Long catelogId = attrEntity.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        PmsCategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (categoryEntity != null) {
            attrresponsevo.setCatelogPath(catelogPath);
            attrresponsevo.setCatelogName(categoryEntity.getName());
        }

        return attrresponsevo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void updateattr(attrvo pmsAttr) {
        PmsAttrEntity pmsAttrEntity = new PmsAttrEntity();
        BeanUtils.copyProperties(pmsAttr, pmsAttrEntity);
        this.updateById(pmsAttrEntity);

        //修改分组关联
        if (pmsAttrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            PmsAttrAttrgroupRelationEntity relationEntity = new PmsAttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(pmsAttr.getAttrGroupId());
            relationEntity.setAttrId(pmsAttr.getAttrId());
            Integer attr_id = relationDao.selectCount(new UpdateWrapper<PmsAttrAttrgroupRelationEntity>().eq(
                    "attr_id", pmsAttr.getAttrId()
            ));


            if (attr_id > 0) {

                relationDao.update(relationEntity, new UpdateWrapper<PmsAttrAttrgroupRelationEntity>().eq(
                        "attr_id", pmsAttr.getAttrId()
                ));


            } else {
                relationDao.insert(relationEntity);
            }
        }

    }

    @Override
    public List<PmsAttrEntity> getRelation(Long attrgroupId) {
        List<PmsAttrAttrgroupRelationEntity> entities = relationDao.selectList(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        List<Long> attrids = entities.stream().map((attr) -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        if(attrids==null||attrids.size()==0){
            return null;
        }
        Collection<PmsAttrEntity> attrentites = this.listByIds(attrids);
        return (List<PmsAttrEntity>) attrentites;
    }

    @Override
    public void deletRelation(AttrGroupRelationVo[] vos) {
//        relationDao.delete(new QueryWrapper<>().eq("attr_id", 1L).eq())
        //relationDao.delete(new QueryWrapper<>().eq("attr_id",1L).eq("attr_group_id",1L));

        List<PmsAttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
            PmsAttrAttrgroupRelationEntity relationEntity = new PmsAttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());

        relationDao.deleteBatchRelation(entities);
    }

    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        //1、当前分组只能关联自己所属的分类里面的所有属性
        PmsAttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        //获取当前分类的id
        Long catelogId = attrGroupEntity.getCatelogId();

        //2、当前分组只能关联别的分组没有引用的属性
        //2.1）、当前分类下的其它分组
        List<PmsAttrGroupEntity> groupEntities = attrGroupDao.selectList(new QueryWrapper<PmsAttrGroupEntity>()
                .eq("catelog_id", catelogId));

        //获取到所有的attrGroupId
        List<Long> collect = groupEntities.stream().map((item) -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());


        //2.2）、这些分组关联的属性
        List<PmsAttrAttrgroupRelationEntity> groupId = relationDao.selectList
                (new QueryWrapper<PmsAttrAttrgroupRelationEntity>().in("attr_group_id", collect));

        List<Long> attrIds = groupId.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        //2.3）、从当前分类的所有属性移除这些属性
        QueryWrapper<PmsAttrEntity> queryWrapper = new QueryWrapper<PmsAttrEntity>()
                .eq("catelog_id", catelogId).eq("attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());

        if (attrIds != null && attrIds.size() > 0) {
            queryWrapper.notIn("attr_id", attrIds);
        }

        //判断是否有参数进行模糊查询
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((w) -> {
                w.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<PmsAttrEntity> page = this.page(new Query<PmsAttrEntity>().getPage(params), queryWrapper);

        PageUtils pageUtils = new PageUtils(page);


        return pageUtils;
    }

    @Override
    public List<Long> selectSearchAttrs(List<Long> attrIds) {



        return  baseMapper.selectSearchAttrs(attrIds);
    }
}