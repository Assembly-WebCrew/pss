export default function (sequelize, DataTypes) {
  const Party = sequelize.define( 'party', {
    shortname: { 
        type: DataTypes.STRING,
        validate: { len: [3,64] },
        primaryKey: true,
        allowNull: false
    },
    name: { 
        type: DataTypes.STRING,
        validate: { len: [3,128] }
    },
    website: { 
        type: DataTypes.STRING,
        validate: { isUrl: true, len: [5,512] }
    },
    startsAt: {
        type: DataTypes.DATE,
        defaultValue: DataTypes.NOW
    },
    endsAt: {
        type: DataTypes.DATE,
        defaultValue: DataTypes.NOW
    },
    public: {
        type: DataTypes.BOOLEAN,
        defaultValue: true
    }
  })
  return Party
}