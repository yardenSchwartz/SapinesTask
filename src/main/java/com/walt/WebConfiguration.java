package com.walt;

import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import javax.servlet.annotation.WebServlet;

@Configuration
public class WebConfiguration {
    @Bean
    ServletRegistrationBean h2ServletRegistrationBean(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/*");
        return registrationBean;
    }
}
