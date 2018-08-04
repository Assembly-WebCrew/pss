'use strict';

const Model = require('objection').Model;

class Location extends Model {
    static get tableName() {
        return 'locations';
    }

    static get jsonSchema() {
        return {
            type: 'object',
            required: ['nameEnglish', 'partyId'],

            properties: {
                id: { type: 'integer' },
                partyId: { type: ['integer', 'null'] },
                nameEnglish: { type: 'string', minLength: 1, maxLength: 128 },
                nameFinnish: { type: 'string', minLength: 1, maxLength: 128 },
                descEnglish: { type: 'string', minLength: 1, maxLength: 2048 },
                descFinnish: { type: 'string', minLength: 1, maxLength: 2048 },
                url: { type: 'string', minLength: 1, maxLength: 256, format: 'url' },
                coordinates: { type: 'string', minLength: 1, maxLength: 32 },
                public: { type: ['boolean', true] }
            }
        };
    }

    static get relationMappings() {
        return {
            party: {
                relation: Model.BelongsToOneRelation,
                modelClass: __dirname + '/party',
                join: {
                    from: 'locations.partyId',
                    to: 'parties.id'
                }
            },
            events: {
                relation: Model.HasManyRelation,
                modelClass: __dirname + '/event',
                join: {
                    from: 'locations.id',
                    to: 'events.locationId'
                }
            }
        };
    }
}

module.exports = Location;