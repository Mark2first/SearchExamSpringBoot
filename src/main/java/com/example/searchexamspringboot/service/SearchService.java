package com.example.searchexamspringboot.service;

import com.alibaba.fastjson.JSON;
import com.example.searchexamspringboot.Doc.BlogData;
import com.example.searchexamspringboot.Doc.BlogDataDoc;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    @Autowired
    private RestHighLevelClient client;

    /**
     * 单一条件搜索
     * @param Keyword
     * @return
     * @throws IOException
     */
    public BlogDataDoc searchSingle(String Keyword) throws IOException {
        SearchRequest request = new SearchRequest("blogdata");
        request.source().query(QueryBuilders.matchQuery("title",Keyword));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        // 解析响应
        SearchHits searchHits = response.getHits();

        // 获取总条数
        long total = searchHits.getTotalHits().value;
        BlogDataDoc blogDataDoc = new BlogDataDoc();
        blogDataDoc.setTotal(total);

        List<BlogData> blogDatas = new ArrayList<>();
        // 文章数组
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits){
            String json = hit.getSourceAsString();
            // 反序列化
            BlogData blogdata = JSON.parseObject(json, BlogData.class);
            blogDatas.add(new BlogData(blogdata.getUrl(),blogdata.getTitle(),blogdata.getId(),blogdata.getCreateTime(),blogdata.getComment(),blogdata.getRead()));
        }
        blogDataDoc.setBlogData(blogDatas);
        return blogDataDoc;
    }

    /**
     * 多行列搜索
     * @param Keyword
     * @return
     * @throws IOException
     */
    public BlogDataDoc searchMuilt(String Keyword) throws IOException {
        SearchRequest request = new SearchRequest("blogdata");
        request.source().query(QueryBuilders.multiMatchQuery(Keyword,"title","content"));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        // 解析响应
        SearchHits searchHits = response.getHits();

        // 获取总条数
        long total = searchHits.getTotalHits().value;
        BlogDataDoc blogDataDoc = new BlogDataDoc();
        blogDataDoc.setTotal(total);

        List<BlogData> blogDatas = new ArrayList<>();
        // 文章数组
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits){
            String json = hit.getSourceAsString();
            // 反序列化
            BlogData blogdata = JSON.parseObject(json, BlogData.class);
            blogDatas.add(new BlogData(blogdata.getUrl(),blogdata.getTitle(),blogdata.getId(),blogdata.getCreateTime(),blogdata.getComment(),blogdata.getRead()));
        }
        blogDataDoc.setBlogData(blogDatas);
        return blogDataDoc;
    }
}
