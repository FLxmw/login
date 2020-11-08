package com.feilong.controller;

import com.feilong.config.AjaxMessage;
import com.feilong.config.TableData;
import com.feilong.entity.User;
import com.feilong.service.UserService;
import com.feilong.utils.Base64Utils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2020-11-06 13:46:44
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/user")
public class UserController {
    /**
     * 服务对象
     */
    @Autowired
    private UserService userService;

    private final Jedis jedis = new Jedis("47.115.91.98", 6379);

    @GetMapping("/showUsers")
    @ResponseBody
    public TableData<User> showUsers(int page, int limit) {
        PageInfo<User> pageInfo = userService.showUsers(page, limit);
        List<User> userList = pageInfo.getList();
        long total = pageInfo.getTotal();
        TableData<User> tableData = new TableData<User>().setData(userList).setCount(total);
        return tableData;
    }


    @PostMapping("/addUser")
    @ResponseBody
    public AjaxMessage addUser(@RequestBody User user) {
        String encode = Base64Utils.encode(user.getPassword());
        user.setPassword(encode);
        User resultUser = userService.insert(user);
        if (resultUser != null) {
            return new AjaxMessage(true, "注册成功！");
        }
        return new AjaxMessage(false, "注册失败！");

    }

    @PostMapping("/checkUser")
    @ResponseBody
    public int checkUser(String username) {
        System.out.println(username);
        try {
            User user = userService.findUserByUsername(username);
            //不为空不能注册  1
            if (user != null) {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @RequestMapping("/login")
    @ResponseBody
    public AjaxMessage userLogin(String username, String password, HttpServletRequest request) {
        request.getSession().setAttribute("username", username);
        String encodePassword = Base64Utils.encode(password);
        User user = new User().setUsername(username).setPassword(encodePassword);
        User result = userService.queryAll(user);
        System.out.println(result);
        if (result != null) {
            return new AjaxMessage(true, "登录成功！");
        }
        return new AjaxMessage(false, "登录失败，请检查用户名或密码是否有误！");
    }

    @RequestMapping("/checkCode")
    @ResponseBody
    public AjaxMessage checkCode(String code, HttpServletRequest request) {
        String sessionCode = (String) request.getSession().getAttribute("code");
        if (code != null && sessionCode != null && code.equals(sessionCode)) {
            return new AjaxMessage(true, "验证码正确！");
        }
        return new AjaxMessage(false, "验证码输入有误，请重新输入");
    }

    @PostMapping("/toIndex")
    public String toIndex(String username, Model model) {
        System.out.println(username);
        model.addAttribute("username", username);
        return "index";
    }

    @PostMapping("/getPhone")
    @ResponseBody
    public String getPhone(String username) {
        System.out.println(username);
        String phone = userService.findPhone(username);
        return phone;
    }

    @PostMapping("/checkMessage")
    @ResponseBody
    public AjaxMessage checkMessage(String message) {
//        Jedis jedis = new Jedis("10.36.134.22", 6379);
//        jedis.auth("xmw225310");
        String redisMessage = jedis.get("message");
        if (redisMessage == null) {
            return new AjaxMessage(false, "验证码已过时，请重新发送！");
        }
        if (redisMessage != null && message != null && message.equals(redisMessage)) {
            return new AjaxMessage(true, "验证码正确！");
        }
        return new AjaxMessage(false, "验证码输入有误，请重新输入！");
    }

    @RequestMapping("/toUpdatePwd")
    public String toUpdatePwd(Model model) {
//        Jedis jedis = new Jedis("10.36.134.22", 6379);
//        jedis.auth("xmw225310");
        String username = jedis.get("username");
        String message = jedis.get("message");
        //1.根据用户名查找
        User userByUsername = userService.findUserByUsername(username);

        if (message != null && username != null && username.equals(userByUsername.getUsername())) {
            model.addAttribute("user", userByUsername);
            return "updatePwd";
        }
        return "405";

    }

    @RequestMapping("/toUpdatePwdEmail")
    public String toUpdatePwdEmail(String code, Model model) {
        System.out.println(code);
//        Jedis jedis = new Jedis("10.36.134.22", 6379);
//        jedis.auth("xmw225310");
        String redisCode = jedis.get("code");
        String email = jedis.get("email");
        if (code != null && redisCode != null && email != null) {
            //2.根据邮箱查找
            User userByEmail = userService.findUserByEmail(email);
            model.addAttribute("user", userByEmail);
            return "updatePwd";
        }
        return "404";

    }

    @PostMapping("/updatePas")
    @ResponseBody
    public AjaxMessage updatePas(String username, String password) {
        User user = userService.findUserByUsername(username);
        System.out.println(user);
        String encode = Base64Utils.encode(password);
        User updateUser = userService.update(user.setId(user.getId()).setUsername(username).setPassword(encode));
        System.out.println(updateUser);
        if (updateUser != null) {
            return new AjaxMessage(true, "密码修改成功！");
        }
        return new AjaxMessage(false, "密码修改失败！");
    }

    @PostMapping("/getEmail")
    @ResponseBody
    public String getEmail(String email) {
        System.out.println(email);
        User userByEmail = userService.findUserByEmail(email);
        return userByEmail.getEmail();
    }

    @PostMapping("/checkMsg")
    @ResponseBody
    public AjaxMessage checkMsg(String message, HttpSession session) {
//        Jedis jedis = new Jedis("10.36.134.22", 6379);
//        jedis.auth("xmw225310");
        String message1 = jedis.get("message1");
        if (message1 == null) {
            return new AjaxMessage(false, "验证码已经超时，请重新发送！");
        } else {
            if (message != null && message.equalsIgnoreCase(message1)) {
                return new AjaxMessage(true, "验证码正确！");
            }
            return new AjaxMessage(false, "验证码输入有误，请重新输入！");
        }

    }

}