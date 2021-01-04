package ru.topjava.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class RootController {

    @GetMapping("/")
    public String root() {
        return "redirect:swagger-ui.html";
        //return "index";
    }

}
