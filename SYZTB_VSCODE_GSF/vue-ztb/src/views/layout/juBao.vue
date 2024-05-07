<template>
  <div>
    <DaoHang></DaoHang>
    <div class="body_jb_form">
      <div class="head"><span>举报中心</span></div>
      <div class="body_form">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="举报说明" prop="sm">
            <el-input v-model="ruleForm.sm"></el-input>
          </el-form-item>
          <el-form-item label="您的公司" prop="myName" class="jb_name">
            <el-input v-model="ruleForm.myName"></el-input>
          </el-form-item>
          <el-form-item label="举报公司" prop="name" class="jb_name">
            <el-input v-model="ruleForm.name"></el-input>
          </el-form-item>
          <el-form-item label="举报项目" prop="projectName" class="jb_name">
            <el-input v-model="ruleForm.projectName"></el-input>
          </el-form-item>
          <el-form-item label="举报原因" prop="type">
            <el-checkbox-group v-model="ruleForm.type">
              <el-checkbox label="公司相关信息作假" name="type"></el-checkbox>
              <el-checkbox label="公司招标信息不实" name="type"></el-checkbox>
              <el-checkbox label="公司招标违反公平公正原则" name="type"></el-checkbox>
              <el-checkbox label="其他" name="type"></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="其他原因" prop="resource">
            <el-input type="textarea" v-model="ruleForm.resource"></el-input>
          </el-form-item>
          <el-form-item label="相关资料">
            <el-upload
              class="upload-demo"
              ref="upload"
              action="http://localhost:8088/jb/file"
              :file-list="fileList"
              :on-change="fileChange"
              name="file"
              :auto-upload="false"
              multiple>
              <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              <!-- <el-button size="small" type="success" @click="submitUpload">上传到服务器</el-button> -->
              <div slot="tip" class="el-upload__tip">文件大小不超过500KB</div>
            </el-upload>
          </el-form-item>
          <el-form-item label="是否匿名">
            <el-switch v-model="ruleForm.hide"></el-switch>
          </el-form-item>
          <el-form-item class="tijiao">
            <el-button type="primary" @click="submitForm('ruleForm')" class="butt">提交</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
            <div class="tj_sm">提交后相关工作人员会及时进行调查，若情况属实会第一时间并发布公告，请耐心等待</div>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <FooterPage></FooterPage>
  </div>
</template>

<script>
// 举报
import DaoHang from '@/components/daoHang.vue'
import FooterPage from '@/components/foot.vue'
import { setJuBao } from '@/api/juBao'

export default {
  data () {
    return {
      ruleForm: {
        sm: '', // 举报说明
        myName: '', // 您的公司名称
        name: '', // 举报的公司名称
        projectName: '', // 举报项目名称
        type: [], // 举报原因 (有单独的表)
        resource: '', // 其他举报原因
        fileName: [], // 举报文件名称 (后端需要先转为字符串再存入)
        hide: false // 是否匿名
      },
      rules: {
        sm: [
          { required: true, message: '请相关举报说明', trigger: 'blur' },
          { min: 1, message: '长度不小于1个字符', trigger: 'blur' }
        ],
        myName: [
          { required: true, message: '请输入您的公司名称', trigger: 'blur' },
          { min: 3, message: '长度不小于3个字符', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入需要举报的公司名称', trigger: 'blur' },
          { min: 3, message: '长度不小于3个字符', trigger: 'blur' }
        ],
        type: [
          { type: 'array', required: true, message: '请至少选择一个活动性质', trigger: 'change' }
        ]
      },
      fileList: []
    }
  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          // 验证成功
          const res = await setJuBao(this.ruleForm)
          this.$refs.upload.submit()
          if (res.isSuccess) {
            this.$router.push('/')
          }
        } else {
          return false
        }
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    },
    fileChange (file, fileList) {
      // 成功提交后还会调用一次
      this.ruleForm.fileName = []
      for (let i = 0; i < fileList.length; i++) {
        this.ruleForm.fileName[i] = fileList[i].name
      }
    }
    // submitUpload () {
    //   // 手动触发上传
    //   // 会循环调用upLoadFile方法，多个文件调用多次
    //   this.$refs.upload.submit()
    // }
  },
  components: {
    DaoHang,
    FooterPage
  }
}
</script>

<style lang="less" scoped>
.body_jb_form {
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
        width: 500px;
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
