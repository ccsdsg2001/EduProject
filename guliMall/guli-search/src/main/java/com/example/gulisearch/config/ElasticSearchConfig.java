package com.example.gulisearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cc
 * @date 2023年03月13日 18:06
 */
@Configuration
public class ElasticSearchConfig {
    public static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        COMMON_OPTIONS = builder.build();
    }


    @Bean
   public RestHighLevelClient esRestClient(){
       RestHighLevelClient client = new RestHighLevelClient(
               RestClient.builder(new HttpHost(
                       "192.168.115.131", 9200, "http"
               ))
       );
       return client;

   }


}
