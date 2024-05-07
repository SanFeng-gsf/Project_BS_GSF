import { Loading } from 'element-ui'

let loading = null
const showLoading = (params) => {
  if (loading) {
    loading.close()
  }
  const options = {
    lock: true,
    text: 'Loading',
    spinner: 'el-icon-loading',
    background: 'rgba(0, 0, 0, 0.7)'
  }
  loading = Loading.service(options)
}
const closeLoading = () => {
  loading.close()
}

export {
  showLoading,
  closeLoading
}
