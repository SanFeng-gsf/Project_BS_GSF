// 招标相关请求
import request from '@/utils/request'

// 根据项目名称查询投标信息
export const getByN = (obj) => {
  return request.post('/tb/selectByN', obj)
}

// 根据项目名称查询投标信息
export const setSuccessById = (id) => {
  return request.get('/tb/setSuccessById', {
    params: {
      id
    }
  })
}

// 根据公司名称查询投标信息 (自己的)
export const selectByName = (name) => {
  return request.post('/tb/selectByName', {
    name
  })
}

// 新增投标信息
export const touBInsert = (obj) => {
  return request.post('/tb/insert', {
    name: obj.name,
    phone: obj.phone,
    projectName: obj.projectName,
    peopleName: obj.peopleName,
    projectNumber: obj.projectNumber,
    suoName: obj.suoName,
    price: obj.price,
    year: obj.year,
    money: obj.money,
    number: obj.number
  })
}
