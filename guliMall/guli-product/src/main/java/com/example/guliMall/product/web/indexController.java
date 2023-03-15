package com.example.guliMall.product.web;

import com.example.guliMall.product.entity.PmsCategoryEntity;
import com.example.guliMall.product.service.PmsCategoryService;
import com.example.guliMall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author cc
 * @date 2023年03月15日 15:30
 */
@Controller
public class indexController {


    @Autowired
    PmsCategoryService categoryService;

    @GetMapping({"/","/index.html"})
    public String index(Model model){
   List<PmsCategoryEntity> categoryEntities=categoryService.getLevel1Cat();
   model.addAttribute("categorys", categoryEntities);
//视图解析器
        return "index";

    }

    @GetMapping("/index/catalog.json")
    @ResponseBody
    public Map<String, List<Catelog2Vo>> getCatelogJson(){
        Map<String, List<Catelog2Vo>>catalogJson  =categoryService.getCatalogJson();

        return catalogJson;
    }
}
