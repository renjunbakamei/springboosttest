package com.jfpal;

import com.jfpal.report.entity.Client;
import com.jfpal.report.entity.Report;
import com.jfpal.report.mapper.impl.ReportMapperImpl;
import com.jfpal.report.util.AllocateEngine;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author liuzh
 * @since 2015-12-12 18:22
 */
@Controller
@SpringBootApplication
@EnableAutoConfiguration
@MapperScan(basePackages = "com.jfpal.report.mapper")
public class Application extends WebMvcConfigurerAdapter {


    @Autowired
    private AllocateEngine allocateEngine;

    @Autowired
    private ReportMapperImpl reportMapperImpl;

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    @RequestMapping("/index")
    public String index(Model model) {
        try {
            List<Client> list=allocateEngine.getUsefulClients(0);
            model.addAttribute("clients", list);
            return "index";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @RequestMapping("/test")
    public List<Report> index() {
        try {
            Report report=new Report();
            report.setStatus("01");
            List<Report> list=reportMapperImpl.getReportList(report);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
