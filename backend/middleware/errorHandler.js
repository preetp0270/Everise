const { AppError } = require('../utils/errors');
const { env } = require('../config/env');

function errorHandler(err, req, res, next) {
  // Zod / custom validation shape
  if (err.code === 'VALIDATION_ERROR' || err.status === 422) {
    return res.status(422).json({
      success: false,
      error: {
        code: 'VALIDATION_ERROR',
        message: err.message || 'Validation failed',
        details: err.details || null,
      },
    });
  }

  if (err instanceof AppError || err.isOperational) {
    return res.status(err.status || 500).json({
      success: false,
      error: {
        code: err.code || 'ERROR',
        message: err.message,
        details: err.details || null,
      },
    });
  }

  // Mongoose duplicate key
  if (err.code === 11000) {
    return res.status(409).json({
      success: false,
      error: {
        code: 'CONFLICT',
        message: 'Resource already exists',
        details: err.keyValue || null,
      },
    });
  }

  console.error('[error]', err);
  const status = err.status || 500;
  res.status(status).json({
    success: false,
    error: {
      code: err.code || 'INTERNAL_ERROR',
      message:
        env.isProduction && status === 500
          ? 'Internal server error'
          : err.message || 'Internal server error',
    },
  });
}

module.exports = { errorHandler };
