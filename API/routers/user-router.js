'use strict';

module.exports = function (app, dbConnection) {
    let userController = require('../controllers/user-controller')

    app.route('/api/user/login')
        .post(userController(dbConnection).login);
        
    app.route('/api/user/signup')
        .post(userController(dbConnection).signup);
        
    app.route('/api/user/get-user-by-username')
        .get(userController(dbConnection).getUserByUsername);
        
    app.route('/api/user/update-user-info')
        .post(userController(dbConnection).updateUserInfo);
        
    app.route('/api/user/change-password')
        .post(userController(dbConnection).changePassword);
};