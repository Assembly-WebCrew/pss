import models from '../../models';

const removeinternals = (event) => {
  // TODO handler for excess location data (publicity etc.) that we don't want to return
}

exports.alllocations = (req, res) => {
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

exports.singlelocation = (req, res) => {
  models.location.findAll({
    where: { public: true }
  }).then((location) => {
    // TODO removeinternals for location
    res.send(200, location);
  }).catch((err) => {
    req.log.error(new Date(), 'Error when fetching location:', err);
    res.send(500, 'Error when fetching location. Please check service status.');
  });
}