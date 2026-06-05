<template>
  <section
    class="glass-card"
    :class="{
      'glass-card--transparent': transparent,
      [`glass-card--pad-${padding}`]: true
    }"
  >
    <div v-if="title || $slots['header-actions']" class="glass-card__header">
      <div class="glass-card__title-wrap">
        <h3 v-if="title" class="glass-card__title">{{ title }}</h3>
        <p v-if="subtitle" class="glass-card__subtitle">{{ subtitle }}</p>
      </div>
      <div v-if="$slots['header-actions']" class="glass-card__actions">
        <slot name="header-actions" />
      </div>
    </div>
    <div class="glass-card__body">
      <slot />
    </div>
    <div v-if="$slots.footer" class="glass-card__footer">
      <slot name="footer" />
    </div>
  </section>
</template>

<script setup>
defineProps({
  title: { type: String, default: '' },
  subtitle: { type: String, default: '' },
  padding: { type: String, default: 'lg' },
  transparent: { type: Boolean, default: false }
})
</script>

<style scoped>
.glass-card {
  background: var(--bg-card);
  backdrop-filter: blur(var(--glass-blur-md));
  -webkit-backdrop-filter: blur(var(--glass-blur-md));
  border: 1px solid var(--border-glass-strong);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-md), inset 0 1px 0 hsla(255, 255, 255, 0.08);
  overflow: hidden;
}

.glass-card--transparent {
  background: transparent;
  backdrop-filter: none;
  -webkit-backdrop-filter: none;
  box-shadow: none;
  border-color: var(--border-glass-light);
}

.glass-card--pad-sm { padding: var(--space-4); }
.glass-card--pad-md { padding: var(--space-6); }
.glass-card--pad-lg { padding: var(--space-8); }

.glass-card__header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-6);
}

.glass-card__title {
  margin: 0;
  font-size: var(--font-size-lg);
  font-weight: 600;
  color: var(--text-primary);
  letter-spacing: -0.2px;
}

.glass-card__subtitle {
  margin: var(--space-1) 0 0;
  font-size: var(--font-size-sm);
  color: var(--text-muted);
}

.glass-card__actions {
  display: flex;
  gap: var(--space-2);
  align-items: center;
}

.glass-card__footer {
  margin-top: var(--space-6);
  padding-top: var(--space-4);
  border-top: 1px solid var(--border-glass-light);
}
</style>
