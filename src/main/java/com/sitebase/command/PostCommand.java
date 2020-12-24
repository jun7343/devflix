package com.sitebase.command;

import com.sitebase.constant.ResultType;
import com.sitebase.utils.Result;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Getter
@ToString
public class PostCommand {

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
