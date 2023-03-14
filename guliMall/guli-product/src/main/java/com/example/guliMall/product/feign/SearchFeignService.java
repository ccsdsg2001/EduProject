package com.example.guliMall.product.feign;

import com.example.common.to.es.SkuEsModel;
import com.example.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("guli-search")
public interface SearchFeignService {
    @PostMapping("/search/product")
     R productStausup(@RequestBody List<SkuEsModel> skuEsModels);
}
