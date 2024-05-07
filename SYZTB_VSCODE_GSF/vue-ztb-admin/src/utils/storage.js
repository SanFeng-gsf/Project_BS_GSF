// vuex 持久化处理 本地存储

// 保证刷新不会丢失数据
const INFO_KEY = 'BS_ZTB_INFO'
const ZBUN_INFO = 'ZB_UN_INFO'

// 获取个人信息
export const getInfo = () => {
  const defaultObj = { token: '', name: '' }
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

// 设置招标发布的项目名称 和 页面标识
export const setSearch = (obj) => {
  localStorage.setItem(ZBUN_INFO, JSON.stringify(obj))
}

// 获取招标发布的项目名称 和 页面标识
export const getSearch = () => {
  const defaultObj = { search: '', show: undefined }
  const result = localStorage.getItem(ZBUN_INFO)
  return result ? JSON.parse(result) : defaultObj
}
