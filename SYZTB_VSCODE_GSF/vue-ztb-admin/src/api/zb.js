// 招标信息相关请求
import request from '@/utils/request'

// 分页获取所有招标信息 默认 5 条
export const getAllzb = (current) => {
  return request.get('/zb/selectAll', {
    params: {
      current
    }
  })
}

// 是否禁用 招标信息 默认 5 条
export const update = (obj) => {
  return request.get('/zb/updateClose', {
    params: {
      id: obj.id,
      close: obj.close
    }
  })
}

// 根据项目名称查询招标信息
export const getByprojectName = (projectName, current) => {
  return request.get('/zb/selectByProjectName', {
    params: {
      projectName,
      current
    }
  })
}
