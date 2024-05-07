<template>
  <div class="self">
    <div class="top"></div>
    <el-descriptions class="margin-top" title="个人信息" :column="3">
      <template slot="extra">
        <el-button @click="change" type="primary">修改密码</el-button>
      </template>
      <el-descriptions-item label="用户名">{{ user.name }}</el-descriptions-item>
      <el-descriptions-item label="密码">{{ user.password }}</el-descriptions-item>
      <el-descriptions-item label="权限">{{ user.admin === 'Admin' ? '高级管理员' : '管理员' }}</el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script>
import { getSysUser, update } from '@/api/user'
import { md5 } from 'js-md5'
export default {
  name: 'SelfIndex',
  data () {
    return {
      user: {}
    }
  },
  computed: {
    name () {
      return this.$store.getters.name
    }
  },
  async created () {
    const res = await getSysUser(this.name)
    this.user = res.data
  },
  methods: {
    change () {
      this.$prompt('请输入新密码', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$/,
        inputErrorMessage: '密码格式不正确'
      }).then(async ({ value }) => {
        const password = md5(this.name + value)
        const res = await update(this.name, password)
        if (res.isSuccess) {
          this.$message({
            type: 'success',
            message: res.message
          })
        } else {
          this.$message({
            type: 'error',
            message: res.message
          })
        }
        this.$router.go()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消'
        })
      })
    }
  }
}
</script>

<style lang="less" scoped>
.self {
  margin-left: 100px;
  width: 1000px;
  height: 500px;
  .top {
    width: 1000px;
    height: 150px;
  }
  .margin-top {
    padding: 20px;
    border: 1px solid;
  }
}
</style>
