package com.cht.config.filter.wrapper;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.cht.utils.Constants;
import com.cht.utils.SMUtils;
import com.cht.utils.SpringContextUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {
    private byte[] requestBody;
    private byte[] deRequestBody;

    private HttpServletRequest request;


    public RequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            if (requestBody == null) {
                IoUtil.copy(request.getInputStream(), baos);
                this.requestBody = baos.toByteArray();
                String jsonStr = new String(this.requestBody);
                JSONObject job = JSON.parseObject(jsonStr);
                String encryptStr = job.getString(Constants.ENCRYPT_PREFIX);
                if (StrUtil.isNotBlank(encryptStr)) {
                    SMUtils smUtils = (SMUtils) SpringContextUtils.getBean(SMUtils.class);
                    String decodeStr = smUtils.decryptStr("04"+encryptStr);
                    log.info("解密后参数：{}", decodeStr);
                    JSONObject inJob = JSON.parseObject(decodeStr);
                    job.putAll(inJob);
                    job.remove(Constants.ENCRYPT_PREFIX);
                    deRequestBody = JSON.toJSONBytes(job);
                } else {
                    deRequestBody = this.requestBody;
                }
            }
            final ByteArrayInputStream bais = new ByteArrayInputStream(deRequestBody);
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                @Override
                public int read() throws IOException {
                    return bais.read();
                }
            };
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}
