const mongoose = require('mongoose');

const habitLogSchema = new mongoose.Schema(
  {
    userId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'User',
      required: true,
      index: true,
    },
    habitId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Habit',
      required: true,
      index: true,
    },
    occurrenceDate: {
      type: String, // YYYY-MM-DD in user's timezone
      required: true,
    },
    status: {
      type: String,
      enum: ['completed', 'skipped', 'incomplete'],
      required: true,
    },
    value: { type: Number, default: null },
    note: { type: String, default: '', maxlength: 2000 },
    photoUrl: { type: String, default: null },
    idempotencyKey: {
      type: String,
      required: true,
      maxlength: 128,
    },
    completedAt: { type: Date, default: null },
  },
  {
    timestamps: true,
    toJSON: {
      transform(_doc, ret) {
        ret.id = ret._id.toString();
        delete ret._id;
        delete ret.__v;
        return ret;
      },
    },
  }
);

// One logical log per user+habit+date (idempotent retries use same key)
habitLogSchema.index(
  { userId: 1, habitId: 1, occurrenceDate: 1 },
  { unique: true }
);
habitLogSchema.index({ userId: 1, idempotencyKey: 1 }, { unique: true });
habitLogSchema.index({ userId: 1, occurrenceDate: 1 });

module.exports = mongoose.model('HabitLog', habitLogSchema);
