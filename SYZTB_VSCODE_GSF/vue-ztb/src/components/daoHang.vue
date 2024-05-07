<template>
  <div>
    <div class="header">
      <div class="title"><span>招得快招投标信息服务平台</span></div>
      <div class="header_right">
        <div class="header_search">
          <el-input v-model="search" size="medium" placeholder="请输入公司名称" prefix-icon="el-icon-search"></el-input>
          <el-button @click="goSearch" type="primary" size="medium" icon="el-icon-search">搜索</el-button>
        </div>
        <div class="login">
          <div class="i_user">
            <img v-if="imgUrl" :src="imgUrl" class="imgSize">
            <i v-else class="el-icon-user"></i></div>
          <div class="login_button"><el-button type="info" round @click="login">登入</el-button></div>
        </div>
        <div class="layout">
          <router-link class="daohang" to="/home">首页</router-link>
          <router-link class="daohang" to="/successShow">招标公示</router-link>
          <router-link class="daohang" to="/zhaoBList">招标信息</router-link>
          <router-link class="daohang" to="/zhaoBInsert">招标发布</router-link>
          <router-link class="daohang" to="/user">个人中心</router-link>
          <router-link class="daohang" to="/juBao">举报</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      search: '',
      imgUrl: ''
    }
  },
  computed: {
    isLogin () {
      return this.$store.getters.token
    }
  },
  created () {
    const base64ToBlob = (base64) => {
      const arr = base64.split(',')
      const type = arr[0].match(/:(.*?);/)[1]
      const bstr = atob(arr[1])
      let n = bstr.length
      const u8arr = new Uint8Array(n)
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n)
      }
      return new Blob([u8arr], { type })
    }
    const reader = localStorage.getItem('fileData')
    if (reader !== '' && reader !== null) {
      this.imgUrl = window.URL.createObjectURL(base64ToBlob(reader))
    }
  },
  methods: {
    goSearch () {
      if (this.search === '' || this.search.trim() === '') {
        return
      }
      this.$router.push(`/search/${this.search}`)
    },
    login () {
      this.$router.push('/login')
      if (this.isLogin) {
        this.$confirm('当前用户已登入，是否退出', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$store.dispatch('user/logout')
          localStorage.removeItem('fileData')
          this.$message({
            type: 'success',
            message: '退出成功!'
          })
        }).catch(() => {
          this.$router.back()
          this.$message({
            type: 'info',
            message: '已取消'
          })
        })
      }
    }
  }
}
</script>

<style lang="less" scoped>
.header {
  float: left;
  width: 100%;
  height: 150px;
  background-color: #ebf9ff;
  text-align: center;
}
.imgSize {
  width: 40px;
  height: 40px;
}
.title {
  float: left;
  width: 500px;
  height: 100px;
  line-height: 100px;
  font-size: 30px;
  margin-left: 150px;
}
.header_right {
  float: left;
  width: 790px;
  height: 150px;
}
.header_search {
  float: left;
  margin-left: 100px;
  width: 500px;
  height: 100px;
  line-height: 100px;
}
.el-input {
  width: 300px;
}

.login {
  float: left;
  width: 180px;
  height: 100px;
}

.el-icon-user {
  font-size: 28px;
}
.i_user {
  float: left;
  margin-top: 35px;
  margin-bottom: 40px;
  margin-right: 10px;
}

.login_button {
  float: left;
  margin-top: 35px;
}

.layout {
  position: relative;
  left: 0;
  top: 0;
  display: flex;
  width: 100%;
  text-align: center;
}
.layout a {
  flex: 1;
  text-decoration: none;
  padding: 14px 0;
  line-height: 20px;
  color: #000;
}
.layout a.router-link-active {
  background-color: rgb(149, 158, 170);
}
.layout a:hover {
  background-color: #627ecb;
}
</style>
