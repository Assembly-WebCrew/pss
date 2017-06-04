export default function (sequelize, DataTypes) {
  const Event = sequelize.define( 'event', {
    name: { 
        type: DataTypes.STRING,
        validate: { len: [3,128] }
    },
    name_fi: { 
        type: DataTypes.STRING,
        validate: { len: [3,128] }
    },
    description: {
        type: DataTypes.STRING,
        validate: { len: [3,4096] }
    },
    description_fi: {
        type: DataTypes.STRING,
        validate: { len: [3,4096] }
    },
    url: { 
        type: DataTypes.STRING,
        validate: { isUrl: true, len: [5,512] }
    },
    stream_url: {
        type: DataTypes.STRING,
        validate: { isUrl: true, len: [5,512] }
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
        Type: DataTimes.STRING,
        defaultValue: ""
    }
  },{
    classMethods: {
        associate: (models) => {
            Event.belongsTo(models.location, { as: 'location_id' }),
            Event.belongsTo(models.party, { as: 'party' }),
            Event.hasOne(models.event, { as: 'startsAfter' })
        }
    }
  })
  return Event
}