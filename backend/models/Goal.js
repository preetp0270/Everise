const mongoose = require('mongoose');

const goalSchema = new mongoose.Schema(
  {
    userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, index: true },
    title: { type: String, required: true, maxlength: 200 },
    description: { type: String, default: '', maxlength: 1000 },
    targetDate: { type: String, default: null },
    linkedHabitIds: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Habit' }],
    status: { type: String, enum: ['active', 'completed', 'archived'], default: 'active' },
    progress: { type: Number, default: 0 },
  },
  { timestamps: true }
);

module.exports = mongoose.model('Goal', goalSchema);
