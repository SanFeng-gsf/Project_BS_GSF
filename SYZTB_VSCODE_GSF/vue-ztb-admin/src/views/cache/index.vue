<template>
  <div class="all">
    <div class="title">真人验证</div>
    <div class="yzm">
      <div class="yzm_title">验证码：</div>
      <div class="yzm_nr">{{ value }}</div>
    </div>
    <div class="inp">
      <el-input v-model="newValue" placeholder="请输入验证码" class="inp_in"></el-input>
    </div>
    <div class="inp">
      请点击确认身份&nbsp;&nbsp;&nbsp;&nbsp;
      <el-switch v-model="check" :active-value="true" :inactive-value="false"></el-switch>
    </div>
    <div class="inp"><el-button @click="checkSys" class="but" type="primary">确认</el-button></div>
  </div>
</template>

<script>
import { Notification } from 'element-ui'
export default {
  name: 'CacheIndex',
  data () {
    return {
      value: '',
      newValue: '',
      check: false
    }
  },
  created () {
    this.value = Math.random().toString(36).slice(-4)
  },
  methods: {
    checkSys () {
      if (this.value === this.newValue && this.check === true) {
        Notification({
          title: '认证成功',
          type: 'success'
        })
        this.$router.replace('/')
      } else {
        Notification({
          title: '认证失败',
          type: 'error'
        })
      }
    }
  }
}
</script>

<style lang="less" scoped>
.all {
  margin-top: 100px;
  margin-left: 500px;
  width: 500px;
  height: 500px;
  border: 1px solid;
  .title {
    width: 500px;
    height: 50px;
    border-bottom: 1px solid;
    line-height: 50px;
    font-size: 20px;
  }
  .yzm {
    float: left;
    margin-top: 30px;
    width: 500px;
    height: 50px;
    .yzm_title {
      float: left;
      margin-left: 120px;
      width: 100px;
      height: 50px;
      line-height: 50px;
    }
    .yzm_nr {
      float: left;
      width: 120px;
      height: 50px;
      font-size: 18px;
      line-height: 50px;
      background-color: rgba(28, 97, 195, 0.689);
    }
  }
  .inp {
    float: left;
    margin-top: 20px;
    width: 500px;
    height: 50px;
    .inp_in {
      float: left;
      margin-left: 120px;
      width: 220px;
    }
  }
}
.but {
  width: 120px;
}
</style>
