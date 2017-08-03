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
    language: {
        type: DataTypes.ENUM,
        values: ["fi", "en", "se", "ru", "fi+en"]
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
        type: DataTypes.STRING,
        defaultValue: ""
    },
    location: {
        type: DataTypes.STRING,
        validate: { len: [3,30] }
    },
    party: {
        type: DataTypes.STRING,
        validate: { len: [3,15] }
    },
    startsAfter: {
        type: DataTypes.INTEGER
    }
  })
  return Event
}