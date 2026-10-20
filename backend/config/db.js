const mongoose = require('mongoose');
const { env } = require('./env');

let isConnected = false;

async function connectDB() {
  if (isConnected) return mongoose.connection;

  mongoose.set('strictQuery', true);

  try {
    await mongoose.connect(env.mongodbUri, {
      serverSelectionTimeoutMS: 10000,
    });
    isConnected = true;
    console.log('[db] Connected to MongoDB');
    return mongoose.connection;
  } catch (err) {
    console.error('[db] Connection error:', err.message);
    throw err;
  }
}

async function disconnectDB() {
  if (!isConnected) return;
  await mongoose.disconnect();
  isConnected = false;
  console.log('[db] Disconnected');
}

module.exports = { connectDB, disconnectDB };
