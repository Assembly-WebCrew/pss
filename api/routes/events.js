import models from '../../models';

const removeInternals = (event) => {
  // TODO handler for excess event data (publicity etc.) that we don't want to return to normal users
}

const removeTranslationInternals = (translation) => {
  // TODO remove id, eventId, createdAt & updatedAt
}

const stripTags = (rawTags) => {
  var tags = [];

  if (rawTags.includes('+')) {
    rawTags.split('+').forEach((tag) => {
      tags.push(tag);
    });
  }
  else {
    tags.push(req.params.tags);
  }

  return tags;
}

const filterTaggedEvents = (events, tags) => {
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

    return tempevents;
  }
}

const addTranslations = (event) => {
  return new Promise((resolve, reject) => {
    models.translation.findAll({
      where: {
        eventId: event.id
      }
    }).then((translations) => {
      event.dataValues.translations = translations;
      // TODO removeTranslationInternals
      resolve(event);
    }).catch((err) => {
      reject(err);
    })
  })
}

exports.allEvents = (req, res) => {
  models.event.findAll({
    where: {
      public: true
    }
  }).then((events) => {
    Promise.all(events.map(addTranslations))
    .then((translatedEvents) => {
      res.setHeader('Content-Type', 'application/json');
      res.send(200, translatedEvents);
    })
    .catch((err) => {
      req.log.error(new Date(), 'Error when fetching translation for event:', err);
      res.send(500, 'Error when fetching all events');
    })
  }).catch((err) => {
    req.log.error(new Date(), 'Error when fetching events:', err);
    res.send(500, 'Error when fetching events for party ' + req.params.party);
  });
}

exports.singlePartyEvents = (req, res) => {
  if (req.params.party.length < 3) {
    req.log.error(new Date(), 'Party', req.params.party, 'does not match requirements and cannot exist.');
    res.send(404, 'Defined party does not meet requirements: ' + req.params.party);
  }

  models.event.findAll({
    where: {
      party: req.params.party,
      public: true
    }
  }).then((events) => {
    Promise.all(events.map(addTranslations))
    .then((translatedEvents) => {
      res.setHeader('Content-Type', 'application/json');
      res.send(200, translatedEvents);
    }).catch((err) => {
      req.log.error(new Date(), 'Error when fetching translations for party', req.params.party + ':', err);
      res.send(500, 'Error when fetching events for party ' + req.params.party);
    });
  }).catch((err) => {
    req.log.error(new Date(), 'Error when fetching events for party', req.params.party + ':', err);
    res.send(500, 'Error when fetching events for party ' + req.params.party);
  });
}

exports.singleEvent = (req, res) => {
  if (req.params.party.length < 3 || Number.isInteger(req.params.event)) {
    req.log.error(new Date(), 'Party', req.params.party, 'or event', req.params.event, 'does not match requirements and cannot exist.');
    res.send(404, 'Defined party or event does not meet requirements.');
  }

  models.event.findOne({
    where: {
      party: req.params.party,
      id: req.params.event,
      public: true
    }
  }).then((event) => {
    addTranslations(event)
    // TODO removeInternals(event)
    .then((translatedEvent) => {
      res.setHeader('Content-Type', 'application/json');
      res.send(200, translatedEvent);
    }).catch((err) => {
      req.log.error(new Date(), 'Error fetching translations for event', req.params.event, 'in party', req.params.party);
      res.send(500, 'Error when fetching event ' + req.params.event + ' for party ' + req.params.party);
    });
  }).catch((err) => {
    req.log.error(new Date(), 'Error fetching event', req.params.event, 'for party', req.params.party);
    res.send(500, 'Error when fetching event ' + req.params.event + ' for party ' + req.params.party);
  });
}

exports.taggedEvents = (req, res) => {
  if (req.params.party.length < 3 || req.params.tags.length < 2) {
    req.log.error(new Date(), 'Party', req.params.party, 'or tags', req.params.tags, 'do not match requirements and cannot exist.');
    res.send(404, 'Defined party or tags do not meet requirements: ' + req.params.party);
  }

  var tags = stripTags(req.params.tags);

  models.event.findAll({
    where: {
      party: req.params.party,
      tags: { $like: '%' + tags[0] + '%' },
      public: true
    }
  }).then((events) => {
    events = filterTaggedEvents(events, tags);
    // TODO removeInternals(event)

    Promise.all(events.map(addTranslations))
    .then((translatedEvents) => {
      res.setHeader('Content-Type', 'application/json');
      res.send(200, translatedEvents);
    }).catch((err) => {
      req.log.error(new Date(), 'Error when fetching translations for party', req.params.party + ':', err);
      res.send(500, 'Error when fetching events for party ' + req.params.party);
    });    
  }).catch((err) => {
    req.log.error(new Date(), 'Error when fetching events for party', req.params.party + ' with tags ' + req.params.tags + ': ' + err);
    res.send(500, 'Error when fetching events for party ' + req.params.party);
  });
}

