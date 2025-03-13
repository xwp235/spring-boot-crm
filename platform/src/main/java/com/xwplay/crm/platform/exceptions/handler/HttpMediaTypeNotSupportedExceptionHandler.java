package com.xwplay.crm.platform.exceptions.handler;

import com.xwplay.crm.common.resp.JsonResp;
import com.xwplay.crm.common.utils.ServletUtil;
import com.xwplay.crm.common.utils.SpringUtil;
import com.xwplay.crm.platform.exceptions.resolver.AbstractExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotSupportedException;

@Component
public class HttpMediaTypeNotSupportedExceptionHandler extends AbstractExceptionHandler {


    @Override
    public boolean support(Exception e) {
        return HttpMediaTypeNotSupportedException.class.isAssignableFrom(e.getClass());
    }

    public HttpMediaTypeNotSupportedExceptionHandler() {
        super(HttpStatus.OK);
    }

    @Override
    public Object doHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        var message = SpringUtil.getMessage("error_request_media_type_not_supported");
        var httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        if (ServletUtil.isAjaxRequest(request)) {
            return JsonResp
                    .error(message)
                    .setCode(httpStatus.value());
        } else {
            return errorView(message, httpStatus);
        }
    }

}
