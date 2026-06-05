<template>
  <div class="page-header">
    <div class="page-header__content">
      <h1 class="page-header__title">
        <el-icon v-if="icon" class="page-header__icon"><component :is="icon" /></el-icon>
        {{ title }}
      </h1>
      <p v-if="description" class="page-header__desc">{{ description }}</p>
    </div>
    <div v-if="$slots.actions" class="page-header__actions">
      <slot name="actions" />
    </div>
  </div>
</template>

<script setup>
defineProps({
  title: { type: String, required: true },
  description: { type: String, default: '' },
  icon: { type: String, default: '' }
})
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-6);
}

.page-header__title {
  margin: 0;
  font-size: var(--font-size-2xl);
  font-weight: 600;
  color: var(--text-primary);
  display: flex;
  align-items: center;
  gap: var(--space-3);
  letter-spacing: -0.3px;
}

.page-header__icon {
  color: var(--color-primary);
  font-size: var(--font-size-xl);
}

.page-header__desc {
  margin: var(--space-2) 0 0;
  font-size: var(--font-size-body);
  color: var(--text-muted);
}

.page-header__actions {
  display: flex;
  gap: var(--space-2);
}
</style>
