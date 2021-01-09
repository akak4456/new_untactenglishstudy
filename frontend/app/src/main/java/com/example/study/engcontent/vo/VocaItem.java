package com.example.study.engcontent.vo;

public class VocaItem {
    private Long vno;
    private String title;
    private Long cnt;

    public Long getVno(){
        return this.vno;
    }
    public String getTitle() {
        return title;
    }

    public Long getCount() {
        return cnt;
    }

    public VocaItem(Long vno,String title, Long cnt) {
        this.vno = vno;
        this.title = title;
        this.cnt = cnt;
    }
}
