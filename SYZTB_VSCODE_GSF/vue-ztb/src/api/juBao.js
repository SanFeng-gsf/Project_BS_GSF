// 举报相关的请求
import request from '@/utils/request'

// 提交举报信息
export const setJuBao = (obj) => {
  return request.post('/jb/details', obj)
}

// 根据 公司名称获取举报信息
export const getJuBao = (myName, projectName) => {
  return request.get('/jb/getJb', {
    params: {
      myName,
      projectName
    }
  })
}

// 根据文件名列表获取文件信息
export const getJuBaoFile = (fileUrl) => {
  return request.get('/jb/getFile', {
    params: {
      fileUrl
    },
    responseType: 'blob'
  })
}

// 根据被举报公司获取被举报信息 (name) 举报公司 为自己
export const getByName = (name) => {
  return request.get('jb/getByName', {
    params: {
      name
    }
  })
}
