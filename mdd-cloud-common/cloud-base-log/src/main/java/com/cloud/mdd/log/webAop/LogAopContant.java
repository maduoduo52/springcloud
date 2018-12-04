package com.cloud.mdd.log.webAop;

import org.springframework.core.io.InputStreamSource;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zengliming
 * @date 2018/4/27 13:20
 */
public class LogAopContant {

    public static List<Class> NO_JSON_CLASS = new ArrayList<>();
    static {
        NO_JSON_CLASS.add(ServletResponse.class);
        NO_JSON_CLASS.add(ServletRequest.class);
        NO_JSON_CLASS.add(HttpServlet.class);
        NO_JSON_CLASS.add(HttpSession.class);
        NO_JSON_CLASS.add(InputStreamSource.class);
    }
}
