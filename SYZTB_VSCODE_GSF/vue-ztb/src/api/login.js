// 登入相关的请求
import request from '@/utils/request'

// 获取登入验证码
export const getCode = (phone) => {
  return request.post('/user/code', { phone })
}

// 登入接口
export const codeLogin = (obj) => {
  return request.post('/user/login', { ...obj })
}
