package com.xwplay.crm.platform.exceptions.handler;


import com.xwplay.crm.common.resp.JsonResp;
import com.xwplay.crm.common.utils.ServletUtil;
import com.xwplay.crm.common.utils.SpringUtil;
import com.xwplay.crm.platform.exceptions.resolver.AbstractExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;

// 触发条件 @Valid校验失败
// 适用数验证注解 @RequestBody、@ModelAttribute
@Component
public class MethodArgumentNotValidExceptionHandler extends AbstractExceptionHandler {

    @Override
    public boolean support(Exception e) {
        return MethodArgumentNotValidException.class.isAssignableFrom(e.getClass());
    }

    public MethodArgumentNotValidExceptionHandler() {
        super(HttpStatus.OK);
    }

    @Override
    protected Object doHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        var errors = new HashMap<String, Object>();
        var e = (MethodArgumentNotValidException) ex;
        var allErrors = e.getAllErrors();
        var httpStatus = HttpStatus.BAD_REQUEST;
        for (var error : allErrors) {
            if (error instanceof FieldError fieldError) {
                errors.put("field-" + fieldError.getField(), fieldError.getDefaultMessage());
            } else if (error != null) {
                errors.put("validator-" + error.getObjectName(), error.getDefaultMessage());
            }
        }
        if (ServletUtil.isAjaxRequest(request)) {
            return JsonResp
                    .error(SpringUtil.getMessage("error_invalid_request_parameters"))
                    .setData(errors)
                    .setCode(httpStatus.value());
        } else {
            return errorView(errors, httpStatus);
        }
    }

}
