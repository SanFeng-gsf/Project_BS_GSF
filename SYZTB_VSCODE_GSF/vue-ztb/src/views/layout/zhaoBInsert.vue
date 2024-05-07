<template>
  <div>
    <DaoHang></DaoHang>
    <div class="body_zb_form">
      <div class="head"><span>招标发布</span></div>
      <div class="body_form">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="招标公司:">
            <div class="color">&nbsp;&nbsp;{{ ruleForm.name }}</div>
          </el-form-item>
          <el-form-item label="公司法人:">
            <div class="color">&nbsp;&nbsp;{{ ruleForm.peopleName }}</div>
          </el-form-item>
          <el-form-item label="联系电话:" prop="phone">
            <el-input v-model="ruleForm.phone" style="width: 300px;"></el-input>
          </el-form-item>
          <el-form-item label="招标项目名称" prop="projectName" label-width="110px">
            <el-input v-model="ruleForm.projectName" style="width: 500px;"></el-input>
          </el-form-item>
          <el-form-item label="招标项目说明">
            <el-input type="textarea" v-model="ruleForm.ex"></el-input>
          </el-form-item>
          <el-form-item label="招标价格" prop="price">
            <el-input v-model="ruleForm.price" style="width: 200px;"></el-input>
          </el-form-item>
          <el-form-item label="截止日期">
            <el-date-picker type="date" placeholder="选择日期" v-model="endTime.date"
              :picker-options="pickerOptions1" value-format="yyyy-MM-dd" style="width: 200px;"></el-date-picker>
            <span style="margin-left: 50px">-</span>
            <el-time-picker placeholder="选择时间" v-model="endTime.time" value-format="HH:mm:ss" style="margin-left: 50px;width: 200px;"></el-time-picker>
          </el-form-item>
          <el-form-item class="tijiao">
            <el-button type="primary" @click="submitForm()" class="butt">提交</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
            <div class="tj_sm">提交前请仔细检查相关信息，避免信息错误造成不必要的麻烦</div>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <FooterPage></FooterPage>
  </div>
</template>

<script>
// 招标信息发布
import DaoHang from '@/components/daoHang.vue'
import FooterPage from '@/components/foot.vue'
import { getUserById } from '@/api/user'
import { setZb } from '@/api/zb'
export default {
  data () {
    const checkNumber = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('招标价格不能为空'))
      } else if (isNaN(value)) {
        return callback(new Error('请输入数字值'))
      }
    }
    return {
      pickerOptions1: {
        disabledDate (time) {
          return time.getTime() < Date.now()
        }
      },
      ruleForm: {
        name: '', // 公司名称
        peopleName: '', // 公司法人
        phone: '', // 联系电话
        projectName: '', // 招标项目名称
        ex: '', // 招标项目说明
        price: undefined, // 招标价格
        endTime: '' // 截止时间
      },
      endTime: {
        date: '',
        time: ''
      },
      rules: {
        projectName: [
          { required: true, message: '请输入项目名称', trigger: 'blur' },
          { min: 3, message: '长度不小于3个字符', trigger: 'blur' }
        ],
        price: [
          { required: true, message: '请输入项目报价', trigger: 'blur' },
          { validator: checkNumber, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 校验 手机号 是否合法
    validFn () {
      // 正则
      if (!/^1[3-9]\d{9}$/.test(this.ruleForm.phone)) {
        this.$message({
          message: '请输入正确的手机号',
          type: 'error'
        })
        return false
      }
      return true
    },
    async submitForm () {
      if (!this.validFn()) {
        return
      }
      this.ruleForm.endTime = this.endTime.date + ' ' + this.endTime.time
      const res = await setZb(this.ruleForm)
      if (res.isSuccess) {
        this.$router.push('/')
        this.$message({
          message: '发布成功',
          type: 'success'
        })
      } else {
        this.$message({
          message: res.message,
          type: 'error'
        })
      }
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    }
  },
  async created () {
    const { data } = await getUserById(this.getId)
    if (data.name === '' || data.peopleName === '') {
      this.$confirm('公司信息不完善，请前往个人中心完善个人信息', '是否前往个人中心', {
        distinguishCancelAndClose: true,
        confirmButtonText: '是',
        cancelButtonText: '否'
      })
        .then(() => {
          this.$router.push('/user')
          this.$message({
            type: 'success',
            message: '跳转成功'
          })
        })
        .catch(action => {
          this.$message({
            type: 'info',
            message: '已取消'
          })
          this.$router.go(-1)
        })
    }
    this.ruleForm.name = data.name
    this.ruleForm.peopleName = data.peopleName
  },
  computed: {
    getId () {
      return this.$store.getters.userId
    }
  },
  components: {
    DaoHang,
    FooterPage
  }
}
</script>

<style lang="less" scoped>
.color {
  color: #b63737;
  font-size: larger;
  font-weight: bold;
}
.body_zb_form {
  float: left;
  width: 100%;
  margin-bottom: 20px;
  .head {
    float: left;
    width: 100%;
    line-height: 60px;
    height: 60px;
    color: #a87070;
    font-weight: bold;
    font-size: 25px;
    text-align: center;
  }
  .body_form {
    float: left;
    width: 80%;
    margin-left: 150px;
    border: 1px solid #a3a7ae;
    .el-form {
      margin: 20px;
      .jb_name {
        width: 600px;
      }
    }
  }
  .tijiao {
    float: left;
    margin-top: 20px;
    .butt {
      margin-left: 200px;
      margin-right: 50px;
    }
    .tj_sm {
      float: left;
      color: #6c6e72;
      font-size: 12px;
    }
  }
}
</style>
