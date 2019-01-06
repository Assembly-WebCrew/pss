package org.assembly.pss.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assembly.pss.bean.RequestError;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public abstract class AbstractController {

    private static final Logger LOG = LogManager.getLogger();

    @ExceptionHandler
    @ResponseBody
    public RequestError error(Exception ex, HttpServletResponse response) {
        RequestError error = new RequestError();
        response.setStatus(400);
        if (ex instanceof MethodArgumentTypeMismatchException && ex.getCause() instanceof NumberFormatException) {
            // Happens when supplying non-numbers as numeric request parameters
            error.setError("Invalid number argument. Check your query.");
        }
        if (ex instanceof IllegalStateException) {
            // Used mainly when a request is not consistent with the database
            String msg = ex.getMessage();
            if (StringUtils.isBlank(msg)) {
                msg = "Illegal state. Check your query data.";
            }
            error.setError(msg);
        } else if (ex instanceof RollbackException
                && ex.getCause() instanceof DatabaseException
                && ex.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
            // Lazy way to pass on SQL errors like "Column 'name' cannot be null"
            error.setError(ex.getCause().getCause().getMessage());
        } else {
            LOG.error("Uncaught Exception thrown!", ex);
            response.setStatus(500);
            error.setError(ex.getClass().getSimpleName());
        }
        return error;
    }
}
