package com.example.gulisearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.common.to.es.SkuEsModel;
import com.example.gulisearch.config.ElasticSearchConfig;
import com.example.gulisearch.constant.Esconstant;
import com.example.gulisearch.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cc
 * @date 2023年03月14日 17:01
 */
@Service
@Slf4j
public class productserviceImpl implements ProductService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        //1.在es中建立索引，建立号映射关系（doc/json/product-mapping.json）

        //2. 在ES中保存这些数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            //构造保存请求
            IndexRequest indexRequest = new IndexRequest(Esconstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String jsonString = JSON.toJSONString(skuEsModel);
            indexRequest.source(jsonString, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }


        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, ElasticSearchConfig.COMMON_OPTIONS);

        //TODO 如果批量错误
        boolean hasFailures = bulk.hasFailures();

        List<String> collect = Arrays.asList(bulk.getItems()).stream().map(item -> {
            return item.getId();
        }).collect(Collectors.toList());

        log.info("商品上架完成：{}",collect);

        return hasFailures;
//        保存到es
//        给es建立索引,建立映射关系
//        BulkRequest bulkRequest = new BulkRequest( );
//        for(SkuEsModel model:skuEsModels){
//            IndexRequest indexRequest = new IndexRequest(Esconstant.PRODUCT_INDEX);
//            indexRequest.id(model.getSkuId().toString());
//            String s = JSON.toJSONString(model);
//            indexRequest.source(s, XContentType.JSON);
//
//            bulkRequest.add(indexRequest);
//        }
//
//
////        给es中保存数据
//
//
//        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, ElasticSearchConfig.COMMON_OPTIONS);
////        TODO:如果返回false 上架出错 失败
//        boolean b = bulk.hasFailures();
//        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> {
//            return item.getId();
//        }).collect(Collectors.toList());
//
////        log.error(collect)
//
//        return b;


    }
}
