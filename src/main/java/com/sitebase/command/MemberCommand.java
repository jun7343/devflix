package com.sitebase.command;

import com.sitebase.constant.ResultType;
import com.sitebase.utils.Result;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Getter
@Setter
@ToString
public class MemberCommand {

    private String id;
    private String password;
    private String name;

    public Result validate() {
        if (StringUtils.isEmpty(getId())) {
            return new Result(ResultType.ERROR, "아이디를 기입해 주세요.");
        } else if (StringUtils.isEmpty(getPassword())) {
            return new Result(ResultType.ERROR, "패스워드를 기입해 주세요.");
        } else if (StringUtils.isEmpty(getName())) {
            return new Result(ResultType.ERROR, "유저 이름을 기입해 주세요.");
        }

        return new Result(ResultType.SUCCESS);
    }
}
