package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//测试spring、springmvc是否配置完成
@Controller
public class Test {
    @RequestMapping("/user")
    public String test(){
        return "user";
    }
}
