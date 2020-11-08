package com.feilong.controller;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.feilong.config.AjaxMessage;
import com.feilong.utils.MessageUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;

import static com.feilong.utils.MessageUtil.signName;
import static com.feilong.utils.MessageUtil.templateCode;

/**
 * @author FeiLong
 * @version 1.8
 * @date 2020/10/16 21:47
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/send")
public class SendMessageController {


    @PostMapping("/sendMessage1")
    public AjaxMessage sendMessage1(String phone, HttpServletRequest req) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", MessageUtil.accessKeyId, MessageUtil.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        String message = MessageUtil.setRandomNumber();
        System.out.println(message);
        //把验证码存储在redis中
        Jedis jedis = new Jedis("10.36.134.22", 6379);
        jedis.auth("xmw225310");
        Boolean result1 = jedis.exists("message1");
        if (result1) {
            Long aLong = jedis.del("message1");
            jedis.set("message1", message, "NX", "EX", 180);
        } else {
            jedis.set("message1", message, "NX", "EX", 180);
        }
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", "{code:" + message + "}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return new AjaxMessage(true, "成功！");
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return new AjaxMessage(false, "失败！");
    }


    @PostMapping("/sendMessage")
    public AjaxMessage sendMessage(String phone, String userIdentify, HttpServletRequest req) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", MessageUtil.accessKeyId, MessageUtil.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        //保证验证码的唯一性需要及时清理
        req.getSession().removeAttribute("message");
        String message = MessageUtil.setRandomNumber();
        System.out.println(message);

        //把验证码存储在redis中
        Jedis jedis = new Jedis("10.36.134.22", 6379);
        jedis.auth("xmw225310");
        Boolean result1 = jedis.exists("message");
        if (result1) {
            Long aLong = jedis.del("message");
            jedis.set("message", message, "NX", "EX", 180);
        } else {
            jedis.set("message", message, "NX", "EX", 180);
        }

        Boolean result = jedis.exists("username");
        if (result) {
            Long count = jedis.del("username");
            jedis.set("username", userIdentify);
        } else {
            jedis.set("username", userIdentify);
        }
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", "{code:" + message + "}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return new AjaxMessage(true, "成功！");
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return new AjaxMessage(false, "失败！");
    }
}
