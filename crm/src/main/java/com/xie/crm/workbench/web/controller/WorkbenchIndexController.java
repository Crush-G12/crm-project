package com.xie.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WorkbenchIndexController {

    @RequestMapping(value = "/workbench/index")
    public String index(){
        return "workbench/index";
    }
}
