const express = require('express');
const Habit = require('../models/Habit');
const HabitLog = require('../models/HabitLog');
const { requireAuth } = require('../middleware/auth');

const router = express.Router();
router.use(requireAuth);

router.get('/summary', async (req, res, next) => {
  try {
    const userId = req.userId;
    const [activeHabits, completedLogs] = await Promise.all([
      Habit.countDocuments({ userId, isArchived: false }),
      HabitLog.countDocuments({ userId, status: 'completed' }),
    ]);
    res.json({
      success: true,
      data: {
        activeHabits,
        totalCompletions: completedLogs,
        message: 'Extended streak and heatmap analytics expand with more history',
      },
    });
  } catch (e) {
    next(e);
  }
});

module.exports = router;
