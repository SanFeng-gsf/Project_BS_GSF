// 招标相关请求
import request from '@/utils/request'

// 分页获取所有招标信息 默认 5 条
export const getAllzb = (current) => {
  return request.get('/zb/selectAll', {
    params: {
      current
    }
  })
}

// 分页获取违规的招标信息 (被禁止的)
export const getWeiGui = (current) => {
  return request.get('/zb/selectWeiGui', {
    params: {
      current
    }
  })
}

// 分页获取成功的招标信息
export const getSuccessZb = (current) => {
  return request.get('/zb/selectSuccess', {
    params: {
      current
    }
  })
}

// 分页获取不成功的招标信息
export const getUnSuccessZb = (current) => {
  return request.get('/zb/selectUnSuccess', {
    params: {
      current
    }
  })
}

// 通过 id 查询招标信息 (含投标信息)
export const getById = (id) => {
  return request.post('/zb/selectDetail', {
    id
  })
}

// 根据公司名称查询该公司的招标信息
export const getByName = (name) => {
  return request.post('/zb/selectByName', {
    name
  })
}

// 根据 id 查询招标信息判断是否结束
export const isFinish = (id) => {
  return request.get('/zb/isFinish', {
    params: {
      id
    }
  })
}

// 新增招标信息
export const setZb = (obj) => {
  return request.post('/zbUn/insert', {
    phone: obj.phone,
    name: obj.name,
    peopleName: obj.peopleName,
    projectName: obj.projectName,
    ex: obj.ex,
    price: obj.price,
    endTime: obj.endTime
  })
}
