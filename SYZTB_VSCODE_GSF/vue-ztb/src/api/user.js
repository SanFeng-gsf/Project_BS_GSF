// 用户相关请求
import request from '@/utils/request'

// 根据 id 获取用户信息
export const getUserById = (id) => {
  return request.get('/user/selectById', {
    params: {
      id
    }
  })
}

// 更新用户信息
export const updateUser = (obj) => {
  return request.post('/user/updateMe', obj)
}

// 获取用户头像
export const getIcon = (url) => {
  return request.get('/userIcon/fileUrl', {
    params: {
      url
    },
    responseType: 'blob'
  })
}
