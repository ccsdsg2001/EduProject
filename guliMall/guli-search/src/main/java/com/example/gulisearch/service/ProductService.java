package com.example.gulisearch.service;

import com.example.common.to.es.SkuEsModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


public interface ProductService {
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;

}
