package com.example.gulisearch.controller;

import com.example.common.exception.CodeEnume;
import com.example.common.to.es.SkuEsModel;
import com.example.common.utils.R;
import com.example.gulisearch.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author cc
 * @date 2023年03月14日 16:58
 */
@RequestMapping("/search")
@RestController
public class ElasticSaveController {
    @Autowired
     ProductService productService;

    @PostMapping("/product")
    public R productStausup(@RequestBody List<SkuEsModel> skuEsModels){
        boolean b =false;
        try {
          b=  productService.productStatusUp(skuEsModels);
        } catch (IOException e) {
            return R.error(CodeEnume.PRODUCT_UP_EXCEPTION.getCode(),CodeEnume.PRODUCT_UP_EXCEPTION.getMsg());

        }

        if(b){
            return R.error(CodeEnume.PRODUCT_UP_EXCEPTION.getCode(),CodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
        }else {
            return R.ok();
        }


    }

}
