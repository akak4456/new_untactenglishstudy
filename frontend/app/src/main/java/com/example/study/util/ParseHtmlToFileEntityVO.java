package com.example.study.util;

import com.example.study.board.vo.FileEntityVO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseHtmlToFileEntityVO {
    public static List<FileEntityVO> parse(String htmlString){
        String imgRegex = "(?i)<img[^>]+?src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
        Pattern p = Pattern.compile(imgRegex);
        Matcher m = p.matcher(htmlString);
        List<FileEntityVO> ret = new ArrayList<>();
        while(m.find()) {
            String imgSrc = m.group(1);
            imgSrc = imgSrc.substring(imgSrc.lastIndexOf('/')+1);
            ret.add(new FileEntityVO(imgSrc));
        }
        return ret;
    }
}
