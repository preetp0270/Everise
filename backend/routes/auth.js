const express = require('express');
const authController = require('../controllers/authController');
const { requireAuth } = require('../middleware/auth');

const router = express.Router();

// Public
router.post('/register', authController.register);
router.post('/login', authController.login);
router.post('/refresh', authController.refresh);
router.post('/logout', authController.logout);

// Protected
router.get('/me', requireAuth, authController.me);
router.post('/logout-all', requireAuth, authController.logoutAll);
router.post('/password/change', requireAuth, authController.changePassword);
router.delete('/account', requireAuth, authController.deleteAccount);

module.exports = router;
