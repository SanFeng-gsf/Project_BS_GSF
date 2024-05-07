import store from '@/store'
import axios from 'axios'
import { Loading } from 'element-ui'

// 创建 axios 实例，将来对创建出来的实例进行自定义配置
// 好处：不会污染原始的 axios 实例
const instance = axios.create({
  baseURL: 'http://localhost:8088',
  timeout: 5000
})

let loadingInstance

// 自定义配置 - 请求/响应 拦截器
// 添加请求拦截器
instance.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  // 添加 loading 等待效果
  loadingInstance = Loading.service({
    lock: true,
    text: 'Loading',
    spinner: 'el-icon-loading',
    background: 'rgba(0, 0, 0, 0.7)'
  })

  // 只要有 token 就携带， 便于请求需要授权的接口
  const token = store.getters.token
  if (token) {
    // 特殊字符 用中括号 如下 -   config.headers['Access-Token'] = token
    config.headers.Authorization = token
  }
  return config
}, function (error) {
  // 对请求错误做些什么
  return Promise.reject(error)
})

// 添加响应拦截器
instance.interceptors.response.use(function (response) {
  // 2xx 范围内的状态码都会触发该函数。
  // 对响应数据做点什么 (默认 axios 会多包装一层 data ,在这里处理一下)
  if (response.status !== 200) {
    // 给出一个错误的 Promise
    return Promise.reject(response.data.message)
  }
  // 成功获取响应 清除 loading
  loadingInstance.close()
  // closeLoading()
  return response.data
}, function (error) {
  // 超出 2xx 范围的状态码都会触发该函数。
  // 对响应错误做点什么
  return Promise.reject(error)
})

// 导出
export default instance
