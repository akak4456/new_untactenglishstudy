package com.example.study.board.vo;

public class FileEntityVO {
    Long fileno;

    String path;

    public FileEntityVO(){

    }

    public FileEntityVO(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getFileno() {
        return fileno;
    }

    public void setFileno(Long fileno) {
        this.fileno = fileno;
    }
}
