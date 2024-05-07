<template>
  <div class="Login">
    <div class="login">
      <div class="login_left"><img class="login_img" src="@/assets/loginLeft.png"></div>
      <div class="container">
        <div class="title">
          <div class="sj"><el-button @click="changeShow(0)" type="text">手机号登入</el-button></div>
          <div class="zh"><el-button @click="changeShow(1)" type="text">账号密码登入</el-button></div>
          <div class="ts" v-if="showPhone">未注册的手机号登录后将自动注册</div>
          <div class="ts" v-else>未注册的账号密码将自动注册</div>
        </div>
        <div class="form" v-if="showPhone">
          <div class="form-item">
            <el-input v-model="phone" class="user_inp" placeholder="请输入手机号"></el-input>
          </div>
          <div class="form-item">
            <el-input v-model="code" class="dx" placeholder="请输入短信验证码"></el-input>
            <el-button class="hq" @click="getCode" type="text">{{ second === totalSecond ? '获取验证码' : second + '重新发送' }}</el-button>
          </div>
        </div>
        <div class="form" v-else>
          <div class="form-item">
            <el-input v-model="name" class="user_inp" placeholder="请输入公司名称"></el-input>
          </div>
          <div class="form-item">
            <el-input v-model="password" class="ms" placeholder="请输入密码" show-password></el-input>
          </div>
        </div>
        <div class="login-btn" @click="login">登录</div>
      </div>
    </div>
    <Foot class="login_foot"></Foot>
  </div>
</template>

<script>
// 登入页
import { getCode, codeLogin } from '@/api/login'
import { Message, Notification } from 'element-ui'
import Foot from '@/components/foot.vue'
import md5 from 'js-md5'
import { getUserById, getIcon } from '@/api/user'

export default {
  name: 'LoginIndex',
  data () {
    return {
      phone: '15979644534', // 手机号
      code: '', // 短信验证码
      name: '', // 公司名称
      password: '', // 密码
      totalSecond: 60, // 总秒数
      second: 60, // 当前秒数
      timer: null, // 定时器 id
      showPhone: true, // 手机登入
      id: undefined
    }
  },
  created () {
  },
  methods: {
    // 手机号或者是账号密码登入切换
    changeShow (n) {
      if (n === 0) {
        this.showPhone = true
      } else {
        this.showPhone = false
      }
    },
    // 校验 手机号 是否合法
    validFn () {
      // 正则
      if (!/^1[3-9]\d{9}$/.test(this.phone)) {
        Message({
          message: '请输入正确的手机号',
          type: 'error'
        })
        return false
      }
      return true
    },
    // 校验 公司名称 密码 是否合法
    validName () {
      if (this.name.trim() == null || this.name.trim().length < 3) {
        Message({
          message: '请输入至少3个非空字符',
          type: 'error'
        })
        return false
      }
      return true
    },
    validPassword () {
      // 至少六个数字
      if (!/^[0-9]{6,12}$/.test(this.password)) {
        Message({
          message: '请输入至少6~12个数字',
          type: 'error'
        })
        return false
      }
      return true
    },
    // 获取短信验证码
    async getCode () {
      if (!this.validFn()) {
        return
      }
      // 当目前没有定时器开着
      if (!this.timer && this.second === this.totalSecond) {
        // 发送请求 (获取真正的验证码)
        await getCode(this.phone)
        Message({
          message: '验证码发送成功',
          type: 'success'
        })
        // 开启倒计时 setInterval 定时器
        this.timer = setInterval(() => {
          this.second--
          if (this.second <= 0) {
            clearInterval(this.timer)
            this.timer = null
            this.second = this.totalSecond
          }
        }, 1000)
      }
    },
    // 登入
    async login () {
      if (this.showPhone) {
        // 手机号登入
        if (!this.validFn()) {
          return
        }
        if (!/^\d{6}$/.test(this.code)) {
          // 判断是不是6位
          Message({
            message: '短信验证码错误',
            type: 'error'
          })
          return
        }
        // 发送手机号登入请求
        const res = await codeLogin({ phone: this.phone, code: this.code })
        if (!res.isSuccess) {
          this.$message({
            message: res.message,
            type: 'error'
          })
          return
        }
        this.$store.commit('user/setUserInfo', res.data)
        this.id = res.data.userId
      } else {
        // 公司名称加密码登入
        if (!this.validName()) {
          return
        } else {
          if (!this.validPassword()) {
            return
          }
        }
        // 加密传入数据库
        this.password = md5(this.name + this.password)
        // 发送手机号登入请求
        const res = await codeLogin({ name: this.name, password: this.password })
        if (!res.isSuccess) {
          this.$message({
            message: res.message,
            type: 'error'
          })
          return
        }
        this.$store.commit('user/setUserInfo', res.data)
        this.id = res.data.userId
      }
      const { data } = await getUserById(this.id)
      if (data.icon !== null && data.icon !== '') {
        const res = await getIcon(data.icon)
        const blob = new Blob([res])
        const file = new File([blob], 'userIcon', { type: blob.type })
        const fileReader = new FileReader()
        fileReader.readAsDataURL(file)
        fileReader.onload = function () {
          const fileData = fileReader.result
          // 这里不能 this.$store.commit('user/setFileData', fileData) 可能是异步问题
          localStorage.setItem('fileData', fileData)
        }
      }
      Notification({
        title: '登入成功',
        type: 'success'
      })
      // 判断地址栏是否有回跳地址
      const url = this.$route.query.backUrl || '/'
      // push 回重复返回 建议用 replace
      this.$router.replace(url)
    }
  },
  // 组件销毁时清除定时器
  destroyed () {
    clearInterval(this.timer)
  },
  components: {
    Foot
  }
}
</script>

