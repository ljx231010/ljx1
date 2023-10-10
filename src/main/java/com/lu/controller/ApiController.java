package com.lu.controller;

import com.lu.pojo.SpeciesNetwork1;
import com.lu.service.SpeciesNetworkService;
import com.lu.service.YenKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ApiController {
    @Autowired
    @Qualifier("YenKServiceImpl")
    private YenKService yenKService;
    @Autowired
    @Qualifier("speciesNetworkServiceImpl")
    private SpeciesNetworkService speciesNetworkService;

    @RequestMapping("/bpapi")
    public String BPApi(Model model, String substrate, String product, String minatomic) throws ServletException, IOException {
//        System.out.println("ApiController-bpapi");
//        request.getRequestDispatcher("WEB-INF/page/login.jsp").forward(request, response);
//        String s = T.get(String.format("https://biolab.gxu.edu.cn/BPFinder/SearchServlet?substrate=%s&product=%s&minatomic=%s", substrate, product, minatomic));
        String s = T.get(String.format("http://w2019.jsjxy.bioinformatics.app.gxu.edu.cn:8080/BPFinder/SearchServlet?substrate=%s&product=%s&minatomic=%s", substrate, product, minatomic));
//        String s = T.get(String.format("http://localhost:8080/BPFinder/SearchServlet?substrate=%s&product=%s&minatomic=%s", substrate, product, minatomic));
        if ("".equals(s)){
            model.addAttribute("results", new ArrayList<>());
        }
        else{
        List<String> l1 = new ArrayList<>(Arrays.asList(s.split(",")));
//        l1.add("C00022->R00006->C00900->R03051->C04039->R01209->C00141->R01434->C00183");
//        l1.add("C00022->R00226->C06010->R04439->C04272->R04441->C00141->R01434->C00183");
//        l1.add("C00022->R00006->C00900->R03052->C04181->R04440->C04272->R04441->C00141->R01434->C00183");
//        l1.add("C00022->R00226->C06010->R05071->C04181->R04440->C04272->R04441->C00141->R01434->C00183");
//        model.addAttribute("results",s);
        model.addAttribute("results", l1);
        }
        return "linePath_result";
    }

    @RequestMapping("/YenK")
    public String YenKApi(Model model, String startCompound, String endCompound, Integer k) {
        List<String> stringsPathList = yenKService.yunK(startCompound, endCompound, k);
        model.addAttribute("results", stringsPathList);
        return "linePath_result";
    }

    @RequestMapping("/test1")
    public String test1(Model model) throws IOException {
        SpeciesNetwork1 sce = speciesNetworkService.getArrayFromTxt("sce");
        System.out.println(sce);
        return "t1";
    }

    @RequestMapping("/afpapi")
    public String AFPApi(Model model,String startCompound,String targetCompound,String searchingStrategy,String minatomic,String solutionNumber,String timeLimit,String drawNpathways){
        System.out.println("ApiController-afpapi");
        String s = T.get(String.format("https://biolab.gxu.edu.cn/AF/AFServlet?startCompound=%s&targetCompound=%s&searchingStrategy=%s&minatomic=%s&solutionNumber=%s&timeLimit=%s&drawNpathways=%s",startCompound,targetCompound,searchingStrategy,minatomic,solutionNumber,timeLimit,drawNpathways));
//        String s = "1-->2-->3";
        if ("".equals(s)){
            model.addAttribute("results", new ArrayList<>());
        }
        else{
            List<String> l1 = new ArrayList<>(Arrays.asList(s.replace("--","-").split(",")));
            model.addAttribute("results", l1);
        }
        return "linePath_result";
    }
}
