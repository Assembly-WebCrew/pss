var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('../api/index');
var should = chai.should();

chai.use(chaiHttp);


describe('Locations', function() {
  it('should list ALL locations on /api/locations GET');
  it('should list ALL locations for a SINGLE party on /api/locations/asms27 GET');
  it('should list a SINGLE location on /api/locations/asms27/1 GET');
});