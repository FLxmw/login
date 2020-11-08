package com.feilong.utils;

/**
 * @author FeiLong
 * @version 1.8
 * @date 2020/10/16 21:48
 */
@SuppressWarnings("ALL")
public class MessageUtil {
    //自己的AccessKey ID（不要泄露）
    public static String accessKeyId = "LTAI4FzBG7JG5Kos5bdMuuKK";
    //自己的AccessKeySecret（不要泄露）
    public static String accessKeySecret = "vnQE1WYNNbg2b5BKiwjZInbP2Oi8yQ";
    //签名名称
    public static String signName = "望望畅游网";
    //短信模板ID。请在控制台模板管理页面模板CODE一列查看。
    public static String templateCode = "SMS_204755595";

    //接收验证码的手机号
    public static String tlephone = "13307728697";


    /**
     * 生成六位随机数验证码
     *
     * @return
     */
    public static String setRandomNumber() {
        return Integer.toString(((int) (Math.random() * 900000 + 100000)));
    }

}
