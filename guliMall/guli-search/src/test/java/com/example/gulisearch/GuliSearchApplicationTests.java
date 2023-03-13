package com.example.gulisearch;

import com.alibaba.fastjson.JSON;
import com.example.gulisearch.config.ElasticSearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class GuliSearchApplicationTests {

    @Autowired
    ElasticSearchConfig elasticSearchConfig;

    @Autowired
    private RestHighLevelClient client;

//    存储操作
    @Test
    void contextLoads() throws IOException {
        RestHighLevelClient restHighLevelClient = elasticSearchConfig.esRestClient();
//        .
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        User user = new User();
        user.setAge(13);
        user.setGender("nan");
        user.setUserName("cc");
        String string = JSON.toJSONString(user);
            indexRequest.source(string, XContentType.JSON);

            //执行操作
        IndexResponse index = client.index(indexRequest, ElasticSearchConfig.COMMON_OPTIONS);

        System.out.println(index);
    }


    @Data
    class User{
        private String userName;
        private String gender;
        private Integer age;
    }


//    elsearch 查找
    @Test
    public void search() throws IOException {
//        创建检索请求
        SearchRequest searchRequest = new SearchRequest();
//        指定索引
        searchRequest.indices("bank");
// 指定dsl，检索条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
//构造检索
        builder.query(QueryBuilders.matchQuery("address","mill"));
//        条件聚合
        TermsAggregationBuilder size = AggregationBuilders.terms("ageAgg").field("age").size(10);
        builder.aggregation(size);
//聚合 平均薪资
        AvgAggregationBuilder field = AggregationBuilders.avg("balanceAvg").field("balance");
        builder.aggregation(field);
//        builder.from();
//        builder.size();
//        builder.aggregation();
        searchRequest.source(builder);


        //执行搜索
        SearchResponse search = client.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
//获取 数值 转换成类
        SearchHits hits = search.getHits();
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit hit:hits1){
//            hit.getId();
            System.out.println(hit.getSourceAsMap());
            System.out.println(hit.getSourceAsString());
            System.out.println(hit.getIndex());
        }

//        获取聚合类型
        Aggregations aggregations = search.getAggregations();
        for (Aggregation aggregation : aggregations.asList()) {
            aggregation.getName();
        }
        System.out.println(search.toString());
    }

}
