package com.xwplay.crm.platform.exceptions.handler;

import com.xwplay.crm.common.resp.JsonResp;
import com.xwplay.crm.common.utils.ServletUtil;
import com.xwplay.crm.common.utils.SpringUtil;
import com.xwplay.crm.platform.exceptions.resolver.AbstractExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.stream.Stream;

@Component
public class InvalidRequestExceptionHandler extends AbstractExceptionHandler {

    public InvalidRequestExceptionHandler() {
        super(HttpStatus.OK);
    }

    @Override
    public boolean support(Exception e) {
        return Stream.of(
                MissingServletRequestPartException.class,
                HttpMessageNotReadableException.class,
                ServletRequestBindingException.class,
                TypeMismatchException.class
        ).anyMatch(clazz -> clazz.isInstance(e));
    }

    @Override
    public Object doHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        var message = SpringUtil.getMessage("error_invalid_request_parameters");
        var httpStatus = HttpStatus.BAD_REQUEST;
        printDevLog(message, e);
        if (ServletUtil.isAjaxRequest(request)) {
            return JsonResp
                    .error(message)
                    .setCode(httpStatus.value());
        } else {
            return errorView(message, httpStatus);
        }
    }
}
