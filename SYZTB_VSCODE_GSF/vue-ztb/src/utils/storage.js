// vuex 持久化处理 本地存储
// 保证刷新不会丢失数据
const INFO_KEY = 'BS_ZTB_INFO'
const ALL_ZB = 'All_ZB'
const HISTORY_KEY = 'history_list'

// 获取个人信息
export const getInfo = () => {
  const defaultObj = { token: '', userId: '' }
  const result = localStorage.getItem(INFO_KEY)
  return result ? JSON.parse(result) : defaultObj
}

// 设置个人信息
export const setInfo = (obj) => {
  localStorage.setItem(INFO_KEY, JSON.stringify(obj))
}

// 删除个人信息
export const removeInfo = () => {
  localStorage.removeItem(INFO_KEY)
}

// 设置所有招标信息
export const setAll = (obj) => {
  localStorage.setItem(ALL_ZB, JSON.stringify(obj.date))
}

// 获取所有招标信息
export const getAll = () => {
  const result = localStorage.getItem(ALL_ZB)
  return JSON.parse(result)
}

// 获取搜索历史
export const getHistoryList = () => {
  const result = localStorage.getItem(HISTORY_KEY)
  return result ? JSON.parse(result) : []
}

// 设置搜索历史
export const setHistoryList = (arr) => {
  localStorage.setItem(HISTORY_KEY, JSON.stringify(arr))
}
