package com.xwplay.crm.common.config;

import com.xwplay.crm.common.config.jte.JteViewResolver;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;

import java.nio.file.Path;
import java.nio.file.Paths;

// https://github.com/casid/jte-spring-boot-demo
@Configuration
public class JteConfig {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Bean
    ViewResolver jteViewResolve(TemplateEngine templateEngine) {
        return new JteViewResolver(templateEngine);
    }

    @Bean
    TemplateEngine templateEngine() {
        if ("prod".equals(activeProfile)) {
            // Templates will be compiled by the maven build task
            return TemplateEngine.createPrecompiled(ContentType.Html);
        } else {
            // Here, a JTE file watcher will recompile the JTE templates upon file save (the web browser will auto-refresh)
            // If using IntelliJ, use Ctrl-F9 to trigger an auto-refresh when editing non-JTE files.
            var codeResolver = new DirectoryCodeResolver(Path.of("platform", "src", "main", "resources", "templates"));
            var templateEngine = TemplateEngine.create(codeResolver, Paths.get("platform/jte-classes"), ContentType.Html, getClass().getClassLoader());
            templateEngine.setBinaryStaticContent(true);
            return templateEngine;
        }
    }

}
