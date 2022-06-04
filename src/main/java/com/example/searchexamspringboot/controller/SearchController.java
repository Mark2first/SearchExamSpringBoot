package com.example.searchexamspringboot.controller;

import com.example.searchexamspringboot.Doc.BlogData;
import com.example.searchexamspringboot.Doc.BlogDataDoc;
import com.example.searchexamspringboot.service.SearchService;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping("/hello")
    public String hello(){
        return "你好，很高兴认识你！";
    }

    /**
     * 单条件搜索
     * @param keyWord
     * @return
     * @throws IOException
     */
    @RequestMapping("SingleSearch")
    public BlogDataDoc searchSingle(@RequestParam("keyWord")String keyWord) throws IOException {
        return searchService.searchSingle(keyWord);
    }

    /**
     * 多条件搜索
     * @param keyWord
     * @return
     * @throws IOException
     */
    @RequestMapping("MutilSearch")
    public BlogDataDoc searchMutil(@RequestParam("keyWord")String keyWord) throws IOException {
        return searchService.searchMuilt(keyWord);
    }


}
