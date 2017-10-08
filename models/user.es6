var bcrypt = require("bcrypt");

module.exports = function (sequelize, DataTypes) {

    var User = sequelize.define('user', {
        annotation_id: {
            type: DataTypes.INTEGER,
            autoIncrement: true,
            primaryKey: true
        },
        username: {
            type: DataTypes.STRING,
            validate: { len: [3, 64] }
        },
        email: {
            type: DataTypes.STRING,
            validate: { isEmail: true, len: [5, 128] }
        },
        password: DataTypes.STRING
    },
        {
            instanceMethods: {
                generateHash: function (password) {
                    return bcrypt.hashSync(password, bcrypt.genSaltSync(8), null);
                },
                validPassword: function (password) {
                    return bcrypt.compareSync(password, this.password);
                },
            }
        });

    return User;
}