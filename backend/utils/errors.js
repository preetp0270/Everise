/**
 * Application error helpers — consistent API error shape.
 */

class AppError extends Error {
  constructor(message, status = 500, code = 'INTERNAL_ERROR', details = null) {
    super(message);
    this.status = status;
    this.code = code;
    this.details = details;
    this.isOperational = true;
  }
}

function badRequest(message, details = null) {
  return new AppError(message, 400, 'BAD_REQUEST', details);
}

function unauthorized(message = 'Unauthorized') {
  return new AppError(message, 401, 'UNAUTHORIZED');
}

function forbidden(message = 'Forbidden') {
  return new AppError(message, 403, 'FORBIDDEN');
}

function notFound(message = 'Resource not found') {
  return new AppError(message, 404, 'NOT_FOUND');
}

function conflict(message, details = null) {
  return new AppError(message, 409, 'CONFLICT', details);
}

function validationError(message, details = null) {
  return new AppError(message, 422, 'VALIDATION_ERROR', details);
}

function tooManyRequests(message = 'Too many requests') {
  return new AppError(message, 429, 'RATE_LIMITED');
}

module.exports = {
  AppError,
  badRequest,
  unauthorized,
  forbidden,
  notFound,
  conflict,
  validationError,
  tooManyRequests,
};
