const mongoose = require('mongoose');

const focusSessionSchema = new mongoose.Schema(
  {
    userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, index: true },
    habitId: { type: mongoose.Schema.Types.ObjectId, ref: 'Habit', default: null },
    startedAt: { type: Date, required: true },
    endedAt: { type: Date, default: null },
    targetEndAt: { type: Date, default: null },
    durationSeconds: { type: Number, default: 0 },
    status: { type: String, enum: ['running', 'paused', 'completed', 'cancelled'], default: 'running' },
  },
  { timestamps: true }
);

module.exports = mongoose.model('FocusSession', focusSessionSchema);
