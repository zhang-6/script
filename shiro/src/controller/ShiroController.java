package controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/shiro")
public class ShiroController {
    @RequestMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password){//→登录后返回值String
        //验证
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated()){//如果没有验证
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);//进行验证→需要UsernamePasswordToken
//            System.out.println(token.hashCode());//测试token是否是同一个值
            token.setRememberMe(true);//记住传入的信息
            subject.login(token);
        }
        return "redirect:/list.jsp";
    }
}
