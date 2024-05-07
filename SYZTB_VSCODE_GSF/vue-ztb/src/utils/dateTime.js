// 时间处理

const getNowTime = () => {
  const now = new Date()
  const year = now.getFullYear() // 获取完整的年份(4位,1970-????)
  const month = now.getMonth() + 1 // 获取当前月份(0-11,0代表1月)
  const today = now.getDate() // 获取当前日(1-31)
  const hour = now.getHours() // 获取当前小时数(o-23)
  const minute = now.getMinutes() // 获取当前分钟数(O-59)
  const second = now.getSeconds() // 获取当前秒数(o-59)
  const nowTime = year + '-' + fillZero(month) + '-' + fillZero(today) + ' ' + fillZero(hour) + ':' + fillZero(minute) + ':' + fillZero(second)
  return nowTime
}

// fillZero 补零函数
function fillZero (str) {
  let realNum
  if (str < 10) {
    realNum = '0' + str
  } else {
    realNum = str
  }
  return realNum
}

// 时间比较 第一个大返回 true
const timeIsBig = (str1, str2) => {
  str1 = new Date(str1).getTime()
  str2 = new Date(str2).getTime()
  if (str1 > str2) {
    return true
  }
  return false
}

export {
  timeIsBig,
  getNowTime
}
