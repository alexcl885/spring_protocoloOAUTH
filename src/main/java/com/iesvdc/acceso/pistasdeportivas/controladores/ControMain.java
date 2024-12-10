package com.iesvdc.acceso.pistasdeportivas.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class ControMain {
    
    @GetMapping("/")
    public String getIndex() {
        return "index";
    }
    
    @GetMapping("/error")
    public String getError() {
        return "error";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/acerca")
    public String acerca() {
        return "acerca";
    }
}
