import models from '../../models';

exports.status = (req, res) => {
    var result = {
      status: 'ALL_GOOD',
      message: 'Everything is working as intended.',
      events: 0,
      locations: 0,
      parties: 0
    }
  
    models.event.findAll().then((events) => {
      result.events = events.length;
    }).catch((err) => {
      result.status = 'ERROR';
      result.message = err;
    }).then(() => {
      models.location.findAll().then((locations) => {
        result.locations = locations.length;
      }).catch((err) => {
        result.status = 'ERROR';
        result.message = err;
      }).then(() => {
        models.party.findAll().then((parties) => {
          result.parties = parties.length;
        }).catch((err) => {
          result.status = 'ERROR';
          result.message = err;
        }).then(() => {
          res.setHeader('Content-Type', 'application/json');
          res.send(200, result);
        })
      })
    })
}