<style lang="less" scoped>
.Login {
  float: left;
  width: 100%;
  height: 100%;
  background-image: url('@/assets/login_background.jpg');
}
.login {
  float: left;
  width: 100%;
  height: 640px;
  text-align: center;
  .login_left {
    float: left;
    margin-left: 450px;
    margin-top: 150px;
    width: 300px;
    height: 291px;
    border: 1px solid;
    .login_img {
      width: 300px;
      height: 291px;
    }
  }
}
.container {
  float: left;
  width: 300px;
  margin-top: 150px;
  border-top: 1px solid #dcdee7;
  border-right: 1px solid #dcdee7;
  border-bottom: 1px solid #dcdee7;
  .title {
    float: left;
    width: 300px;
    text-align: center;
    margin-bottom: 10px;
    border-bottom: 1px solid #dcdee7;
    .sj, .zh {
      float: left;
      width: 150px;
      line-height: 45px;
      border-bottom: 1px solid #dcdee7;
    }
    .ts {
      float: left;
      margin-left: 20px;
      line-height: 35px;
      font-size: 12px;
      color: #403939;
    }
  }
.el-button {
  color: #000000;
  font-weight: bold;
}
  .form-item {
    float: left;
    width: 300px;
    margin-top: 10px;
    .user_inp, .ms {
      margin-left: 10px;
      margin-bottom: 10px;
      width: 280px;
    }
    .dx {
      margin-top: 10px;
      margin-left: 10px;
      margin-bottom: 10px;
      width: 170px;
    }
    .hq {
      position: relative;
      margin-left: 20px;
      top: 2px;
      width: 90px;
      height: 35px;
      background-color: rgba(133, 128, 106, 0.5);
    }
    .ms {
      margin-top: 10px;
    }
  }

  .login-btn {
    width: 280px;
    height: 40px;
    float: left;
    margin-left: 10px;
    margin-top: 10px;
    margin-bottom: 20px;
    line-height: 40px;
    background: linear-gradient(90deg, #ecb53c, #ff9211);
    color: #fff;
    border-radius: 39px;
    box-shadow: 0 10px 20px 0 rgba(0, 0, 0, 0.1);
    letter-spacing: 2px;
    text-align: center;
  }
}
.login_foot {
  background: none;
}
</style>
