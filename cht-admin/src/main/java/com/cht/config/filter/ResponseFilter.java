package com.cht.config.filter;

import com.alibaba.fastjson2.JSON;
import com.cht.admin.pojo.R;
import com.cht.config.filter.wrapper.ResponseWrapper;
import com.cht.utils.SMUtils;
import com.cht.utils.SpringContextUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@WebFilter(filterName = "responseFilter",urlPatterns = {"/api/*"})
public class ResponseFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ResponseWrapper wrapper = new ResponseWrapper((HttpServletResponse) servletResponse);
        filterChain.doFilter(servletRequest, wrapper);
        String result = wrapper.getResponseData(StandardCharsets.UTF_8.toString());
        if (result.length() < 512000) {
            log.info("返回加密出参：{}", result);
        }
        String encryptResult;
        SMUtils smUtils = (SMUtils) SpringContextUtils.getBean(SMUtils.class);
        try {
            encryptResult = smUtils.encryptStr(result);
        } catch (Exception e) {
            encryptResult = smUtils.encryptStr(JSON.toJSONString(R.FAIL()));
        }
        servletResponse.getOutputStream().write(encryptResult.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
