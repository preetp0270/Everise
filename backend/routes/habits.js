const express = require('express');
const habitController = require('../controllers/habitController');
const { requireAuth } = require('../middleware/auth');

const router = express.Router();
router.use(requireAuth);

router.get('/', habitController.list);
router.post('/', habitController.create);
router.get('/:id', habitController.get);
router.patch('/:id', habitController.update);
router.post('/:id/archive', habitController.archive);
router.post('/:id/restore', habitController.restore);
router.delete('/:id', habitController.remove);

module.exports = router;
