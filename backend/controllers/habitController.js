const habitService = require('../services/habitService');
const {
  createHabitSchema,
  updateHabitSchema,
  createLogSchema,
  parseOrThrow,
} = require('../validators/habit');

function ok(res, data, status = 200) {
  return res.status(status).json({ success: true, data });
}

async function list(req, res, next) {
  try {
    const archived = req.query.archived === 'true';
    const data = await habitService.listHabits(req.userId, { archived });
    return ok(res, data);
  } catch (e) {
    next(e);
  }
}

async function get(req, res, next) {
  try {
    const data = await habitService.getHabit(req.userId, req.params.id);
    return ok(res, data);
  } catch (e) {
    next(e);
  }
}

async function create(req, res, next) {
  try {
    const body = parseOrThrow(createHabitSchema, req.body);
    const data = await habitService.createHabit(req.userId, body);
    return ok(res, data, 201);
  } catch (e) {
    next(e);
  }
}

async function update(req, res, next) {
  try {
    const body = parseOrThrow(updateHabitSchema, req.body);
    const data = await habitService.updateHabit(req.userId, req.params.id, body);
    return ok(res, data);
  } catch (e) {
    next(e);
  }
}

async function archive(req, res, next) {
  try {
    const data = await habitService.archiveHabit(req.userId, req.params.id);
    return ok(res, data);
  } catch (e) {
    next(e);
  }
}

async function restore(req, res, next) {
  try {
    const data = await habitService.restoreHabit(req.userId, req.params.id);
    return ok(res, data);
  } catch (e) {
    next(e);
  }
}

async function remove(req, res, next) {
  try {
    const data = await habitService.deleteHabit(req.userId, req.params.id);
    return ok(res, data);
  } catch (e) {
    next(e);
  }
}

async function createLog(req, res, next) {
  try {
    const body = parseOrThrow(createLogSchema, req.body);
    const data = await habitService.createLog(req.userId, body);
    return ok(res, data, 201);
  } catch (e) {
    next(e);
  }
}

async function listLogs(req, res, next) {
  try {
    const data = await habitService.listLogs(req.userId, {
      habitId: req.query.habitId,
      from: req.query.from,
      to: req.query.to,
    });
    return ok(res, data);
  } catch (e) {
    next(e);
  }
}

async function deleteLog(req, res, next) {
  try {
    const data = await habitService.deleteLog(req.userId, req.params.id);
    return ok(res, data);
  } catch (e) {
    next(e);
  }
}

module.exports = {
  list,
  get,
  create,
  update,
  archive,
  restore,
  remove,
  createLog,
  listLogs,
  deleteLog,
};
