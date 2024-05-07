// 计算评分 满分 5分
// 目前按照 四个标准评分 (公司注册资金、成立时长、成功投标个数、公司规模)
const comput = (obj) => {
  const num = [0, 0, 0, 0, 0]
  if (obj.money > 100) {
    num[0] = 0.5
  } else if (obj.money > 500) {
    num[0] = 0.5
    num[4] = num[4] + 0.25
  }
  if (obj.year > 3) {
    num[1] = 0.5
  } else if (obj.year > 10) {
    num[1] = 0.5
    num[4] = num[4] + 0.25
  }
  if (obj.projectNumber > 3) {
    num[2] = 0.5
  } else if (obj.projectNumber > 10) {
    num[2] = 0.5
    num[4] = num[4] + 0.25
  }
  if (obj.number > 100) {
    num[3] = 0.5
  } else if (obj.number > 500) {
    num[3] = 0.5
    num[4] = num[4] + 0.25
  }
  const sum = num.reduce((sum, item) => sum + item, 0)
  return sum
}

export {
  comput
}
