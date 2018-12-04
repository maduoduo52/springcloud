package com.cloud.mdd.log.config;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author zengliming
 * @date 2018/4/25 16:59
 */
@Slf4j
public class AppNameConverter extends ClassicConverter {

    static Set<String> set = new LinkedHashSet<>();
    //只读取4个根文件
    static {
        set.add("bootstrap.yml");
        set.add("application.yml");
        set.add("bootstrap.properties");
        set.add("application.properties");
    }

    public static String appName = "appName未设置";


    public AppNameConverter() {
        String name = null;
        String active = null;
        String url = null;
        for (String s : set) {
            try {
                url = s;
                if(url.endsWith("yml")){
                    String [] strs = load(url);
                    //已经获取了appName
                    if(!StringUtils.isEmpty(strs[0])){
                        name = strs[0];
                        break;
                    }
                    //没有获取到appName 获取到了active
                    if(!StringUtils.isEmpty(strs[1])){
                        active = strs[1];
                        break;
                    }
                }else{
                    Map<String,Object> map = GetAllProperties(url);
                    if(map!=null && !map.isEmpty()){
                        if(map.get("spring.application.name")!=null && !"".equals(map.get("spring.application.name"))){
                            name = (String) map.get("spring.application.name");
                            break;
                        }
                        if(map.get("spring.profiles.active")!=null && !"".equals(map.get("spring.profiles.active"))){
                            active = (String) map.get("spring.profiles.active");
                            break;
                        }
                    }
                }
            }catch (Exception e){
            }
        }
        if(!StringUtils.isEmpty(name)){ //appName已经获取
            appName = name;
        }else if(!StringUtils.isEmpty(active)){ //未获取appName
            //排除native  配置中心所需
            active = active.replaceAll(",native","").replaceAll("native,","").trim();
            try {
                int l = url.lastIndexOf(".");
                url = url.substring(0,l)+"-"+active+url.substring(l,url.length());
                if(url.endsWith("yml")){
                    String [] strs = load(url);
                    if(!StringUtils.isEmpty(strs[0])){
                        appName = strs[0];
                    }
                    if(StringUtils.isEmpty(strs[0])){
                        url = url.substring(0,l)+"-"+active+".properties";
                        Map<String,Object> map = GetAllProperties(url);
                        if(map!=null && map.get("spring.application.name")!=null && !map.get("spring.application.name").equals("")){
                            appName = (String) map.get("spring.application.name");
                        }
                    }
                }else{
                    Map<String,Object> map = GetAllProperties(url);
                    if(map!=null && map.get("spring.application.name")!=null && !map.get("spring.application.name").equals("")){
                        appName = (String) map.get("spring.application.name");
                    }else{
                        url = url.substring(0,l)+"-"+active+".yml";
                        String [] strs = load(url);
                        if(!StringUtils.isEmpty(strs[0])){
                            appName = strs[0];
                        }
                    }
                }
            }catch (Exception e){
                log.error("",e);
            }
        }
    }

    /**
     * 加载yml文件
     * @param url
     * @return
     */
    private String [] load(String url){
        String name = null;
        String active = null;
        String yml = read(url);
        if(!StringUtils.isEmpty(yml)){
            Yaml yaml = new Yaml();
            Map<String, Object> ymls = (Map<String, Object>)  yaml.load(yml);
            if(ymls!=null && !ymls.isEmpty()){
                Map<String,Object> spring = (Map<String, Object>) ymls.get("spring");
                if(spring!=null && !spring.isEmpty()){
                    //首先取name 如果没有再去active
                    Map<String,Object> application = (Map<String, Object>) spring.get("application");
                    if(application!=null && !application.isEmpty()){
                        name = (String) application.get("name");
                    }
                    Map<String,Object> profiles = (Map<String, Object>) spring.get("profiles");
                    if(profiles!=null && !profiles.isEmpty()){
                        active = (String) profiles.get("active");
                    }
                }
            }
        }
        return new String[]{name,active};
    }




    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        return appName;
    }

    //读取Properties的全部信息
    private static Map<String,Object> GetAllProperties(String filePath) throws IOException {
        Map<String,Object> map = new HashMap<>();
        try {
            Properties pps = new Properties();
            ClassPathResource resource = new ClassPathResource(filePath);
            @Cleanup InputStream inputStream = resource.getInputStream();
            pps.load(inputStream);
            Enumeration en = pps.propertyNames(); //得到配置文件的名字
            while(en.hasMoreElements()) {
                String strKey = (String) en.nextElement();
                String strValue = pps.getProperty(strKey);
                map.put(strKey,strValue);
            }
        } catch (Exception e) {
            log.error("{}",e);
        }
        return map;
    }

    /**
     * 读取文件信息  yml文件
     * @param filepath
     * @return
     */
    private String read(String filepath){
        String returnStr = "";
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            String str = "";
            ClassPathResource resource = new ClassPathResource(filepath);
            InputStream inputStream = resource.getInputStream();
            isr = new InputStreamReader(inputStream);
            br = new BufferedReader(isr);
            while ((str = br.readLine()) != null) {
                returnStr+=str+"\n";
            }
        } catch (Exception e) {
            log.error("{}",e);
        }finally {
            try {
                br.close();
                isr.close();
            } catch (IOException e) {
            }
        }
        return returnStr;
    }
}
