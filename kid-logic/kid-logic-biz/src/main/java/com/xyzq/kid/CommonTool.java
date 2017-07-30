package com.xyzq.kid;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 范例工具类
 *
 * 本目录用于存放通用工具类、抽奖接口等
 */
public class CommonTool {

    public final static int STATUS_NORMAL = 0;//记录正常
    public final static int STATUS_DELETE = 1;//记录删除

    /**
     * 隐藏构造函数
     */
    private CommonTool() { }

    /**
     * 方法描述 Date转化为string
     *
     * @return 返回值 年-月-日
     */
    public static String DataToStringYMD(Date date) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 方法描述 Date转化为string
     *
     * @return 返回值 年-月-日 时:分:秒
     */
    public static String DataToStringYMDHMS(Date date) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 方法描述 Date转化为string
     *
     * @return 返回值 年-月-日 时:分:秒
     */
    public static Date StringToDataYMDHMS(String dateString) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 方法描述 Date转化为string
     *
     * @return 返回值 年-月-日
     */
    public static Date StringToDataYMD(String dateString) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否过期
     * @param expireDay
     * @return
     */
    public static boolean checkExpire(String expireDay) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(new Date());
        if(now.compareTo(expireDay) > 0) {
            return true;
        }
        return false;
    }
}
