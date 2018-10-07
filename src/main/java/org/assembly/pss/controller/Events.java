package org.assembly.pss.controller;

import io.swagger.annotations.Api;
import java.util.List;
import javax.annotation.Resource;
import org.assembly.pss.bean.persistence.PublicEvent;
import org.assembly.pss.service.EventService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Event")
@RestController
@RequestMapping(value = "/api/event", produces = "application/json")
public class Events extends AbstractController {

    @Resource
    private EventService eventService;

    @RequestMapping(method = RequestMethod.GET, value = "/party/{party}")
    public List<PublicEvent> getPublicEvents(@PathVariable String party, @RequestParam(required = false) Long location, @RequestParam(required = false) String tag) {
        return eventService.getPublicEvents(party, location, tag);
    }
}
