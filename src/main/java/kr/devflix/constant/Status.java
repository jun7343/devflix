package kr.devflix.constant;

public enum Status {
    DELETE("삭제"), HIDDEN("숨김"), NOTICE("공지"), POST("게시");

    private String status;

    Status(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "status: " + status;
    }

    public String getStatus() {
        return status;
    }
}
