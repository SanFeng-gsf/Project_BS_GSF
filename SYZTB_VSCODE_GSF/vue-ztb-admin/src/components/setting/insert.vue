<template>
  <div class="in">
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" class="demo-ruleForm">
        <el-form-item class="item" label="账号" prop="name" label-width="80px">
          <el-input v-model="ruleForm.name"></el-input>
        </el-form-item>
        <el-form-item class="item" label="密码" prop="password" label-width="80px">
          <el-input type="password" v-model="ruleForm.password" :show-password="true"></el-input>
        </el-form-item>
        <el-form-item class="item" label="是否为高级管理员" label-width="150px">
          <el-switch v-model="value" active-color="#13ce66"></el-switch>
        </el-form-item>
        <el-form-item class="item">
          <el-button type="primary" @click="submitForm('ruleForm')" class="but">注&nbsp;&nbsp;册</el-button>
        </el-form-item>
      </el-form>
  </div>
</template>

<script>
import md5 from 'js-md5'
import { Notification } from 'element-ui'
import { getSysUser, insert } from '@/api/user'
export default {
  name: 'InsertIndex',
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
        password: '',
        admin: ''
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
      },
      value: false
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
    if (this.user.admin !== 'Admin') {
      this.$store.commit('zbUn/setSearchInfo', { search: '', show: 1 })
      this.$router.go()
    }
  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          // 加密
          if (this.value) {
            this.ruleForm.admin = 'Admin'
          }
          this.ruleForm.password = md5(this.ruleForm.name + this.ruleForm.password)
          const res = await insert(this.ruleForm)
          this.ruleForm = {}
          this.value = false
          if (res.isSuccess) {
            Notification({
              title: '注册成功',
              type: 'success'
            })
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

  .demo-ruleForm {
    margin-top: 120px;
    margin-left: 350px;
    padding-top: 50px;
    width: 600px;
    height: 300px;
    border: 1px solid;
    .item {
      margin-left: 80px;
      width: 400px;
    }
  }
  .but {
    margin-left: 20px;
    width: 150px;
  }
</style>
