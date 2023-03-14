package com.example.guliMall.product.feign;

import com.example.common.to.SkuHasStockVo;
import com.example.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("guli-ware")
public interface WareFeignService {

    @PostMapping("/ware/waresku/hasstock")
    public R getskuStorge(@RequestBody List<Long> Skuids);
}
