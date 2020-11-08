package com.feilong.login_demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class LoginDemoApplicationTests {
    @Autowired
   private JavaMailSender mailSender;

    @Test
    void contextLoads() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2285538755@qq.com");//properties中配置的username
        message.setTo("xiaomingwang5310feilong@aliyun.com");//向谁发送
        message.setSubject("主题：简单邮件");
        message.setText("简单邮件内容from ");
        mailSender.send(message);
    }

}
