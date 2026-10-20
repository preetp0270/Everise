const mongoose = require('mongoose');

const userSchema = new mongoose.Schema(
  {
    email: {
      type: String,
      required: true,
      unique: true,
      lowercase: true,
      trim: true,
      maxlength: 254,
    },
    passwordHash: {
      type: String,
      required: true,
      select: false, // never return by default
    },
    displayName: {
      type: String,
      trim: true,
      maxlength: 80,
      default: '',
    },
    avatarUrl: {
      type: String,
      default: null,
    },
    timezone: {
      type: String,
      default: 'UTC',
    },
    weekStartsOn: {
      type: Number,
      min: 0,
      max: 6,
      default: 1, // Monday
    },
    themePreference: {
      type: String,
      enum: ['system', 'light', 'dark'],
      default: 'system',
    },
    isGuest: {
      type: Boolean,
      default: false,
    },
    deletedAt: {
      type: Date,
      default: null,
    },
  },
  {
    timestamps: true,
    toJSON: {
      transform(_doc, ret) {
        ret.id = ret._id.toString();
        delete ret._id;
        delete ret.__v;
        delete ret.passwordHash;
        return ret;
      },
    },
  }
);

userSchema.index({ email: 1 });
userSchema.index({ deletedAt: 1 });

module.exports = mongoose.model('User', userSchema);
