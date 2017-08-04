import models from '../models'
import restify from 'restify'
import bunyan from 'bunyan'
import os from 'os'

const log = bunyan.createLogger( { name: 'pss-api', level: 'DEBUG' } );

export function startServer() {
  const server = restify.createServer();
  server.use(restify.CORS());
  server.use(restify.acceptParser(server.acceptable));
  server.use(restify.bodyParser({ mapParams: true }));
  server.use(restify.authorizationParser());
  
  server.get('/api/status', (req, res, next) => {
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

  })

  server.get('/api/events/:party/tags/:tags', (req, res, next) => {
    if (req.params.party.length < 3 || req.params.tags.length < 2) {
      log.error(new Date() + ' Party ' + req.params.party + ' or tags + ' + req.params.tags  + ' does not match requirements and cannot exist.');
      res.send(404, 'Defined party or tags do not meet requirements: ' + req.params.party);
    }

    var tags = [];

    if (req.params.tags.includes('+')) {
      req.params.tags.split('+').forEach((tag) => {
        tags.push(tag);
      });
    }
    else {
      tags.push(req.params.tags);
    }
    
    models.event.findAll({
      where: { 
        party: req.params.party,
        tags: { $like: '%' + tags[0] + '%' }
      }
    }).then((events) => {

      // If we have more than one tag, we'll filter out events without all of them.
      if (tags.length > 1) {
        var tempevents = [];
        events.forEach((event) => {
          var included = true;
          tags.forEach((tag) => {
            if (!event.tags.includes(tag)) included = false;
          })
          if (included) {
            tempevents.push(event);
          }
        })
        events = tempevents;
      }

      res.setHeader('Content-Type', 'application/json');
      res.send(200, events);
    }).catch((err) => {
      log.error(new Date() + ' Error when fetching events for party ' + req.params.party + ' with tags ' + req.params.tags + ': ' + err);
      res.send(500, 'Error when fetching events for party ' + req.params.party);
    })
  })

  server.get('/api/events/:party', (req, res, next) => {
    if (req.params.party.length < 3) {
      log.error(new Date() + ' Party ' + req.params.party + ' does not match requirements and cannot exist.');
      res.send(404, 'Defined party does not meet requirements: ' + req.params.party);
    }

    models.event.findAll({
      where: { party: req.params.party }
    }).then((events) => {
      res.setHeader('Content-Type', 'application/json');
      res.send(200, events);
    }).catch((err) => {
      log.error(new Date() + ' Error when fetching events for party ' + req.params.party + ': ' + err);
      res.send(500, 'Error when fetching events for party ' + req.params.party);
    })
  })

  server.get('/api/events', (req, res, next) => {
    log.error(new Date() + ' No party specified.');
    res.send(400, 'No party specified. Try with /api/events/partyid found from /api/events/parties')
  })

  server.get('/api/parties/:partyid', (req, res, next) => {
    if (req.params.partyid.length < 3) {
      log.error(new Date() + ' No party specified.');
      res.send(400, 'No party specified. Get all active parties from /api/events/parties')
    }

    models.party.findOne({
      where: { 
        shortname: req.params.partyid,
        active: true
      }
    }).then((parties) => {
      res.send(200, parties);
    }).catch((err) => {
      log.error(new Date() + ' Error when fetching parties: ' + err);
      res.send(500, 'Error when fetching party. Please check service status.');
    })
  })
  
  server.get('/api/parties', (req, res, next) => {
    models.party.findAll({
      where: { active: true }
    }).then((parties) => {
      res.send(200, parties);
    }).catch((err) => {
      log.error(new Date() + ' Error when fetching parties: ' + err);
      res.send(500, 'Error when fetching parties. Please check service status.');
    })
  })

  server.listen( process.env.PORT || 8080, process.env.IP || "0.0.0.0", () =>
    log.info( '%s server listening at %s', server.name, server.url )
  )
}