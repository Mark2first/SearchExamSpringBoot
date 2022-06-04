package com.example.searchexamspringboot.Doc;

import lombok.Data;

import java.util.List;

@Data
public class BlogDataDoc {
    private long total;
    private List<BlogData> blogData;
}