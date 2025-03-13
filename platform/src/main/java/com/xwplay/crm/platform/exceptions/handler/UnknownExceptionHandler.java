package com.xwplay.crm.platform.exceptions.handler;

import com.xwplay.crm.common.enums.ExceptionLevel;
import com.xwplay.crm.common.resp.JsonResp;
import com.xwplay.crm.common.utils.ServletUtil;
import com.xwplay.crm.common.utils.SpringUtil;
import com.xwplay.crm.platform.exceptions.resolver.AbstractExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class UnknownExceptionHandler extends AbstractExceptionHandler {

    @Override
    public boolean support(Exception e) {
        return e != null;
    }

    public UnknownExceptionHandler() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public Object doHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        var message = SpringUtil.getMessage("serverInternalError");
        logger.error(message, e);
        if (ServletUtil.isAjaxRequest(request)) {
            return JsonResp
                    .error(message)
                    .setCode(httpStatus.value())
                    .setExceptionTypeWithTraceId(ExceptionLevel.ERROR);
        } else {
            return errorView(message, httpStatus);
        }
    }
}
