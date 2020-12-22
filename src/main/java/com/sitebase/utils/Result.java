package com.sitebase.utils;

import com.sitebase.constant.ResultType;

public class Result {

    private String message;
    private ResultType resultType;

    public Result(ResultType resultType) {
        this.resultType = resultType;
    }

    public Result(ResultType resultType, String message) {
        this.resultType = resultType;
        this.message = message;
    }

    public boolean isSuccess() {
        return resultType == ResultType.SUCCESS;
    }

    public boolean isERROR() {
        return resultType == ResultType.ERROR;
    }

    public String getMessage() {
        return this.message;
    }
}
