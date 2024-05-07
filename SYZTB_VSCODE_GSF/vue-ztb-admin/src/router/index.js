import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'

import Layout from '../views/layout'
import Home from '../views/layout/home'

const Login = () => import('@/views/login')

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/home',
    component: Layout,
    children: [
      { path: '/home', component: Home }
    ]
  },
  { path: '/login', component: Login }
]

const router = new VueRouter({
  routes
})

const authUrl = ['/home']
router.beforeEach((to, from, next) => {
  if (!authUrl.includes(to.path)) {
    next()
    return
  }
  const token = store.getters.token
  if (token) {
    next()
  } else {
    next('/login')
  }
})

export default router
