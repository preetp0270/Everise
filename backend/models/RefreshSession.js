const mongoose = require('mongoose');

/**
 * Stores hashed refresh tokens only.
 * Supports rotation, revocation, and reuse detection.
 */
const refreshSessionSchema = new mongoose.Schema(
  {
    userId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'User',
      required: true,
      index: true,
    },
    tokenHash: {
      type: String,
      required: true,
      unique: true,
    },
    deviceInfo: {
      type: String,
      maxlength: 256,
      default: '',
    },
    expiresAt: {
      type: Date,
      required: true,
    },
    revokedAt: {
      type: Date,
      default: null,
    },
    replacedByHash: {
      type: String,
      default: null,
    },
    lastUsedAt: {
      type: Date,
      default: Date.now,
    },
  },
  { timestamps: true }
);

refreshSessionSchema.index({ userId: 1, revokedAt: 1 });
refreshSessionSchema.index({ expiresAt: 1 }, { expireAfterSeconds: 0 }); // TTL optional cleanup

module.exports = mongoose.model('RefreshSession', refreshSessionSchema);
