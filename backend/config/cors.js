const cors = require('cors');
const { env } = require('./env');

const corsOptions = {
  origin(origin, callback) {
    // Allow non-browser clients (mobile apps, curl, Postman) — no Origin header
    if (!origin) {
      return callback(null, true);
    }
    if (env.corsOrigins.includes(origin) || env.nodeEnv === 'development') {
      return callback(null, true);
    }
    return callback(new Error(`CORS blocked for origin: ${origin}`));
  },
  credentials: true,
  methods: ['GET', 'POST', 'PUT', 'PATCH', 'DELETE', 'OPTIONS'],
  allowedHeaders: ['Content-Type', 'Authorization', 'X-Request-Id', 'X-Idempotency-Key'],
};

module.exports = { corsMiddleware: cors(corsOptions) };
