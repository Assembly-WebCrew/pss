import models from '../../models';

exports.allParties = (req, res) => {
  models.party.findAll({
    where: { // TODO scope = public
      public: true 
    }
  }).then((parties) => {
    res.send(200, parties);
  }).catch((err) => {
    req.log.error(new Date(), 'Error when fetching parties:', err);
    res.send(500, 'Error when fetching parties. Please check service status.');
  });
}

exports.singleParty = (req, res) => {
  if (req.params.partyid.length < 3) {
    req.log.error(new Date(), 'Party', req.params.party, 'does not match requirements and cannot exist.');
    res.send(404, 'Defined party does not meet requirements: ' + req.params.party);
  }

  models.party.findOne({
    where: { // TODO scope = public
      shortname: req.params.partyid,
      public: true
    }
  }).then((singleparty) => {
    res.send(200, singleparty);
  }).catch((err) => {
    req.log.error(new Date(), 'Error when fetching party:', err);
    res.send(500, 'Error when fetching party. Please check service status.');
  })
}

exports.adminAllParties = (req, res) => {

}

exports.adminSingleParty = (req, res) => {
  
}