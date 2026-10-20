const Habit = require('../models/Habit');
const HabitLog = require('../models/HabitLog');
const { notFound, forbidden, conflict } = require('../utils/errors');

async function listHabits(userId, { archived = false } = {}) {
  return Habit.find({ userId, isArchived: archived })
    .sort({ isPinned: -1, order: 1, createdAt: -1 })
    .lean({ virtuals: true })
    .then((docs) => docs.map(shapeHabit));
}

async function getHabit(userId, id) {
  const habit = await Habit.findById(id);
  if (!habit) throw notFound('Habit not found');
  if (habit.userId.toString() !== userId) throw forbidden();
  return shapeHabit(habit.toJSON());
}

async function createHabit(userId, data) {
  const habit = await Habit.create({ ...data, userId });
  return shapeHabit(habit.toJSON());
}

async function updateHabit(userId, id, data) {
  const habit = await Habit.findById(id);
  if (!habit) throw notFound('Habit not found');
  if (habit.userId.toString() !== userId) throw forbidden();
  Object.assign(habit, data);
  habit.syncVersion = (habit.syncVersion || 1) + 1;
  await habit.save();
  return shapeHabit(habit.toJSON());
}

async function archiveHabit(userId, id) {
  const habit = await Habit.findById(id);
  if (!habit) throw notFound('Habit not found');
  if (habit.userId.toString() !== userId) throw forbidden();
  habit.isArchived = true;
  await habit.save();
  return shapeHabit(habit.toJSON());
}

async function restoreHabit(userId, id) {
  const habit = await Habit.findById(id);
  if (!habit) throw notFound('Habit not found');
  if (habit.userId.toString() !== userId) throw forbidden();
  habit.isArchived = false;
  await habit.save();
  return shapeHabit(habit.toJSON());
}

async function deleteHabit(userId, id) {
  const habit = await Habit.findById(id);
  if (!habit) throw notFound('Habit not found');
  if (habit.userId.toString() !== userId) throw forbidden();
  await HabitLog.deleteMany({ habitId: id, userId });
  await habit.deleteOne();
  return { message: 'Deleted' };
}

async function createLog(userId, data) {
  const habit = await Habit.findById(data.habitId);
  if (!habit) throw notFound('Habit not found');
  if (habit.userId.toString() !== userId) throw forbidden();

  // Idempotent upsert by user+habit+date
  const existing = await HabitLog.findOne({
    userId,
    habitId: data.habitId,
    occurrenceDate: data.occurrenceDate,
  });

  if (existing) {
    if (existing.idempotencyKey === data.idempotencyKey) {
      return shapeLog(existing.toJSON());
    }
    existing.status = data.status;
    existing.value = data.value ?? existing.value;
    existing.note = data.note ?? existing.note;
    existing.idempotencyKey = data.idempotencyKey;
    existing.completedAt = data.status === 'completed' ? new Date() : null;
    await existing.save();
    return shapeLog(existing.toJSON());
  }

  try {
    const log = await HabitLog.create({
      userId,
      habitId: data.habitId,
      occurrenceDate: data.occurrenceDate,
      status: data.status,
      value: data.value ?? null,
      note: data.note || '',
      idempotencyKey: data.idempotencyKey,
      completedAt: data.status === 'completed' ? new Date() : null,
    });
    return shapeLog(log.toJSON());
  } catch (err) {
    if (err.code === 11000) {
      const again = await HabitLog.findOne({
        userId,
        habitId: data.habitId,
        occurrenceDate: data.occurrenceDate,
      });
      if (again) return shapeLog(again.toJSON());
      throw conflict('Duplicate log');
    }
    throw err;
  }
}

async function listLogs(userId, { habitId, from, to } = {}) {
  const q = { userId };
  if (habitId) q.habitId = habitId;
  if (from || to) {
    q.occurrenceDate = {};
    if (from) q.occurrenceDate.$gte = from;
    if (to) q.occurrenceDate.$lte = to;
  }
  const logs = await HabitLog.find(q).sort({ occurrenceDate: -1 }).lean();
  return logs.map(shapeLog);
}

async function deleteLog(userId, id) {
  const log = await HabitLog.findById(id);
  if (!log) throw notFound('Log not found');
  if (log.userId.toString() !== userId) throw forbidden();
  await log.deleteOne();
  return { message: 'Undone' };
}

function shapeHabit(h) {
  if (!h) return h;
  const id = h.id || (h._id && h._id.toString());
  return { ...h, id, _id: undefined, __v: undefined };
}

function shapeLog(l) {
  if (!l) return l;
  const id = l.id || (l._id && l._id.toString());
  return { ...l, id, _id: undefined, __v: undefined };
}

module.exports = {
  listHabits,
  getHabit,
  createHabit,
  updateHabit,
  archiveHabit,
  restoreHabit,
  deleteHabit,
  createLog,
  listLogs,
  deleteLog,
};
