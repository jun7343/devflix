package com.sitebase.dto;

import com.sitebase.constant.ResultType;
import com.sitebase.utils.Result;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class PostDto {

    private String title;
    private String content;
    
    public Result validate() {
        if (StringUtils.isEmpty(title)) {
            return new Result(ResultType.ERROR, "제목 기입해 주세요.");
        } else if (StringUtils.isEmpty(content)) {
            return new Result(ResultType.ERROR, "내용 기입해 주세요.");
        }

        return new Result(ResultType.SUCCESS);
    }
}
