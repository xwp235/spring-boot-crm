package com.xwplay.crm.platform.exceptions.handler;

import com.xwplay.crm.common.resp.JsonResp;
import com.xwplay.crm.common.utils.ServletUtil;
import com.xwplay.crm.common.utils.SpringUtil;
import com.xwplay.crm.platform.exceptions.resolver.AbstractExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

@Component
public class HttpMediaTypeNotAcceptableExceptionHandler extends AbstractExceptionHandler {

    @Override
    public boolean support(Exception e) {
        return HttpMediaTypeNotAcceptableException.class.isAssignableFrom(e.getClass());
    }

    public HttpMediaTypeNotAcceptableExceptionHandler() {
        super(HttpStatus.OK);
    }

    @Override
    public Object doHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        var message = SpringUtil.getMessage("error_http_media_type_not_acceptable");
        var httpStatus = HttpStatus.NOT_ACCEPTABLE;
        if (ServletUtil.isAjaxRequest(request)) {
            return JsonResp
                    .error(message)
                    .setCode(httpStatus.value());
        } else {
            return errorView(message, httpStatus);
        }
    }

}
