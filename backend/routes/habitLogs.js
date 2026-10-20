const express = require('express');
const habitController = require('../controllers/habitController');
const { requireAuth } = require('../middleware/auth');

const router = express.Router();
router.use(requireAuth);

router.get('/', habitController.listLogs);
router.post('/', habitController.createLog);
router.delete('/:id', habitController.deleteLog);

module.exports = router;
