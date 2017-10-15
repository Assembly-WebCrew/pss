import models from '../../models';

const removeInternals = (event) => {
  // TODO handler for excess location data (publicity etc.) that we don't want to return
}

exports.allLocations = (req, res) => {
  models.location.findAll({
    where: { public: true }
  }).then((locations) => {
    // TODO removeinternals for each location
    res.send(200, locations);
  }).catch((err) => {
    req.log.error(new Date(), 'Error when fetching locations:', err);
    res.send(500, 'Error when fetching locations. Please check service status.');
  });
}

exports.partyLocations = (req, res) => {
  models.location.findAll({
    where: {
      public: true,
      party: req.params.party
    }
  }).then((location) => {
    // TODO removeInternals for location
    res.send(200, location);
  }).catch((err) => {
    req.log.error(new Date(), 'Error when fetching location:', err);
    res.send(500, 'Error when fetching locations. Please check service status.');
  });
}

exports.singleLocation = (req, res) => {
  models.location.findOne({
    where: {
      public: true,
      party: req.params.party,
      location_id: req.params.location
    }
  }).then((location) => {
    // TODO removeInternals for location
    res.send(200, location);
  }).catch((err) => {
    req.log.error(new Date(), 'Error when fetching location:', err);
    res.send(500, 'Error when fetching location. Please check service status.');
  });
}