<template>
  <div class="Login">
    <div class="title">招得快后台管理系统登入</div>
    <div class="body">
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" class="demo-ruleForm">
        <el-form-item class="item" label="账号" prop="name">
          <el-input v-model="ruleForm.name"></el-input>
        </el-form-item>
        <el-form-item class="item" label="密码" prop="password">
          <el-input type="password" v-model="ruleForm.password" :show-password="true"></el-input>
        </el-form-item>
        <el-form-item class="item">
          <el-button type="primary" @click="submitForm('ruleForm')">登入</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
// 登入页
import md5 from 'js-md5'
import { Notification } from 'element-ui'
import { login } from '@/api/user'

export default {
  name: 'LoginIndex',
  data () {
    const checkPassWord = (rule, value, callback) => {
      // const regex = /^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z]).{8,}$/
      const regex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$/
      setTimeout(() => {
        if (!regex.test(value)) {
          callback(new Error('密码至少包含一个大写字母、一个小写字母以及数字'))
        } else {
          callback()
        }
      }, 1000)
    }
    return {
      ruleForm: {
        name: '',
        password: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入账号名称', trigger: 'blur' },
          { min: 3, message: '请至少输入3个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入账号密码', trigger: 'blur' },
          { validator: checkPassWord, trigger: 'blur' },
          { min: 6, max: 12, message: '密码长度为3到12位', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          // 加密
          this.ruleForm.password = md5(this.ruleForm.name + this.ruleForm.password)
          const res = await login(this.ruleForm)
          this.ruleForm = {}
          if (res.isSuccess) {
            this.$store.commit('user/setUserInfo', res.data)
            Notification({
              title: '登入成功',
              type: 'success'
            })
            this.$router.replace('/')
          } else {
            Notification({
              title: res.message,
              type: 'error'
            })
          }
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.title {
  margin-left: 580px;
  margin-top: 200px;
  width: 330px;
  height: 50px;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  line-height: 50px;
  background-color: rgb(189, 194, 201);
}
.body {
  margin-left: 580px;
  width: 330px;
  height: 220px;
  text-align: center;
  border-top: 1px solid #000;
  background-color: rgb(189, 194, 201);
}
.demo-ruleForm {
  margin-left: 15px;
  margin-top: 20px;
}
.item {
  width: 300px;
}
.el-input {
  width: 220px;
}
.el-button {
  width: 300px;
}
</style>
