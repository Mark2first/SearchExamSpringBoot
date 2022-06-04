package com.example.searchexamspringboot.Doc;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BlogData {
    private String url;
    private String id;
    private String title;
    private String createTime;
    private String content;
    private String read;
    private String comment;

    public BlogData(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public BlogData(String url, String id, String title, String createTime, String read, String comment) {
        this.url = url;
        this.id = id;
        this.title = title;
        this.createTime = createTime;
        this.read = read;
        this.comment = comment;
    }

    public BlogData(BlogData blogData) {
        this.url = url;
        this.id = id;
        this.title = title;
        this.createTime = createTime;
        this.content = content;
        this.read = read;
        this.comment = comment;
    }
}
