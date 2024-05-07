import { getSearch, setSearch } from '@/utils/storage'

export default {
  namespaced: true,
  state () {
    return {
      // 个人信息
      searchInfo: getSearch()
    }
  },
  mutations: {
    setSearchInfo (state, obj) {
      state.searchInfo = obj
      setSearch(obj)
    }
  },
  actions: {
    delete (context) {
      context.commit('setSearchInfo', { search: '', show: undefined })
    }
  },
  getters: {
    getSearchInfo (state) {
      return state.searchInfo
    }
  }
}
