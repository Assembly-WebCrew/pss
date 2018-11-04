package org.assembly.pss.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assembly.pss.bean.RequestError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public abstract class AbstractController {

    private static final Logger LOG = LogManager.getLogger();

    @ExceptionHandler
    @ResponseBody
    public RequestError error(Exception ex, HttpServletResponse response) {
        RequestError error = new RequestError();
        if (ex instanceof MethodArgumentTypeMismatchException && ex.getCause() instanceof NumberFormatException) {
            response.setStatus(400);
            error.setError("Invalid number argument, check your query!");
        } else {
            LOG.error("Uncaught Exception thrown!", ex);
            response.setStatus(500);
            error.setError(ex.getClass().getSimpleName());
        }
        return error;
    }
}
