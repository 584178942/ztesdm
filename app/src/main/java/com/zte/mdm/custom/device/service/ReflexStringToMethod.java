 package  com.zte.mdm.custom.device.service;

import android.content.Context;

import com.google.gson.Gson;
import com.zte.mdm.custom.device.SGTApplication;
import com.zte.mdm.custom.device.bean.SimBean;
import com.zte.mdm.custom.device.utils.LogUtils;
import com.zte.mdm.custom.device.utils.UpdateUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ga.mdm.PolicyManager;

import static com.zte.mdm.custom.device.util.AppConstants.INT_INT;
import static com.zte.mdm.custom.device.util.AppConstants.INT_STRING;

/**
 * 从字符串中映射出sdk里面的方法
 * @author Z T
 * @data 20200925
 */
public class ReflexStringToMethod {
    /**
     * 方法是否执行成功
      */
    private Boolean flag;
    public Object reflexToMethod(String PackageName, String className, String methodName, SimBean.DataBean datum, Context context) {
        switch (methodName) {
            case "updateApp":
                return reflexToMethodapp(className, methodName, datum, context);
            default:
                return ParameterTypeOne(className, methodName, datum);
        }
    }

    /**
     * 更新反射方法
     */
    public Object reflexToMethodapp(String className, String methodName, SimBean.DataBean datum, Context context) {
        boolean flag = true;
        try {
            //①通过类装载器获取Car类对象
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class clazz = loader.loadClass(className);

            //②获取类的默认构造器对象并通过它实例化Car
            Constructor cons = null;
            cons = clazz.getDeclaredConstructor((Class[]) null);
            UpdateUtils car = (UpdateUtils) cons.newInstance();
            Method setBrand = clazz.getMethod(methodName, Context.class);
            setBrand.invoke(car, context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
    //基本反射类
    public Object reflexToMethodMore(String className, String methodName, SimBean.DataBean datum) {
        try {
            Class<?> clazz = Class.forName(className);
            Object objClasss = SGTApplication.policyManager;
            Object obj = null;
            String parType = datum.getParaType();
            String[] parameter = datum.getParameter().split("\\@,");
            LogUtils.info("Parameter",new Gson().toJson(parameter));
            switch (parType) {
                case INT_STRING:

                    break;   
                case INT_INT:
                    Method method2 = clazz.getDeclaredMethod(methodName, int.class,int.class);
                    method2.setAccessible(true);
                    int p2 = 0  ;
                    if ("0x02".equals(parameter[1])){
                        p2 = 0x02;
                     }
                    // int
                    flag = (Boolean) method2.invoke(objClasss,  Integer.parseInt(parameter[0]),p2);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (flag == null) {
            flag = false;
        }
        return true;
    }
    //基本反射类
    public Object reflexToMethod(String className, String methodName, SimBean.DataBean datum) {
        try {
            Class<?> clazz = Class.forName(className);
            Object objClasss = SGTApplication.policyManager;
            if (datum.getParaType().equals("List")) {
                Object obj = null;
                obj = new ArrayList<String>();
                String[] list = datum.getParameter().split("\\,");
                for (int i = 0; i < list.length; i++) {
                    ((List) obj).add(list[i]);
                }
                Method method = clazz.getDeclaredMethod(methodName, List.class);
                method.setAccessible(true);
                flag = (Boolean) method.invoke(objClasss, obj);
            } else if (datum.getParaType().equals("boolean")) {
                Object obj = null;
                obj = datum.getParameter().equals("true");
                Method method = clazz.getDeclaredMethod(methodName, boolean.class);
                method.setAccessible(true);
                flag = (Boolean) method.invoke(objClasss, obj);
            } else if (datum.getParaType().equals("String")) {
                Object obj = datum.getParameter();
                Method method = clazz.getDeclaredMethod(methodName, String.class);
                method.setAccessible(true);
                flag = (Boolean) method.invoke(objClasss, obj);
            } else if (datum.getParaType().equals("int")) {
                Object obj = datum.getParameter();
                Method method = clazz.getDeclaredMethod(methodName, int.class);
                method.setAccessible(true);
                flag = (Boolean) method.invoke(objClasss, Integer.parseInt((String) obj));
                LogUtils.info("flag",flag + "");
            }  else {
                Method method = clazz.getDeclaredMethod(methodName);
                method.setAccessible(true);
                flag = (Boolean) method.invoke(objClasss);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (flag == null) {
            flag = false;
        }
        return true;
    }
    public Object  ParameterTypeOne(String className, String methodName, SimBean.DataBean datum){
        int parameterLength = datum.getParameter().split("\\@,").length;
        if (parameterLength == 1) {
            return reflexToMethod(className, methodName, datum);
        } else {
            // return true;
           return reflexToMethodMore(className, methodName, datum);
        }
    }

}
