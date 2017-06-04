export default function (sequelize, DataTypes) {
  const Location = sequelize.define( 'location', {
    location_id: { 
        type: DataTypes.STRING,
        validate: { len: [3,128] },
        allowNull: false
    },
    name: { 
        type: DataTypes.STRING,
        validate: { len: [3,128] }
    },
    name_fi: { 
        type: DataTypes.STRING,
        validate: { len: [3,128] }
    },
    url: { 
        type: DataTypes.STRING,
        validate: { isUrl: true, len: [5,512] }
    },
    coordinates: {
        type: DataTypes.GEOMETRY('POINT')
    }
  },{
    classMethods: {
        associate: (models) => {
            Location.belongsTo(models.party, { as: 'party'})
        }
    }
  })
  return Location
}