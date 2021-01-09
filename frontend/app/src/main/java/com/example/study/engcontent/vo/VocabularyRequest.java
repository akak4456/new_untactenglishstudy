package com.example.study.engcontent.vo;

import java.util.ArrayList;

public class VocabularyRequest {
    private String title;
    private ArrayList<String> content;

    public VocabularyRequest(String title, ArrayList<String> content) {
        this.title = title;
        this.content = content;
    }
}
