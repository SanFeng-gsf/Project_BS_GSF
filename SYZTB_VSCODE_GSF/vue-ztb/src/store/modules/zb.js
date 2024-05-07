import { getAll, setAll } from '@/utils/storage'
import { getNowTime, timeIsBig } from '@/utils/dateTime'

// 招标信息相关
export default {
  namespaced: true,
  state () {
    return {
      allZb: getAll(), // 所有的招标信息
      successZb: [] // 成功的招标信息
    }
  },
  mutations: {
    setAllZb (state, arr) {
      state.allZb = arr
      setAll({ date: arr })
    },
    setSuccessZb (state, arr) {
      state.successZb = arr
    }
  },
  actions: {

  },
  getters: {
    getAllZb (state) {
      return state.allZb
    },
    getSuccessZb (state) {
      state.successZb = state.allZb.filter(item => timeIsBig(getNowTime(), item.endTime) === true)
      return state.successZb
    }
  }
}