exports.adminAllEvents = (req, res) => {

  models.event.findAll({
    where: {
      party: req.params.party
    }
  }).then((events) => {
    Promise.all(events.map(addTranslations))
    .then((translatedEvents) => {
      res.setHeader('Content-Type', 'application/json');
      res.send(200, translatedEvents);
    }).catch((err) => {
      req.log.error(new Date(), 'Error when fetching translations for events:', err);
      res.send(500, 'Error when fetching events.');
    });
    
  }).catch((err) => {
    req.log.error(new Date(), 'Error when fetching events:', err);
    res.send(500, 'Error when fetching events.');
  });
}

exports.adminSinglePartyEvents = (req, res) => {
  if (req.params.party.length < 3) {
    req.log.error(new Date(), 'Party', req.params.party, 'does not match requirements and cannot exist.');
    res.send(404, 'Defined party does not meet requirements: ' + req.params.party);
  }

  models.event.findAll({
    where: {
      party: req.params.party
    }
  }).then((events) => {
    Promise.all(events.map(addTranslations))
    .then((translatedEvents) => {
      res.setHeader('Content-Type', 'application/json');
      res.send(200, translatedEvents);
    }).catch((err) => {
      req.log.error(new Date(), 'Error when fetching translations for events in party', req.params.party + ':', err);
      res.send(500, 'Error when fetching events for party ' + req.params.party);
    });
  }).catch((err) => {
    req.log.error(new Date(), 'Error when fetching events for party', req.params.party + ':', err);
    res.send(500, 'Error when fetching events for party ' + req.params.party);
  });
}

exports.adminSingleEvent = (req, res) => {
  if (req.params.party.length < 3 || Number.isInteger(req.params.event)) {
    req.log.error(new Date(), 'Party', req.params.party, 'or event', req.params.event, 'does not match requirements and cannot exist.');
    res.send(404, 'Defined party or event does not meet requirements.');
  }

  models.event.findOne({
    where: {
      party: req.params.party,
      id: req.params.event
    }
  }).then((event) => {
    addTranslations(event)
    .then((translatedEvent) => {
      res.setHeader('Content-Type', 'application/json');
      res.send(200, translatedEvent);
    }).catch((err) => {
      req.log.error(new Date(), 'Error fetching translations for event', req.params.event, 'in party', req.params.party);
      res.send(500, 'Error when fetching event ' + req.params.event + ' for party ' + req.params.party);
    });
  }).catch((err) => {
    req.log.error(new Date(), 'Error fetching event', req.params.event, 'for party', req.params.party);
    res.send(500, 'Error when fetching event ' + req.params.event + ' for party ' + req.params.party);
  });
}

exports.adminTaggedEvents = (req, res) => {
  if (req.params.party.length < 3 || req.params.tags.length < 2) {
    req.log.error(new Date(), 'Party', req.params.party, 'or tags', req.params.tags, 'do not match requirements and cannot exist.');
    res.send(404, 'Defined party or tags do not meet requirements: ' + req.params.party);
  }

  var tags = stripTags(req.params.tags);

  models.event.findAll({
    where: {
      party: req.params.party,
      tags: { $like: '%' + tags[0] + '%' }
    }
  }).then((events) => {
    events = filterTaggedEvents(events, tags);

    Promise.all(events.map(addTranslations))
    .then((translatedEvents) => {
      res.setHeader('Content-Type', 'application/json');
      res.send(200, translatedEvents);
    }).catch((err) => {
      req.log.error(new Date(), 'Error when fetching translations for party', req.params.party + ':', err);
      res.send(500, 'Error when fetching events for party ' + req.params.party);
    });
  }).catch((err) => {
    req.log.error(new Date(), 'Error when fetching events for party', req.params.party + ' with tags ' + req.params.tags + ': ' + err);
    res.send(500, 'Error when fetching events for party ' + req.params.party);
  });
}