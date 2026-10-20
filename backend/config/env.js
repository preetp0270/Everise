/**
 * Central environment configuration.
 * Never commit real secrets — use Render dashboard / local .env.
 */
require('dotenv').config();

function required(name) {
  const value = process.env[name];
  if (!value && process.env.NODE_ENV === 'production') {
    throw new Error(`Missing required env var: ${name}`);
  }
  return value;
}

const env = {
  nodeEnv: process.env.NODE_ENV || 'development',
  port: parseInt(process.env.PORT || '3000', 10),
  mongodbUri: process.env.MONGODB_URI || 'mongodb://127.0.0.1:27017/everise',
  jwtAccessSecret: process.env.JWT_ACCESS_SECRET || 'dev-access-secret-change-in-prod',
  jwtRefreshSecret: process.env.JWT_REFRESH_SECRET || 'dev-refresh-secret-change-in-prod',
  accessTokenTtl: process.env.ACCESS_TOKEN_TTL || '15m',
  refreshTokenTtl: process.env.REFRESH_TOKEN_TTL || '30d',
  corsOrigins: (process.env.CORS_ORIGINS || 'http://localhost:5173')
    .split(',')
    .map((s) => s.trim())
    .filter(Boolean),
  rateLimitWindowMs: parseInt(process.env.RATE_LIMIT_WINDOW_MS || '900000', 10),
  rateLimitMax: parseInt(process.env.RATE_LIMIT_MAX || '100', 10),
  isProduction: process.env.NODE_ENV === 'production',
};

module.exports = { env, required };
