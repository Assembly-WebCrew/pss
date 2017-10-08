import models from '../models';
import restify from 'restify';
import bunyan from 'bunyan';
import os from 'os';

const log = bunyan.createLogger({ name: 'pss-api', level: 'DEBUG' });

const commonroutes = require('./routes/common.js');
const eventroutes = require('./routes/events.js');
const partyroutes = require('./routes/parties.js');
const locationroutes = require('./routes/locations.js');

const auth = require('./auth.js');

export function startServer() {
  const server = restify.createServer({
    "name": "Party Schedule Service API",
    "log": log
  });
  server.use(restify.CORS());
  server.use(restify.acceptParser(server.acceptable));
  server.use(restify.bodyParser({ mapParams: true }));
  server.use(restify.authorizationParser());

  server.pre((req, res, next) => {
    //req.log.info({req: req}, 'start'); // for query debugging
    return next();
  })

  server.get('/api/status', commonroutes.status);
  server.get('/api/status/authenticated', auth.checkAuthentication, commonroutes.status); // placeholder way for verifying authentication, or perhaps some extra status data could be served here...

  server.get('/api/events/:party/tags/:tags', eventroutes.taggedevents);
  server.get('/api/events/:party', eventroutes.singlepartyevents);
  server.get('/api/events', eventroutes.allevents);

  server.get('/api/locations', locationroutes.alllocations);
  server.get('/api/locations/:location', locationroutes.singlelocation);

  server.get('/api/parties/:partyid', partyroutes.singleparty);
  server.get('/api/parties', partyroutes.allparties);

  // TODO admin routes & functions

  server.listen(process.env.PORT || 8080, process.env.IP || "0.0.0.0", () =>
    log.info('%s server listening at %s', server.name, server.url)
  )
}