const { z } = require('zod');

const emailSchema = z
  .string()
  .trim()
  .toLowerCase()
  .email('Invalid email address')
  .max(254);

const passwordSchema = z
  .string()
  .min(8, 'Password must be at least 8 characters')
  .max(128, 'Password is too long')
  .regex(/[A-Za-z]/, 'Password must contain a letter')
  .regex(/[0-9]/, 'Password must contain a number');

const registerSchema = z.object({
  email: emailSchema,
  password: passwordSchema,
  displayName: z.string().trim().max(80).optional().default(''),
  timezone: z.string().max(64).optional().default('UTC'),
});

const loginSchema = z.object({
  email: emailSchema,
  password: z.string().min(1, 'Password is required').max(128),
  deviceInfo: z.string().max(256).optional().default(''),
});

const refreshSchema = z.object({
  refreshToken: z.string().min(20, 'Invalid refresh token'),
  deviceInfo: z.string().max(256).optional().default(''),
});

const changePasswordSchema = z.object({
  currentPassword: z.string().min(1),
  newPassword: passwordSchema,
});

function parseOrThrow(schema, data) {
  const result = schema.safeParse(data);
  if (!result.success) {
    const details = result.error.issues.map((i) => ({
      path: i.path.join('.'),
      message: i.message,
    }));
    const err = new Error('Validation failed');
    err.status = 422;
    err.code = 'VALIDATION_ERROR';
    err.details = details;
    throw err;
  }
  return result.data;
}

module.exports = {
  registerSchema,
  loginSchema,
  refreshSchema,
  changePasswordSchema,
  parseOrThrow,
};
