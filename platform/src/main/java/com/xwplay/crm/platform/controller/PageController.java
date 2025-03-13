package com.xwplay.crm.platform.controller;

import gg.jte.Content;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private TemplateEngine templateEngine;

    public PageController(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @GetMapping
    public String index(Model model) {
        var sidebar = renderTemplate("layout/sidebar.jte");
        model.addAttribute("sidebar", sidebar);
        return "index";
    }

    private Content renderTemplate(String templateName) {
        StringOutput output = new StringOutput();
        templateEngine.render(templateName, null, output);
        String renderedContent = output.toString(); // 获取渲染后的 HTML 字符串
        return templateOutput -> templateOutput.writeContent(renderedContent);
    }

}
