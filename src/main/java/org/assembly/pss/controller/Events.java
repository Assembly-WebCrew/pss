package org.assembly.pss.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.assembly.pss.bean.persistence.entity.PublicEvent;
import org.assembly.pss.database.Database;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Event")
@RestController
@RequestMapping(value = "/api/event")
public class Events extends AbstractController {

    @Resource
    private Database database;

    @RequestMapping(method = RequestMethod.GET, value = "/party/{party}")
    @ApiOperation("Get all public events for a given party")
    public List<PublicEvent> getPublicEvents(@PathVariable String party, @RequestParam(required = false) Long location, @RequestParam(required = false) Long tag) {
        return database.getPublicEvents(party, location, tag);
    }
}
