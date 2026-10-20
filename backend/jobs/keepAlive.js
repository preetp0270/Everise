/**
 * Optional self-ping to reduce Render free-tier cold starts.
 *
 * Option A (Render): RENDER_EXTERNAL_URL is set automatically by Render.
 * Option B (manual): set KEEP_ALIVE_URL in .env, e.g.
 *   KEEP_ALIVE_URL=http://localhost:3000/ping          (local test)
 *   KEEP_ALIVE_URL=https://your-app.onrender.com/ping  (production)
 *
 * Optional:
 *   KEEP_ALIVE_ENABLED=true|false   (default: on when a URL is available)
 *   KEEP_ALIVE_INTERVAL_MS=300000   (default 14 min on Render, 30s for local test if unset with localhost)
 */
function resolveKeepAliveUrl() {
  if (process.env.KEEP_ALIVE_URL) {
    return process.env.KEEP_ALIVE_URL.trim();
  }
  if (process.env.RENDER_EXTERNAL_URL) {
    return `${process.env.RENDER_EXTERNAL_URL.replace(/\/$/, '')}/ping`;
  }
  return null;
}

function startKeepAlive() {
  const enabled = (process.env.KEEP_ALIVE_ENABLED || 'true').toLowerCase() !== 'false';
  if (!enabled) {
    console.log('ℹ️  Keep-alive disabled (KEEP_ALIVE_ENABLED=false)');
    return;
  }

  const base = resolveKeepAliveUrl();
  if (!base) {
    console.log('ℹ️  Keep-alive off — add this to backend/.env for Option B:');
    console.log('   KEEP_ALIVE_URL=http://localhost:3000/ping');
    console.log('   KEEP_ALIVE_INTERVAL_MS=30000');
    console.log('   (On Render, set KEEP_ALIVE_URL=https://YOUR-SERVICE.onrender.com/ping)');
    return;
  }

  const isLocal = /localhost|127\.0\.0\.1/.test(base);
  const defaultInterval = isLocal ? 30_000 : 14 * 60 * 1000;
  const intervalMs = parseInt(
    process.env.KEEP_ALIVE_INTERVAL_MS || String(defaultInterval),
    10
  );
  const firstDelayMs = isLocal ? 3_000 : 60_000;

  const ping = async () => {
    try {
      const res = await fetch(base, { method: 'GET' });
      console.log(`💓 Keep-alive ping ${base} → ${res.status}`);
    } catch (err) {
      console.warn(`⚠️  Keep-alive ping failed: ${err.message}`);
    }
  };

  console.log(`✅ Keep-alive (Option B) started`);
  console.log(`   URL: ${base}`);
  console.log(`   First ping in ${Math.round(firstDelayMs / 1000)}s, then every ${Math.round(intervalMs / 1000)}s`);

  setTimeout(ping, firstDelayMs);
  setInterval(ping, intervalMs);
}

module.exports = { startKeepAlive, resolveKeepAliveUrl };
