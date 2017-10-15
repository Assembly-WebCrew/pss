var chai = require('chai');
var chaiHttp = require('chai-http');
var server = 'http://localhost:8080';
var should = chai.should();

chai.use(chaiHttp);


describe('Parties', function() {
  it('should list ALL parties on /api/parties GET', (done) => {
      chai.request(server)
        .get('/api/parties')
        .end((err, res) => {
            res.should.have.status(200);
            res.body.should.be.an('array');
            res.body.should.have.lengthOf.above(0);
            res.body[0].should.have.property('shortname');
            res.body[0].should.not.have.property('createdAt');
            done();
        });
  });
  it('should list a SINGLE party on /api/parties/asms27 GET', (done) => {
    chai.request(server)
    .get('/api/parties/asms27')
    .end((err, res) => {
        res.should.have.status(200);
        res.should.be.json;
        res.body.should.have.property('shortname');
        res.body.should.have.property('startsAt');
        res.body.should.have.property('endsAt');
        res.body.should.have.property('originallyStartsAt');
        res.body.should.not.have.property('createdAt');
        done();
    });
  });
});