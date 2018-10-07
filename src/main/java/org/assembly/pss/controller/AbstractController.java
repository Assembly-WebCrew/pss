package org.assembly.pss.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.assembly.pss.bean.RequestError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class AbstractController {

    private static final Logger LOG = Logger.getLogger(AbstractController.class);

    @ExceptionHandler
    @ResponseBody
    public RequestError error(Exception ex, HttpServletResponse response) {
        RequestError error = new RequestError();
        LOG.error("Uncaught Exception thrown!", ex);
        response.setStatus(500);
        error.setError(ex.getClass().getSimpleName());
        return error;
    }
}
