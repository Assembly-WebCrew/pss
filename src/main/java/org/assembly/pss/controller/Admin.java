package org.assembly.pss.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assembly.pss.annotation.RequireAdmin;

@RequireAdmin
@Api(tags = "Admin")
@RestController
@RequestMapping("/api/admin")
public class Admin extends AbstractController {

    private static final Logger LOG = LogManager.getLogger();

    @RequestMapping(method = RequestMethod.GET, value = "/test", produces = "application/json")
    @ApiOperation(value = "testtesttt", authorizations = {
        @Authorization(value = "basicAuth")})
    public org.assembly.pss.bean.Status testtest() {
        LOG.debug("testing debug logging");
        LOG.info("testing info logging");
        LOG.error("testing error logging");
        org.assembly.pss.bean.Status status = new org.assembly.pss.bean.Status();
        status.setStatus("OK_ADMIN");
        status.setServerTime(System.currentTimeMillis());
        return status;
    }
}
