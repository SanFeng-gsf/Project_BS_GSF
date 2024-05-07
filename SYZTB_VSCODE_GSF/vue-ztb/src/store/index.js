import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import zb from './modules/zb'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
  },
  getters: {
    token (state) {
      return state.user.userInfo.token
    },
    userId (state) {
      return state.user.userInfo.userId
    }
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user,
    zb
  }
})
