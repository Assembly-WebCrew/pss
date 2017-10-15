var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('../api/index');
var should = chai.should();

chai.use(chaiHttp);


describe('Events', function() {
  it('should list ALL events on /api/events GET');
  it('should list ALL events for a SINGLE party on /api/events/asms27 GET');
  it('should list ALL events for a SINGLE party containing a TAG on /api/events/asms27/tags/opening');
  it('should list a SINGLE event on /api/events/asms27/event/1 GET');
});