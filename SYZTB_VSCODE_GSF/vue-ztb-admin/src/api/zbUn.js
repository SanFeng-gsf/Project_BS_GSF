// 招标 发布 信息相关请求
import request from '@/utils/request'

// 获取招标发布信息 发布记录总获取
export const getInsertAll = (current) => {
  return request.get('/zbUn/selectAll', {
    params: {
      current
    }
  })
}

// 通过招标发布信息审核 是否通过
export const updateCheck = (obj) => {
  return request.get('/zbUn/updateCheck', {
    params: {
      id: obj.id,
      pass: obj.pass
    }
  })
}

// 通过招标项目名称查询
export const getByProjectName = (projectName) => {
  return request.get('/zbUn/selectByProjectName', {
    params: {
      projectName
    }
  })
}
