package com.sitebase.command;

import com.sitebase.constant.ResultType;
import com.sitebase.utils.Result;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class MemberCommand {

    private String username;
    private String password;
    private String name;

    public Result validate() {
        if (StringUtils.isEmpty(username)) {
            return new Result(ResultType.ERROR, "아이디를 기입해 주세요.");
        } else if (StringUtils.isEmpty(password)) {
            return new Result(ResultType.ERROR, "패스워드를 기입해 주세요.");
        } else if (StringUtils.isEmpty(name)) {
            return new Result(ResultType.ERROR, "유저 이름을 기입해 주세요.");
        }

        return new Result(ResultType.SUCCESS);
    }
}
