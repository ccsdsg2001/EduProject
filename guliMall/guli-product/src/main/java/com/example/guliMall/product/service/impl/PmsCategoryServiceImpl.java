package com.example.guliMall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.guliMall.product.dao.PmsCategoryDao;
import com.example.guliMall.product.entity.PmsCategoryEntity;
import com.example.guliMall.product.service.PmsCategoryService;


@Service("pmsCategoryService")
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryDao, PmsCategoryEntity> implements PmsCategoryService {




    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsCategoryEntity> page = this.page(
                new Query<PmsCategoryEntity>().getPage(params),
                new QueryWrapper<PmsCategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<PmsCategoryEntity> listWithTree() {
//1.查出所有分类
        List<PmsCategoryEntity> entities = baseMapper.selectList(null);
//2.组装父子树形结构 《1》找到一级分类
        List<PmsCategoryEntity> collect = entities.stream().filter((categoryEntity) -> {
            return categoryEntity.getParentCid() == 0;
        }).map((menu)->{
            menu.setChildren(getchildrens(menu,entities));
            return menu;
        }).sorted((menu1,menu2)->{
            return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());

        }).collect(Collectors.toList());


        return collect;
    }

    //查找所有菜单子菜单
    private List<PmsCategoryEntity> getchildrens(PmsCategoryEntity root,List<PmsCategoryEntity> all){
        List<PmsCategoryEntity> collect = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(categoryEntity -> {
            //找到子菜单
            categoryEntity.setChildren(getchildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
//            2菜单排序
            return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());
        return collect;
    }

}
/*
 * @Author cc
 * @Description //TODO
 * @Date 23:33 2023/2/21
 * @param null
 * @return null
 stream() − 为集合创建串行流。

parallelStream() − 为集合创建并行流。

1.过滤filter(T->boolean),保留boolean为true的元素。

list.stream().filter(user->user.getAge()>20).collect(Collectors.toList())

  保留年龄大于20的用户数据流，再将其转换为List。

2.去重distinct(),去除重复元素，这个方法是通过类的equals方法来判断两个元素是否相等，需要对原有元素重写equals方法。

list.strean().distinct().collect(Collectors.toList());

3.排序sorted()/sorted((T,T)->int),如果流中的元素的类实现了Comparable接口，直接可以排序，否则可以在sorted()中加入排序规则。

list.strean().sorted().collect(Collectors.toList());

4.截取前n个元素limit(long n)

list.stream().limit(5).collect(Collectors.toList());

  截取前5个元素。

5.去除前n个元素skip(long n)

list.stream().skip(2);

去除前两个元素。

6.将T映射为R,map(T->R)
  在map中可以对每个元素进行操作;类似遍历对元素操作。

String department="技术部";
  list
     .stream()
     .map(user->
    User newUser=new User();
    newUser.setName(user.getName());
        newUser.setDepartment(department);
    newUser.setAge(user.getAge());
    return newUser;
      )
     .collect(Collectors.toList());

   可以将元素的某个元素提取为一个新的集合。

7.将多个流合并为一个流 flatMap()
8.是否存在给定的条件T->boolean,anyMatch(T->boolean)

boolean isAt =list.stream().anyMatch(user -> user.getAge() == 20);

流中不存在元素给定的T->boolean
  findAny():找到其中一个元素（使用Stream时找到的是第一个元素;使用parallelStream()并行时找到的是其中一个元素）
  findFirst():找到第一个元素
9.组合流中的元素，求和，求积，最大值等

  reduce((T,T)->T),reduce(T(T,T)->T)
  //计算年龄总和：
  int sum = list.stream().map(Person::getAge).reduce(0, (a, b) -> a + b);
  //与之相同:
  int sum = list.stream().map(Person::getAge).reduce(0, Integer::sum);

  0代表起始值为0,如果求积只需要将+改为*;


10.收集方法collect()
  list.stream().collect(toList())，list.stream().collect(toSet()) ，       list.strean().collect(Collectors.toMap(User::getID,User::getName))，，，
  还可以分组求和等

Map<String, Integer> collect2 = list
    .stream()
    .collect(
        Collectors.groupingBy(
            list::getMethed1,
            Collectors.summingInt(T->Integer.parseInt(list.getMethed2()))
            )
        );

 根据Methed1分组，根据Methed2求和。

11.遍历

list.strean().forEach();

12.返回流中元素个数count
   一般与过滤一起使用，计算符条件流中的元素个数。

13.统计，mapToInt

List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
IntSummaryStatistics stats = numbers.stream().mapToInt((x) -> x).summaryStatistics(); System.out.println("列表中最大的数 : " + stats.getMax());
System.out.println("列表中最小的数 : " + stats.getMin());
System.out.println("所有数之和 : " + stats.getSum());
System.out.println("平均数 : " + stats.getAverage());

14.遍历树形结构

/**
* 获取父节点
* @param trees
* @return List
*/
//public static List<PermissionVo> makeTree(List<PermissionVo> trees) {
//    if (!CollectionUtils.isEmpty(trees)) {
//        return trees.stream().filter(m -> m.getParentId() == 0).peek(
//                (m) -> m.setChildren(getChildrenList(m, trees))
//        ).collect(Collectors.toList());
//    }
//    return null;
//}

    /**
//     * 获取子节点列表
//     * @param tree
//     * @param trees
//     * @return List
//     */
//    private static List<PermissionVo> getChildrenList(PermissionVo tree, List<PermissionVo> trees) {
//        List<PermissionVo> collect = trees.stream().filter(item -> Objects.equals(item.getParentId(), tree.getId())).peek(
//                (item) -> item.setChildren(getChildrenList(item, trees))
//        ).collect(Collectors.toList());
//        if(!CollectionUtils.isEmpty(collect)){
//            return collect;
//        }
//        return null;
// */
// **/
//*/