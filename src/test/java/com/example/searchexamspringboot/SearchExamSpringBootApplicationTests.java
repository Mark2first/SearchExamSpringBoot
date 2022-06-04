package com.example.searchexamspringboot;

import com.alibaba.fastjson.JSON;
import com.example.searchexamspringboot.Doc.BlogData;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootTest
class SearchExamSpringBootApplicationTests {
    private RestHighLevelClient client;

    @Test
    void Init(){
        System.out.println(client);
    }

    @BeforeEach
    void SetUp(){
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://localhost:9200")
        ));
    }

    @AfterEach
    void tearDown() throws IOException {
        this.client.close();
    }

    /**
     * 判断索引是否存在
     * @throws IOException
     */
    @Test
    void testExistIndex() throws IOException {
        // 1 创建Request对象
        GetIndexRequest request = new GetIndexRequest("blogdata");
        // 2 发起请求
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        // 3 输出
        System.out.println(exists);
    }

    /**
     * 批量导入数据
     */
    @Test
    void testSave() throws IOException {
        // 1 创建Request
        BulkRequest request = new BulkRequest();

        //TODO:这里的 new File() 可更改为存储的具体地址
        List<String> lines = FileUtils.readLines(
                        new File("/Users/caozheng/IdeaProjects/SearchExam/data.json"),"UTF-8");
        for(String line : lines){
            BlogData blogdata = JSON.parseObject(line, BlogData.class);
            // 2 准备参数
            request.add(new IndexRequest("blogdata").source(JSON.toJSONString(blogdata), XContentType.JSON));
        }
        // 3 发送请求
        client.bulk(request,RequestOptions.DEFAULT);
    }

    /**
     * 查询文档
     */
    @Test
    void getDocument() throws IOException {
        SearchRequest request = new SearchRequest("blogdata");
        request.source().query(QueryBuilders.matchAllQuery());
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        // 解析响应
        SearchHits searchHits = response.getHits();

        // 获取总条数
        long total = searchHits.getTotalHits().value;

        System.out.println("共搜索到"+total+"条数据");

        // 文章数组
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits){
            String json = hit.getSourceAsString();
            // 反序列化
            BlogData blogdata = JSON.parseObject(json, BlogData.class);
            System.out.println("blogdata = "+blogdata);
        }
    }

    /**
     * 联合查询
     * @throws IOException
     */
    @Test
    void getDocumentBySingle() throws IOException {
        String query = "用户";
        SearchRequest request = new SearchRequest("blogdata");
        request.source().query(QueryBuilders.multiMatchQuery(query,"title","content"));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        // 解析响应
        SearchHits searchHits = response.getHits();

        // 获取总条数
        long total = searchHits.getTotalHits().value;

        System.out.println("共搜索到"+total+"条数据");

        // 文章数组
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits){
            String json = hit.getSourceAsString();
            // 反序列化
            BlogData blogdata = JSON.parseObject(json, BlogData.class);
            System.out.println("标题 = "+blogdata.getTitle());
        }
    }
}
