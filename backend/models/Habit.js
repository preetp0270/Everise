const mongoose = require('mongoose');

const scheduleSchema = new mongoose.Schema(
  {
    type: {
      type: String,
      enum: ['daily', 'weekdays', 'weekly', 'custom'],
      default: 'daily',
    },
    daysOfWeek: {
      type: [Number], // 0=Sun .. 6=Sat
      default: [],
    },
    interval: {
      type: Number,
      min: 1,
      default: 1,
    },
  },
  { _id: false }
);

const targetSchema = new mongoose.Schema(
  {
    type: {
      type: String,
      enum: ['checkbox', 'duration', 'quantity', 'reps'],
      default: 'checkbox',
    },
    value: { type: Number, default: 1 },
    unit: { type: String, default: '' },
  },
  { _id: false }
);

const reminderSchema = new mongoose.Schema(
  {
    enabled: { type: Boolean, default: false },
    times: { type: [String], default: [] }, // "HH:mm"
    days: { type: [Number], default: [] },
  },
  { _id: false }
);

const habitSchema = new mongoose.Schema(
  {
    userId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'User',
      required: true,
      index: true,
    },
    name: { type: String, required: true, trim: true, maxlength: 120 },
    description: { type: String, default: '', maxlength: 1000 },
    icon: { type: String, default: 'spa' },
    color: { type: String, default: '#5D9FC6' },
    category: {
      type: String,
      default: 'personal_development',
      maxlength: 64,
    },
    schedule: { type: scheduleSchema, default: () => ({}) },
    target: { type: targetSchema, default: () => ({}) },
    reminder: { type: reminderSchema, default: () => ({}) },
    startDate: { type: String, required: true }, // YYYY-MM-DD
    endDate: { type: String, default: null },
    isArchived: { type: Boolean, default: false },
    isPinned: { type: Boolean, default: false },
    priority: { type: Number, default: 0 },
    order: { type: Number, default: 0 },
    implementationIntention: { type: String, default: '', maxlength: 500 },
    stackedAfterHabitId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Habit',
      default: null,
    },
    minVersion: { type: String, default: '', maxlength: 200 },
    isNegative: { type: Boolean, default: false }, // reduce unwanted habit
    syncVersion: { type: Number, default: 1 },
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

habitSchema.index({ userId: 1, isArchived: 1, order: 1 });
habitSchema.index({ userId: 1, isPinned: -1 });

module.exports = mongoose.model('Habit', habitSchema);
