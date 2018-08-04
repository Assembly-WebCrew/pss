'use strict';

const Model = require('objection').Model;

class Party extends Model {
    static get tableName() {
        return 'parties';
    }

    static get jsonSchema() {
        return {
            type: 'object',
            required: ['shortname', 'name'],

            properties: {
                id: { type: 'integer' },
                shortname: { type: 'string', minLength: 1, maxLength: 128 },
                name: { type: 'string', minLength: 1, maxLength: 128 },
                url: { type: 'string', minLength: 1, maxLength: 256, format: 'url' },
                startsAt: { type: 'string', format: 'date-time' },
                endsAt: { type: 'string', format: 'date-time' },
                public: { type: ['boolean', true] }
            }
        };
    }

    static get relationMappings() {
        return {
            locations: {
                relation: Model.HasManyRelation,
                modelClass: __dirname + '/location',
                join: {
                    from: 'parties.id',
                    to: 'locations.partyId'
                }
            },
            events: {
                relation: Model.HasManyRelation,
                modelClass: __dirname + '/event',
                join: {
                    from: 'parties.id',
                    to: 'events.partyId'
                }
            }
        };
    }
}

module.exports = Party;