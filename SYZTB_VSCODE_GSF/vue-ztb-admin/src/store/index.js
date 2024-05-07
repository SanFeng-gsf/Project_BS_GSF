import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import zbUn from './modules/zbUn'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
  },
  getters: {
    token (state) {
      return state.user.userInfo.token
    },
    name (state) {
      return state.user.userInfo.name
    }
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user,
    zbUn
  }
})
