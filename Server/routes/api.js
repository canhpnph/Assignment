var express = require('express');
var router = express.Router();

var loverController = require('../controllers/api/api_lover');
var userController = require('../controllers/api/api_user');
var check_login = require('../middleware/user.middleware');
var middleware = require('../middleware/api-auth');
var messageControll = require('../controllers/api/api_message');

var multer = require('multer');
var uploader = multer({dest: '.tmp'});

router.use((req, res, next) => {
    next();
})

// router lover
router.get('/list-lover', check_login.check_login, loverController.list_Lover);
router.post('/add-lover', check_login.check_login, uploader.single('image'), loverController.add_Lover);
router.put('/update-lover-:id', check_login.check_login, uploader.single('image'), loverController.updateLover);
router.delete('/delete-lover-:id', check_login.check_login, loverController.deleteLover);

// router lover type
router.get('/list-loverType', check_login.check_login, loverController.listOfLoverType);
router.post('/add-loverType', check_login.check_login, loverController.add_LoverType);
router.put('/update-loverType-:id', check_login.check_login, loverController.updateLoverType);
router.delete('/delete-loverType-:id', check_login.check_login, loverController.deleteLoverType);


// router user
router.get('/list-user', check_login.check_login, userController.list_user);
router.post('/register', userController.registerUser);
router.post('/login', userController.login);
router.post('/check-username', userController.checkUsername);

router.post('/login-success', check_login.check_login, userController.login_success);

router.get('/profile', middleware.api_auth, userController.profile);
router.put('/update-info-:id', middleware.api_auth, uploader.single('image'), userController.updateInfoUser);
router.put('/update-password-:id', middleware.api_auth, userController.updatePasswordUser);
router.post('/logout', middleware.api_auth, userController.logout);

// router message
router.get('/list-message', messageControll.listMessage );
router.post('/add-message', messageControll.addNewMessage);

module.exports = router;