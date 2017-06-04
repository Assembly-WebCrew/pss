import models from '../models'
import restify from 'restify'
import bunyan from 'bunyan'
import os from 'os'

const log = bunyan.createLogger( { name: 'ucdevapi', level: 'DEBUG' } );
var ipaddr = require('./ipaddr');
var misc = require('./misc');
var virtserver = require('./server');
var storage = require('./storage');

export function startServer() {
  const server = restify.createServer();
  server.use(restify.CORS());
  server.use(restify.acceptParser(server.acceptable));
  server.use(restify.bodyParser({ mapParams: true }));
  server.use(restify.authorizationParser());

  // test route for debug
  server.post('/foo', function(req, res, next) {
    console.log("Reached /foo");
    console.log(req.params);
  })

  // TODO actual application :P

  server.listen( process.env.PORT || 8080, process.env.IP || "0.0.0.0", () =>
    log.info( '%s server listening at %s', server.name, server.url )
  )
}