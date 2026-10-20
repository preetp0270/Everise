const authService = require('../services/authService');
const {
  registerSchema,
  loginSchema,
  refreshSchema,
  changePasswordSchema,
  parseOrThrow,
} = require('../validators/auth');

function ok(res, data, status = 200) {
  return res.status(status).json({ success: true, data });
}

async function register(req, res, next) {
  try {
    const body = parseOrThrow(registerSchema, req.body);
    const result = await authService.register(body);
    return ok(res, result, 201);
  } catch (err) {
    next(err);
  }
}

async function login(req, res, next) {
  try {
    const body = parseOrThrow(loginSchema, req.body);
    const result = await authService.login(body);
    return ok(res, result);
  } catch (err) {
    next(err);
  }
}

async function refresh(req, res, next) {
  try {
    const body = parseOrThrow(refreshSchema, req.body);
    const result = await authService.refresh(body);
    return ok(res, result);
  } catch (err) {
    next(err);
  }
}

async function logout(req, res, next) {
  try {
    const refreshToken = req.body?.refreshToken;
    await authService.logout(refreshToken);
    return ok(res, { message: 'Logged out' });
  } catch (err) {
    next(err);
  }
}

async function logoutAll(req, res, next) {
  try {
    await authService.logoutAll(req.userId);
    return ok(res, { message: 'All sessions revoked' });
  } catch (err) {
    next(err);
  }
}

async function changePassword(req, res, next) {
  try {
    const body = parseOrThrow(changePasswordSchema, req.body);
    const result = await authService.changePassword(req.userId, body);
    return ok(res, result);
  } catch (err) {
    next(err);
  }
}

async function deleteAccount(req, res, next) {
  try {
    const result = await authService.deleteAccount(req.userId);
    return ok(res, result);
  } catch (err) {
    next(err);
  }
}

async function me(req, res, next) {
  try {
    const user = await authService.getMe(req.userId);
    return ok(res, { user });
  } catch (err) {
    next(err);
  }
}

module.exports = {
  register,
  login,
  refresh,
  logout,
  logoutAll,
  changePassword,
  deleteAccount,
  me,
};
