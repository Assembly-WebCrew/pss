import models from '../models';
import restify from 'restify';
import bunyan from 'bunyan';
import os from 'os';

const log = bunyan.createLogger({ name: 'pss-api', level: 'DEBUG' });

const commonRoutes = require('./routes/common.js');
const eventRoutes = require('./routes/events.js');
const partyRoutes = require('./routes/parties.js');
const locationRoutes = require('./routes/locations.js');

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

  server.get('/api/status', commonRoutes.status);
  server.get('/api/status/authenticated', auth.checkAuthentication, commonRoutes.status); // placeholder way for verifying authentication, or perhaps some extra status data could be served here...

  server.get('/api/events', eventRoutes.allEvents);
  server.get('/api/events/:party', eventRoutes.singlePartyEvents);
  server.get('/api/events/:party/tags/:tags', eventRoutes.taggedEvents);

  server.get('/api/locations', locationRoutes.allLocations);
  server.get('/api/locations/:party', locationRoutes.partyLocations);
  server.get('/api/locations/:party/:location', locationRoutes.singleLocation);

  server.get('/api/parties', partyRoutes.allParties);
  server.get('/api/parties/:partyid', partyRoutes.singleParty);

  server.get('/api/admin/events', eventRoutes.adminAllEvents);
  server.get('/api/admin/events/:party', eventRoutes.adminSinglePartyEvents);
  server.get('/api/admin/events/:party/:event', eventRoutes.adminSingleEvent);
  server.get('/api/admin/events/:party/tags/:tags', eventRoutes.adminTaggedEvents);

  server.get('/api/admin/locations', locationRoutes.adminAllLocations);
  server.get('/api/admin/locations/:party', locationRoutes.adminPartyLocations);
  server.get('/api/admin/locations/:party/:location', locationRoutes.adminSingleLocation);

  server.get('/api/admin/parties', partyRoutes.adminAllParties);
  server.get('/api/admin/parties/:partyid', partyRoutes.adminSingleParty);

  // TODO admin routes & functions

  server.listen(process.env.PORT || 8080, process.env.IP || "0.0.0.0", () =>
    log.info('%s server listening at %s', server.name, server.url)
  )
}