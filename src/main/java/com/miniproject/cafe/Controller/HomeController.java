package com.miniproject.cafe.Controller;

import com.miniproject.cafe.Service.MenuService;
import com.miniproject.cafe.VO.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/main")
    public String home() {
        return "main";
    }

    @GetMapping("/order_history")
    public String order_history() {
        return "order_history";
    }

    @GetMapping("coffee")
    public String food() {
        return "redirect:/menu/coffee";
    }

    @GetMapping("/mypick")
    public String myPickPage() {
        return "mypick";
    }

}
