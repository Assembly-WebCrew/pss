'use strict';

const Model = require('objection').Model;

class Event extends Model {
    static get tableName() {
        return 'events';
    }

    static get jsonSchema() {
        return {
            type: 'object',
            required: ['nameEnglish', 'startsAt', 'duration'],

            properties: {
                id: { type: 'integer' },
                locationId: { type: ['integer', 'null'] },
                partyId: { type: ['integer', 'null'] },
                nameEnglish: { type: 'string', minLength: 1, maxLength: 128 },
                nameFinnish: { type: 'string', minLength: 1, maxLength: 128 },
                descEnglish: { type: 'string', minLength: 1, maxLength: 2048 },
                descFinnish: { type: 'string', minLength: 1, maxLength: 2048 },
                url: { type: 'string', minLength: 1, maxLength: 256, format: 'url'},
                streamUrl: { type: 'string', minLength: 1, maxLength: 256, format: 'url' },
                startsAt: { type: 'string', format: 'date-time' },
                originallyStartsAt: { type: 'string', format: 'date-time'},
                duration: { type: ['integer', '0'], description: 'Duration in minutes, eg. 1 hour = 60' },
                tags: { type: 'string', description: 'Comma-separated list of tags, eg. foo, bar', minLength: 1, maxLength: 512 },
                public: { type: ['boolean', true] },
                canceled: { type: ['boolean', false] }
            }
        };
    }

    static get relationMappings() {
        return {
            location: {
                relation: Model.BelongsToOneRelation,
                modelClass: __dirname + '/location',
                join: {
                    from: 'events.locationId',
                    to: 'locations.id'
                }
            },
            party: {
                relation: Model.BelongsToOneRelation,
                modelClass: __dirname + '/party',
                join: {
                    from: 'events.partyId',
                    to: 'parties.id'
                }
            }
        };
    }
}

module.exports = Event;