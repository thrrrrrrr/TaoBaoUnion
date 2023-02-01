package com.thr.taobaounion.model.domain;

import java.util.ArrayList;
import java.util.List;

public class SearchRecommend {

    private boolean success;
    private int code;
    private String message;
    private List<DataBean> data;

    public List<String> getList() {
        List<String> res = new ArrayList<>();
        for (DataBean d : getData()) {
            res.add(d.keyword);
        }
        return res;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1250359232734244864
         * keyword : iPhone
         * createTime : 2020-04-15 17:44
         */

        private String id;
        private String keyword;
        private String createTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
