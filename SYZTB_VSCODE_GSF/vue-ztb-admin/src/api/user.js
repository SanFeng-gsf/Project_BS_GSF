// 账号相关信息
import request from '@/utils/request'

// 登入请求
export const login = (obj) => {
  return request.post('/sys/login', {
    name: obj.name,
    password: obj.password
  })
}

// 查询管理员个人信息
export const getSysUser = (name) => {
  return request.get('/sys/selectByName', {
    params: {
      name
    }
  })
}

// 更新管理员密码
export const update = (name, password) => {
  return request.get('/sys/update', {
    params: {
      name,
      password
    }
  })
}

// 注册管理员账号
export const insert = (obj) => {
  return request.post('/sys/insert', {
    name: obj.name,
    password: obj.password,
    admin: obj.admin
  })
}

// 删除管理员
export const deleteById = (id) => {
  return request.get('/sys/deleteById', {
    params: {
      id
    }
  })
}

// 更新管理员权限
export const updateAdmin = (name) => {
  return request.get('/sys/updateAdmin', {
    params: {
      name
    }
  })
}

// 获取所有管理员信息
export const getAll = () => {
  return request.get('/sys/selectAll', {})
}

// 获取账户信息 公司名称
export const getUser = (name) => {
  return request.get('user/getUser', {
    params: {
      name
    }
  })
}

// 更新用户账户 禁用
export const updateUser = (obj) => {
  return request.get('/user/updateUser', {
    params: {
      id: obj.id,
      ban: obj.ban
    }
  })
}
