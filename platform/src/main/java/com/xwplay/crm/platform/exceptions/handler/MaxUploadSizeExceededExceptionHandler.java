package com.xwplay.crm.platform.exceptions.handler;

import com.xwplay.crm.common.resp.JsonResp;
import com.xwplay.crm.common.utils.ServletUtil;
import com.xwplay.crm.common.utils.SpringUtil;
import com.xwplay.crm.platform.exceptions.resolver.AbstractExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Component
public class MaxUploadSizeExceededExceptionHandler extends AbstractExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize maxFileSize;

    @Override
    public boolean support(Exception e) {
        return MaxUploadSizeExceededException.class.isAssignableFrom(e.getClass());
    }

    public MaxUploadSizeExceededExceptionHandler() {
        super(HttpStatus.OK);
    }

    @Override
    public Object doHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        var message = SpringUtil.getMessage("error_upload_file_too_large", maxFileSize.toKilobytes() + "KB");
        if (ServletUtil.isAjaxRequest(request)) {
            return JsonResp
                    .error(message)
                    .setCode(HttpStatus.BAD_REQUEST.value());
        } else {
            return errorView(message, httpStatus);
        }
    }

}
