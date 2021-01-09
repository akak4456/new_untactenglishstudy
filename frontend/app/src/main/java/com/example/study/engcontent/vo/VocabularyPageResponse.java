package com.example.study.engcontent.vo;

import com.example.study.util.PageMaker;

public class VocabularyPageResponse {
    private PageMaker<VocaItem> page;
    private Long defaultVocaCount;

    public PageMaker<VocaItem> getPage() {
        return page;
    }

    public Long getDefaultVocaCount() {
        return defaultVocaCount;
    }
}
