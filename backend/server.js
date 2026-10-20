/**
 * Everise API entry point.
 * Binds to process.env.PORT for Render compatibility.
 */
if (process.env.NODE_ENV !== 'production') {
  require('dotenv').config();
}

const dns = require('dns');
// Helps Atlas SRV resolution on some local / ISP DNS setups
dns.setServers(['8.8.8.8', '8.8.4.4']);

const express = require('express');
const helmet = require('helmet');
const morgan = require('morgan');
const rateLimit = require('express-rate-limit');

const { env } = require('./config/env');
const { connectDB, disconnectDB } = require('./config/db');
const { corsMiddleware } = require('./config/cors');
const { errorHandler } = require('./middleware/errorHandler');
const { startKeepAlive } = require('./jobs/keepAlive');

const healthRoutes = require('./routes/health');
const authRoutes = require('./routes/auth');
const habitRoutes = require('./routes/habits');
const habitLogRoutes = require('./routes/habitLogs');
const analyticsRoutes = require('./routes/analytics');

const app = express();

// Security & parsing
app.use(helmet());
app.use(corsMiddleware);
app.use(express.json({ limit: '1mb' }));
app.use(express.urlencoded({ extended: true }));

if (env.nodeEnv !== 'test') {
  app.use(morgan(env.isProduction ? 'combined' : 'dev'));
}

app.use(
  rateLimit({
    windowMs: env.rateLimitWindowMs,
    max: env.rateLimitMax,
    standardHeaders: true,
    legacyHeaders: false,
  })
);

// Lightweight ping for uptime monitors / keep-alive (no DB required)
app.get('/ping', (req, res) => {
  res.status(200).json({
    success: true,
    data: {
      status: 'ok',
      service: 'everise-api',
      timestamp: new Date().toISOString(),
      uptimeSeconds: Math.floor(process.uptime()),
    },
  });
});

// API v1
app.use('/api/v1/health', healthRoutes);
app.use('/api/v1/auth', authRoutes);
app.use('/api/v1/habits', habitRoutes);
app.use('/api/v1/habit-logs', habitLogRoutes);
app.use('/api/v1/analytics', analyticsRoutes);

app.get('/', (req, res) => {
  res.send('🚀 Everise Backend is Running...');
});

// 404
app.use((req, res) => {
  res.status(404).json({
    success: false,
    error: { code: 'NOT_FOUND', message: `Route ${req.method} ${req.path} not found` },
  });
});

// Central error handler (must be last)
app.use(errorHandler);

async function start() {
  try {
    if (!process.env.MONGODB_URI) {
      console.error('❌ MONGODB_URI is missing — set it in .env or Render env vars');
    }
    if (!process.env.JWT_ACCESS_SECRET) {
      console.warn('⚠️  JWT_ACCESS_SECRET missing — using dev fallback (not for production)');
    }
    if (!process.env.JWT_REFRESH_SECRET) {
      console.warn('⚠️  JWT_REFRESH_SECRET missing — using dev fallback (not for production)');
    }

    await connectDB();

    const server = app.listen(env.port, '0.0.0.0', () => {
      console.log(`🚀 Everise API running on port ${env.port} (${env.nodeEnv})`);
      startKeepAlive();
    });

    const shutdown = async (signal) => {
      console.log(`⚠️  ${signal} received — shutting down`);
      server.close(async () => {
        await disconnectDB();
        process.exit(0);
      });
    };
    process.on('SIGTERM', () => shutdown('SIGTERM'));
    process.on('SIGINT', () => shutdown('SIGINT'));
  } catch (err) {
    console.error('❌ Failed to start server:', err.message || err);
    process.exit(1);
  }
}

if (require.main === module) {
  start();
}

module.exports = { app };
