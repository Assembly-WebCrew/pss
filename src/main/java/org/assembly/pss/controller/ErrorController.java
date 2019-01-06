package org.assembly.pss.controller;

import org.assembly.pss.bean.RequestError;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ErrorController {

    @RequestMapping("/errors/403")
    @ResponseBody
    public RequestError e403() {
        RequestError error = new RequestError();
        error.setError("Permission denied. Check your authorization.");
        return error;
    }

    @RequestMapping("/errors/404")
    @ResponseBody
    public RequestError e404() {
        RequestError error = new RequestError();
        error.setError("Not found. Check your query.");
        return error;
    }

    @RequestMapping("/errors/405")
    @ResponseBody
    public RequestError e405() {
        RequestError error = new RequestError();
        error.setError("Method not allowed. Check your query.");
        return error;
    }

    @RequestMapping("/errors/500")
    @ResponseBody
    public RequestError e500() {
        RequestError error = new RequestError();
        error.setError("Internal server error. Try again later.");
        return error;
    }
}
