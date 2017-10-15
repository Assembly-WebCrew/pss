import models from '../../models';

exports.allLocations = (req, res) => {
  models.location.findAll({
    where: { // TODO scope = public
      public: true 
    }
  }).then((locations) => {
    res.send(200, locations);
  }).catch((err) => {
    req.log.error(new Date(), 'Error when fetching locations:', err);
    res.send(500, 'Error when fetching locations. Please check service status.');
  });
}

exports.partyLocations = (req, res) => {
  models.location.findAll({
    where: { // TODO scope = public
      public: true,
      party: req.params.party
    }
  }).then((location) => {
    res.send(200, location);
  }).catch((err) => {
    req.log.error(new Date(), 'Error when fetching location:', err);
    res.send(500, 'Error when fetching locations. Please check service status.');
  });
}

exports.singleLocation = (req, res) => {
  models.location.findOne({
    where: { // TODO scope = public
      public: true,
      party: req.params.party,
      location_id: req.params.location
    }
  }).then((location) => {
    res.send(200, location);
  }).catch((err) => {
    req.log.error(new Date(), 'Error when fetching location:', err);
    res.send(500, 'Error when fetching location. Please check service status.');
  });
}

exports.adminAllLocations = (req, res) => {

}

exports.adminPartyLocations = (req, res) => {
  
}

exports.adminSingleLocation = (req, res) => {
  
}