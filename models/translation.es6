export default function (sequelize, DataTypes) {
    const Translation = sequelize.define('translation', {
        type: {
            type: DataTypes.STRING,
            validate: { len: [3, 64] }
        },
        lang: {
            type: DataTypes.STRING,
            validate: { len: [1, 10] }
        },
        text: {
            type: DataTypes.STRING,
            validate: { len: [3, 4096] }
        }
    }, {
            classMethods: {
                associate: (models) => {
                    Translation.belongsTo(models.event, { as: 'event' })
                }
            }
        })
    return Translation
}