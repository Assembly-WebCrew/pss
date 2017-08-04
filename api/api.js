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

  server.get('/api/events/:party', (req, res, next) => {
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