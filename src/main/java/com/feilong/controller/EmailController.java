package com.feilong.controller;

import com.feilong.config.AjaxMessage;
import com.feilong.entity.User;
import com.feilong.service.UserService;
import com.feilong.utils.Base64Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.mail.internet.MimeMessage;
import java.util.UUID;

/**
 * @author FeiLong
 * @version 1.8
 * @date 2020/11/7 19:23
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    @PostMapping("/sendEmail")
    @ResponseBody
    public AjaxMessage sendEmail(String email) {
        try {

            String uuid = UUID.randomUUID().toString();
            String encode = Base64Utils.encode(uuid);

            Jedis jedis = new Jedis("10.36.134.22", 6379);
            jedis.auth("xmw225310");

            Boolean result1 = jedis.exists("email");
            if (result1) {
                Long count = jedis.del("email");
                jedis.set("email", email);
            } else {
                jedis.set("email", email);
            }


            Boolean result2 = jedis.exists("code");
            if (result2) {
                Long count = jedis.del("code");
                jedis.set("code", encode, "NX", "EX", 180);
            } else {
                jedis.set("code", encode, "NX", "EX", 180);
            }

            System.out.println(jedis.get("code"));
            String url = "localhost:8006/user/toUpdatePwdEmail?code=" + encode;
            System.out.println(url);
            String html = "<html><head><title> 重置密码 </title></head>"
                    + "<body><span>这是重置密码链接</span><a href=" + url + ">点击这里</a><span>找回密码</span></body></html>";
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(username);
            messageHelper.setSubject("密码重置");
            messageHelper.setText(html, true);
            messageHelper.setTo(email);
            mailSender.send(mimeMessage);
            return new AjaxMessage(true, "邮件发送成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AjaxMessage(false, "邮件发送失败！");
    }

}
