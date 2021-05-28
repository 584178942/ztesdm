package com.zte.mdm.custom.device.bean;

public class  UpdateBean {
    /**
     * state : 10002
     * message : 处理成功，有执行数据
     * draw : null
        */

    private int state;
    private String message;
    private Object draw;
    private DataBean data;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDraw() {
        return draw;
    }

    public void setDraw(Object draw) {
        this.draw = draw;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * page : 1
         * rows : 10
         * draw : null
         * version : 1.0.0.1
         * url : baidu.com
         * categroyIds : 12,15,17,1
         * state : 1
         * remarks : test01
         * createUser : null
         * createTime : 2019-08-17T06:23:06.000+0000
         * updateUser : null
         * updateTime : 2019-08-17T08:29:13.000+0000
         */

        private int id;
        private int page;
        private int rows;
        private Object draw;
        private String version;
        private String url;
        private String categroyIds;
        private String state;
        private String remarks;
        private Object createUser;
        private String createTime;
        private Object updateUser;
        private String updateTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        public Object getDraw() {
            return draw;
        }

        public void setDraw(Object draw) {
            this.draw = draw;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCategroyIds() {
            return categroyIds;
        }

        public void setCategroyIds(String categroyIds) {
            this.categroyIds = categroyIds;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public Object getCreateUser() {
            return createUser;
        }

        public void setCreateUser(Object createUser) {
            this.createUser = createUser;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(Object updateUser) {
            this.updateUser = updateUser;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}
