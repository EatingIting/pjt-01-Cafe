package com.miniproject.cafe.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @GetMapping("/coffee")
    public String coffee() {
        return "menu/coffee";
    }

    @GetMapping("/beverage")
    public String beverage() {
        return "menu/beverage";
    }

    @GetMapping("/food")
    public String food() {
        return "menu/food";
    }

    @GetMapping("/tea")
    public String tea() {
        return "menu/tea";
    }

    @GetMapping("/newMenu")
    public String newMenu() {
        return "menu/newMenu";
    }

}
