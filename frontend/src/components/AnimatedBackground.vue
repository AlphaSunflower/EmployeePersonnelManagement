<template>
  <div class="animated-bg">
    <div class="orb orb--1"></div>
    <div class="orb orb--2"></div>
    <div class="orb orb--3"></div>
    <div class="bg-gradient"></div>
    <div class="bg-grain"></div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.animated-bg {
  position: fixed;
  inset: 0;
  z-index: 0;
  overflow: hidden;
  pointer-events: none;
}

.bg-gradient {
  position: absolute;
  inset: 0;
  background: var(--bg-base-gradient);
}

.bg-grain {
  position: absolute;
  inset: 0;
  opacity: 0.03;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.85' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)' opacity='1'/%3E%3C/svg%3E");
  background-repeat: repeat;
  background-size: 256px 256px;
}

.orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  will-change: transform;
}

.orb--1 {
  width: 500px;
  height: 500px;
  top: -15%;
  left: -10%;
  background: radial-gradient(circle, hsla(217, 89%, 61%, 0.18), transparent 70%);
  animation: float-1 18s ease-in-out infinite;
}

.orb--2 {
  width: 400px;
  height: 400px;
  top: 50%;
  right: -12%;
  background: radial-gradient(circle, hsla(250, 80%, 65%, 0.13), transparent 70%);
  animation: float-2 24s ease-in-out infinite;
}

.orb--3 {
  width: 450px;
  height: 450px;
  bottom: -20%;
  left: 40%;
  background: radial-gradient(circle, hsla(190, 80%, 55%, 0.1), transparent 70%);
  animation: float-3 30s ease-in-out infinite;
}

@keyframes float-1 {
  0%, 100% { transform: translate(0, 0) scale(1); }
  25%      { transform: translate(8%, -12%) scale(1.06); }
  50%      { transform: translate(-4%, -20%) scale(0.94); }
  75%      { transform: translate(-10%, -4%) scale(1.03); }
}

@keyframes float-2 {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33%      { transform: translate(-12%, 8%) scale(1.08); }
  66%      { transform: translate(8%, 12%) scale(0.92); }
}

@keyframes float-3 {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50%      { transform: translate(10%, -8%) scale(1.05); }
}
</style>
