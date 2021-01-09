package com.example.study.board.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class BoardVO implements Parcelable {
    Long bno;
    String title;
    String content;
    String kind;
    MemberWithBoardVO member;
    List<FileEntityVO> files;

    protected BoardVO(Parcel in) {
        if (in.readByte() == 0) {
            bno = null;
        } else {
            bno = in.readLong();
        }
        title = in.readString();
        content = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (bno == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(bno);
        }
        dest.writeString(title);
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BoardVO> CREATOR = new Creator<BoardVO>() {
        @Override
        public BoardVO createFromParcel(Parcel in) {
            return new BoardVO(in);
        }

        @Override
        public BoardVO[] newArray(int size) {
            return new BoardVO[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<FileEntityVO> getFiles() {
        return files;
    }

    public void setFiles(List<FileEntityVO> files) {
        this.files = files;
    }

    public BoardVO(String title, String content, List<FileEntityVO> files) {
        this.title = title;
        this.content = content;
        this.files = files;
    }

    public BoardVO(String title,String content, List<FileEntityVO> files,String kind){
        this(title,content,files);
        this.kind = kind;
    }

    public MemberWithBoardVO getMember() {
        return member;
    }

    public void setMember(MemberWithBoardVO member) {
        this.member = member;
    }

    public Long getBno() {
        return bno;
    }

    public void setBno(Long bno) {
        this.bno = bno;
    }
}
