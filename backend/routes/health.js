const express = require('express');
const mongoose = require('mongoose');

const router = express.Router();

/**
 * GET /api/v1/health
 * Used by Render health checks and monitoring.
 */
router.get('/', (req, res) => {
  const dbState = mongoose.connection.readyState;
  // 0 = disconnected, 1 = connected, 2 = connecting, 3 = disconnecting
  const dbHealthy = dbState === 1;

  res.status(dbHealthy ? 200 : 503).json({
    success: dbHealthy,
    data: {
      status: dbHealthy ? 'ok' : 'degraded',
      service: 'everise-api',
      timestamp: new Date().toISOString(),
      uptimeSeconds: Math.floor(process.uptime()),
      database: dbHealthy ? 'connected' : 'disconnected',
    },
  });
});

module.exports = router;
