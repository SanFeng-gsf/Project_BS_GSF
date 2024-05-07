import Vue from 'vue'
import VueRouter from 'vue-router'

import Layout from '../views/layout'
import Home from '../views/layout/home'
import SuccessShow from '../views/layout/successShow'
import ZhaoBList from '../views/layout/zhaoBList'
import ZhaoBInsert from '../views/layout/zhaoBInsert'
import User from '../views/layout/user'
import JuBao from '@/views/layout/juBao.vue'
import WeiGuiShow from '@/views/layout/weiGuiShow.vue'

import store from '@/store'

// 路由懒加载 按需加载
const Login = () => import('@/views/login')
const ZhaoBDetail = () => import('@/views/zhaoBDetail')
const Search = () => import('@/views/search')

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [
      { path: '/home', component: Home },
      { path: '/successShow', component: SuccessShow },
      { path: '/zhaoBList', component: ZhaoBList },
      { path: '/zhaoBInsert', component: ZhaoBInsert },
      { path: '/user', component: User },
      { path: '/juBao', component: JuBao },
      { path: '/weiGuiShow', component: WeiGuiShow }

    ]
  },
  { path: '/login', component: Login },
  { path: '/zhaoBDetail/:id', component: ZhaoBDetail },
  { path: '/search/:n', component: Search }
]

const router = new VueRouter({
  routes
})

// 全局前置导航守卫 (相当于拦截器 页面访问拦截)
// 所有的路由在被访问前都会经过这里
/*
  to: 到哪去 包含到哪去的完整路由信息对象（路径，参数）
  from: 从哪里来 包含从哪来的完整路由信息对象（路径，参数）
  next(): 是否放行 表示放行
  next(路径): 表示拦截，并跳转到里面的路径
*/

// 需要权限的页面
const authUrl = ['/zhaoBInsert', '/user', '/juBao']
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
