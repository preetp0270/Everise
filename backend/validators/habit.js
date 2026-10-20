const { z } = require('zod');

const scheduleSchema = z.object({
  type: z.enum(['daily', 'weekdays', 'weekly', 'custom']).default('daily'),
  daysOfWeek: z.array(z.number().int().min(0).max(6)).default([]),
  interval: z.number().int().min(1).default(1),
}).optional();

const targetSchema = z.object({
  type: z.enum(['checkbox', 'duration', 'quantity', 'reps']).default('checkbox'),
  value: z.number().default(1),
  unit: z.string().max(32).default(''),
}).optional();

const reminderSchema = z.object({
  enabled: z.boolean().default(false),
  times: z.array(z.string()).default([]),
  days: z.array(z.number().int()).default([]),
}).optional();

const createHabitSchema = z.object({
  name: z.string().trim().min(1).max(120),
  description: z.string().max(1000).optional().default(''),
  icon: z.string().max(64).optional().default('spa'),
  color: z.string().max(32).optional().default('#5D9FC6'),
  category: z.string().max(64).optional().default('personal_development'),
  schedule: scheduleSchema,
  target: targetSchema,
  reminder: reminderSchema,
  startDate: z.string().regex(/^\d{4}-\d{2}-\d{2}$/),
  endDate: z.string().regex(/^\d{4}-\d{2}-\d{2}$/).nullable().optional(),
  isPinned: z.boolean().optional().default(false),
  priority: z.number().int().optional().default(0),
  order: z.number().int().optional().default(0),
  isNegative: z.boolean().optional().default(false),
  implementationIntention: z.string().max(500).optional().default(''),
  minVersion: z.string().max(200).optional().default(''),
});

const updateHabitSchema = createHabitSchema.partial();

const createLogSchema = z.object({
  habitId: z.string().min(1),
  occurrenceDate: z.string().regex(/^\d{4}-\d{2}-\d{2}$/),
  status: z.enum(['completed', 'skipped', 'incomplete']),
  value: z.number().nullable().optional(),
  note: z.string().max(2000).optional().default(''),
  idempotencyKey: z.string().min(1).max(128),
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
  createHabitSchema,
  updateHabitSchema,
  createLogSchema,
  parseOrThrow,
};
