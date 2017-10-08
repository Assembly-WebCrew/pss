import models from '../models';

exports.checkAuthentication = function (req, res, next) {
    res.header('WWW-Authenticate', 'Basic realm="Auth required"');

    if (!req.authorization || !req.authorization.basic || !req.authorization.basic.password) {
        res.send(401);
        return next(false);
    }

    models.user.findOne({
        where: {
            username: req.authorization.basic.username
        }
    }).then((user) => {
        if (user.validPassword(req.authorization.basic.password)) {
            return next();
        } else {
            req.log.warn(new Date(), 'User', req.authorization.basic.username, 'tried to access an authenticated endpoint with incorrect password');
            res.send(401);
            return next(false);
        }
    }).catch((err) => {
        req.log.warn(new Date(), 'Nonexistant user', req.authorization.basic.username, 'tried to access an authenticated endpoint');
        res.send(401);
        return next(false);
    })
};