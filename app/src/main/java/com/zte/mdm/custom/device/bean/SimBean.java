package com.zte.mdm.custom.device.bean;

import java.util.List;

public class SimBean {
    /**
     * state : 10002
     * message : 锁机锁通话
     * draw : 1397863754699411457
     * data : [{"reverseOperationId":"1397843408504000513","methodName":"lockDevice","orderNum":0,"updateTime":"2021-05-27T17:15:51","classAddress":"ga.mdm.PolicyManager","classification":"1","delFlag":"0","pkgAddress":"ga.mdm","configDescribe":"终端锁定","funcName":"终端锁定","callbackState":"锁定中|已锁定|激活中|run","paraGenericParadigm":"","createTime":"2021-05-27T17:13:08","brandId":"1372027020701671425","parameter":"","lockMsg":"","id":"1397843302295834625","state":"1","paraType":""}]
     * min : 30
     * max : 60
     * interestScreenmin : 14400
     * interestScreenmax : 28800
     * uninstallCert : false
     * cert : null
     */

    private int state;
    private String message;
    private String draw;
    private Object data;
    private int min;
    private int max;
    private int interestScreenmin;
    private int interestScreenmax;
    private boolean uninstallCert;
    private Object cert;
    private Object operationType;

    public Object getOperationType() {
        return operationType;
    }

    public void setOperationType(Object operationType) {
        this.operationType = operationType;
    }

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

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getInterestScreenmin() {
        return interestScreenmin;
    }

    public void setInterestScreenmin(int interestScreenmin) {
        this.interestScreenmin = interestScreenmin;
    }

    public int getInterestScreenmax() {
        return interestScreenmax;
    }

    public void setInterestScreenmax(int interestScreenmax) {
        this.interestScreenmax = interestScreenmax;
    }

    public boolean isUninstallCert() {
        return uninstallCert;
    }

    public void setUninstallCert(boolean uninstallCert) {
        this.uninstallCert = uninstallCert;
    }

    public Object getCert() {
        return cert;
    }

    public void setCert(Object cert) {
        this.cert = cert;
    }

    public static class DataBean {
        /**
         * reverseOperationId : 1397843408504000513
         * methodName : lockDevice
         * orderNum : 0
         * updateTime : 2021-05-27T17:15:51
         * classAddress : ga.mdm.PolicyManager
         * classification : 1
         * delFlag : 0
         * pkgAddress : ga.mdm
         * configDescribe : 终端锁定
         * funcName : 终端锁定
         * callbackState : 锁定中|已锁定|激活中|run
         * paraGenericParadigm :
         * createTime : 2021-05-27T17:13:08
         * brandId : 1372027020701671425
         * parameter :
         * lockMsg :
         * id : 1397843302295834625
         * state : 1
         * paraType :
         */

        private String reverseOperationId;
        private String methodName;
        private int orderNum;
        private String updateTime;
        private String classAddress;
        private String classification;
        private String delFlag;
        private String pkgAddress;
        private String configDescribe;
        private String funcName;
        private String callbackState;
        private String paraGenericParadigm;
        private String createTime;
        private String brandId;
        private String parameter;
        private String lockMsg;
        private String id;
        private String state;
        private String paraType;

        public String getReverseOperationId() {
            return reverseOperationId;
        }

