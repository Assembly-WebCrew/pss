export default function (sequelize, DataTypes) {
    const Event = sequelize.define('event', {
        url: {
            type: DataTypes.STRING,
            validate: { isUrl: true, len: [5, 512] }
        },
        stream_url: {
            type: DataTypes.STRING,
            validate: { isUrl: true, len: [5, 512] }
        },
        startsAt: {
            type: DataTypes.DATE,
            defaultValue: DataTypes.NOW
        },
        originallyStartsAt: {
            type: DataTypes.DATE,
            defaultValue: DataTypes.NOW
        },
        endsAt: {
            type: DataTypes.DATE,
            defaultValue: DataTypes.NOW
        },
        tags: {
            type: DataTypes.STRING,
            defaultValue: ""
        },
        startsAfter: {
            type: DataTypes.INTEGER,
            defaultValue: -1
        },
        public: {
            type: DataTypes.BOOLEAN,
            defaultValue: true
        }
    }, {
            classMethods: {
                associate: (models) => {
                    Event.hasMany(models.translation, { as: 'translations' }),
                        Event.belongsTo(models.party, { as: 'shortname', foreignKey: 'party' }),
                        Event.belongsTo(models.location, { as: 'location_id', foreignKey: 'location' })
                }
            }
        })
    return Event
}