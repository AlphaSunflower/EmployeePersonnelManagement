import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './styles/tokens.css'
import './styles/ep-overrides.css'
import './styles/global.css'
import './styles/transitions.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import AnimatedBackground from './components/AnimatedBackground.vue'
import GlassCard from './components/GlassCard.vue'
import PageHeader from './components/PageHeader.vue'

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.use(ElementPlus)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
app.component('AnimatedBackground', AnimatedBackground)
app.component('GlassCard', GlassCard)
app.component('PageHeader', PageHeader)
app.mount('#app')