        public void setReverseOperationId(String reverseOperationId) {
            this.reverseOperationId = reverseOperationId;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public int getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(int orderNum) {
            this.orderNum = orderNum;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getClassAddress() {
            return classAddress;
        }

        public void setClassAddress(String classAddress) {
            this.classAddress = classAddress;
        }

        public String getClassification() {
            return classification;
        }

        public void setClassification(String classification) {
            this.classification = classification;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public String getPkgAddress() {
            return pkgAddress;
        }

        public void setPkgAddress(String pkgAddress) {
            this.pkgAddress = pkgAddress;
        }

        public String getConfigDescribe() {
            return configDescribe;
        }

        public void setConfigDescribe(String configDescribe) {
            this.configDescribe = configDescribe;
        }

        public String getFuncName() {
            return funcName;
        }

        public void setFuncName(String funcName) {
            this.funcName = funcName;
        }

        public String getCallbackState() {
            return callbackState;
        }

        public void setCallbackState(String callbackState) {
            this.callbackState = callbackState;
        }

        public String getParaGenericParadigm() {
            return paraGenericParadigm;
        }

        public void setParaGenericParadigm(String paraGenericParadigm) {
            this.paraGenericParadigm = paraGenericParadigm;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }

        public String getParameter() {
            return parameter;
        }

        public void setParameter(String parameter) {
            this.parameter = parameter;
        }

        public String getLockMsg() {
            return lockMsg;
        }

        public void setLockMsg(String lockMsg) {
            this.lockMsg = lockMsg;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getParaType() {
            return paraType;
        }

        public void setParaType(String paraType) {
            this.paraType = paraType;
        }
    }/*

    *//**
     * state : 10002
     * message : 处理成功，有执行数据
     * draw : 222
     * data : [{"id":6,"page":1,"rows":10,"draw":null,"configDescribe":"测试锁机配置","pkgAddress":"com.test","classAddress":"com.test.pkg.andriod.Test1","methodName":"test1Main","completeMethodName":"com.test.pkg.andriod.Test1.test1Main2","categoryId":"1,2,4,8,10,11,12,","paraType":"List","paraGenericParadigm":"String","parameter":"null","type":"2","classification":"1","interfaceIds":"1","state":"1","callbackState":"success","createUser":1,"createTime":"2019-07-31T07:11:41.000+0000","updateUser":null,"updateTime":null},{"id":9,"page":1,"rows":10,"draw":null,"configDescribe":"新增网络白名单","pkgAddress":"com.pkg.hw","classAddress":"com.pkg.hw.android.Main1","methodName":"method01","completeMethodName":"com.pkg.hw.android.Main1.method01","categoryId":"0,1,2,3,4,5,6,7,8","paraType":"List","paraGenericParadigm":"String","parameter":"www.baidu.com,www.hao123.com","type":"2","classification":"1","interfaceIds":"","state":"1","callbackState":"success","createUser":1,"createTime":"2019-08-07T06:21:22.000+0000","updateUser":null,"updateTime":null}]
     *//*

    private int state;
    private String message;
    private String draw;
    private List<DataBean> data;
    private int min;
    private int max;
    private int interestScreenmax;
    private int interestScreenmin;
    *//**
     * 卸载证书(默认不卸载)
     *//*
    private Boolean uninstallCert;

    private String cert;

    public Boolean getUninstallCert() {
        return uninstallCert;
    }

    public void setUninstallCert(Boolean uninstallCert) {
        this.uninstallCert = uninstallCert;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public int getInterestScreenmax() {
        return interestScreenmax;
    }

    public void setInterestScreenmax(int interestScreenmax) {
        this.interestScreenmax = interestScreenmax;
    }

    public int getInterestScreenmin() {
        return interestScreenmin;
    }

    public void setInterestScreenmin(int interestScreenmin) {
        this.interestScreenmin = interestScreenmin;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

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

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        *//**
         * id : 6
         * page : 1
         * rows : 10
         * draw : null
         * configDescribe : 测试锁机配置
         * pkgAddress : com.test
         * classAddress : com.test.pkg.andriod.Test1
         * methodName : test1Main
         * completeMethodName : com.test.pkg.andriod.Test1.test1Main2
         * categoryId : 1,2,4,8,10,11,12,
         * paraType : List
         * paraGenericParadigm : String
         * parameter : null
         * type : 2
         * classification : 1
         * interfaceIds : 1
         * state : 1
         * callbackState : success
         * createUser : 1
         * createTime : 2019-07-31T07:11:41.000+0000
         * updateUser : null
         * updateTime : null
         *//*

        private int id;
        private int page;
        private int rows;
        private Object draw;
        private String configDescribe;
        private String pkgAddress;
        private String classAddress;
        private String methodName;
        private String completeMethodName;
        private String categoryId;
        private String paraType;
        private String paraGenericParadigm;
        private String parameter;
        private String type;
        private String classification;
        private String interfaceIds;
        private String state;
        private String callbackState;
        private int createUser;
        private String createTime;
        private Object updateUser;
        private Object updateTime;

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

        public String getConfigDescribe() {
            return configDescribe;
        }

        public void setConfigDescribe(String configDescribe) {
            this.configDescribe = configDescribe;
        }

        public String getPkgAddress() {
            return pkgAddress;
        }

        public void setPkgAddress(String pkgAddress) {
            this.pkgAddress = pkgAddress;
        }

        public String getClassAddress() {
            return classAddress;
        }

        public void setClassAddress(String classAddress) {
            this.classAddress = classAddress;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public String getCompleteMethodName() {
            return completeMethodName;
        }

        public void setCompleteMethodName(String completeMethodName) {
            this.completeMethodName = completeMethodName;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getParaType() {
            return paraType;
        }

        public void setParaType(String paraType) {
            this.paraType = paraType;
        }

        public String getParaGenericParadigm() {
            return paraGenericParadigm;
        }

        public void setParaGenericParadigm(String paraGenericParadigm) {
            this.paraGenericParadigm = paraGenericParadigm;
        }

        public String getParameter() {
            return parameter;
        }

        public void setParameter(String parameter) {
            this.parameter = parameter;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getClassification() {
            return classification;
        }

        public void setClassification(String classification) {
            this.classification = classification;
        }

        public String getInterfaceIds() {
            return interfaceIds;
        }

        public void setInterfaceIds(String interfaceIds) {
            this.interfaceIds = interfaceIds;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCallbackState() {
            return callbackState;
        }

        public void setCallbackState(String callbackState) {
            this.callbackState = callbackState;
        }

        public int getCreateUser() {
            return createUser;
        }

        public void setCreateUser(int createUser) {
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

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }
    }*/

}
