package org.assembly.pss.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import java.util.List;
import javax.annotation.Resource;
import org.assembly.pss.annotation.RequireAdmin;
import org.assembly.pss.bean.persistence.Event;
import org.assembly.pss.bean.persistence.Location;
import org.assembly.pss.bean.persistence.Tag;
import org.assembly.pss.database.Database;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RequireAdmin
@Api(tags = "Admin")
@RestController
@RequestMapping("/api/admin")
public class Admin extends AbstractController {

    @Resource
    private Database database;

    @RequestMapping(method = RequestMethod.GET, value = "/event/party/{party}")
    @ApiOperation(value = "Get all public and non-public events for a given party", authorizations = {
        @Authorization(value = "basicAuth")})
    public List<Event> getEvents(@PathVariable String party) {
        return database.getEvents(party);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/location")
    @ApiOperation(value = "Get all (event) locations", authorizations = {
        @Authorization(value = "basicAuth")})
    public List<Location> getLocations() {
        return database.getLocations();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tag")
    @ApiOperation(value = "Get all (event) tags", authorizations = {
        @Authorization(value = "basicAuth")})
    public List<Tag> getTags() {
        return database.getTags();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/party")
    @ApiOperation(value = "Get all parties that currently have at least one event",
            notes = "Parties don't actually exist in the database, therefore if at least one event has a party, then that party exists. "
            + "If the last event for a party is deleted, the party gets deleted as well.",
            authorizations = {
                @Authorization(value = "basicAuth")})
    public List<String> getParties() {
        return database.getParties();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/event")
    @ApiOperation(value = "Create or Update an event", notes = "If an event exists with the provided ID, then that event is updated with the given data. "
            + "If no ID is given, or the ID does not exist in the database, then a new event is created and a new ID is assigned to it, ignoring the supplied ID. "
            + "The response contains the event with the created or updated data, including the ID that was eventually assigned to it if it was just created. \n"
            + "Similar ID behavior applies to the provided Location and Tags; if an ID for the supplied location or tag exists, "
            + "then the existing entry is used and the supplied location/tag data is *ignored* (apart from the the ID(s)). "
            + "If it does not, a new location/tag is created with an automatically assigned ID and the supplied data. \n"
            + "This endpoint cannot be used to modify existing locations or tags, you can only modify *which* locations or tags the event has.",
            authorizations = {
                @Authorization(value = "basicAuth")})
    public Event createOrUpdateEvent(@RequestBody Event event) {
        return database.merge(event);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/location")
    @ApiOperation(value = "Create or Update a location", notes = "If a location exists with the provided ID, then that location is updated with the given data. "
            + "If no ID is given, or the ID does not exist in the database, then a new location is created and a new ID is assigned to it, ignoring the given ID. "
            + "The response contains the location with the created or updated data, including the ID that was eventually assigned to it if it was just created.",
            authorizations = {
                @Authorization(value = "basicAuth")})
    public Location createOrUpdateLocation(@RequestBody Location location) {
        return database.merge(location);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tag")
    @ApiOperation(value = "Create or Update a tag", notes = "If a tag exists with the provided ID, then that tag is updated with the given data. "
            + "If no ID is given, or the ID does not exist in the database, then a new tag is created and a new ID is assigned to it, ignoring the given ID. "
            + "The response contains the tag with the created or updated data, including the ID that was eventually assigned to it if it was just created.",
            authorizations = {
                @Authorization(value = "basicAuth")})
    public Tag createOrUpdateTag(@RequestBody Tag tag) {
        return database.merge(tag);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/event/id/{id}")
    @ApiOperation(value = "Delete an event", authorizations = {
        @Authorization(value = "basicAuth")})
    public void deleteEvent(@PathVariable Integer id) {
        Event event = database.getEvent(id);
        if (event == null) {
            throw new IllegalStateException("Can't delete event with ID: " + id + " since it doesn't seem to exist");
        }
        database.remove(event);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/location/id/{id}")
    @ApiOperation(value = "Delete a location", authorizations = {
        @Authorization(value = "basicAuth")})
    public void deleteLocation(@PathVariable Integer id) {
        Location location = database.getLocation(id);
        if (location == null) {
            throw new IllegalStateException("Can't delete location with ID: " + id + " since it doesn't seem to exist");
        }
        database.remove(location);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/tag/id/{id}")
    @ApiOperation(value = "Delete a tag", authorizations = {
        @Authorization(value = "basicAuth")})
    public void deleteTag(@PathVariable Integer id) {
        Tag tag = database.getTag(id);
        if (tag == null) {
            throw new IllegalStateException("Can't delete tag with ID: " + id + " since it doesn't seem to exist");
        }
        database.remove(tag);
    }
}
