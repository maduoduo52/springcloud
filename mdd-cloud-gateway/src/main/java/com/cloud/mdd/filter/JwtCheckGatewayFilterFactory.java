//package com.cloud.mdd.filter;
//
//import com.cloud.mdd.Constant;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import reactor.core.publisher.Mono;
//
///**
// * @Desc:
// * @Author Maduo
// * @Create 2018/12/22 22:35
// */
//public class JwtCheckGatewayFilterFactory  extends AbstractGatewayFilterFactory<JwtCheckGatewayFilterFactory.Config> {
//
//    public JwtCheckGatewayFilterFactory() {
//        super(Config.class);
//    }
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return (exchange, chain) -> {
//            String jwtToken = exchange.getRequest().getHeaders().getFirst(Constant.CLOUD_AUTHORIZATION);
//            //校验jwtToken的合法性
//            if (StringUtils.isNotBlank(jwtToken)) {
//                // 合法
//                // 将用户id作为参数传递下去
//                return chain.filter(exchange);
//            }
//
//            //不合法(响应未登录的异常)
//            ServerHttpResponse response = exchange.getResponse();
//            //设置headers
//            HttpHeaders httpHeaders = response.getHeaders();
//            httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
//            httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
//            //设置body
//            String warningStr = "未登录或登录超时";
//            DataBuffer bodyDataBuffer = response.bufferFactory().wrap(warningStr.getBytes());
//
//            return response.writeWith(Mono.just(bodyDataBuffer));
//        };
//    }
//
//    public static class Config {
//        //Put the configuration properties for your filter here
//    }
//}