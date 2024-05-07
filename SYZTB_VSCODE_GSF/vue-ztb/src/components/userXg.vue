<template>
  <div class="xg">
    <el-form :model="userItem" :rules="rules" ref="userItem" label-width="100px" class="demo-userItem">
      <el-form-item label="相关资料">
        <img v-if="this.imgUrl !== ''" :src="this.imgUrl" class="img_size">
        <el-upload class="upload-demo" ref="upload" action="http://localhost:8088/userIcon/icon" :file-list="fileIcon"
          :before-upload="beforeAvatarUpload" :on-success="onSuccess" name="file" :auto-upload="false" :limit="1">
          <el-button slot="trigger" size="small" type="primary">选择头像</el-button>
          <el-button size="small" type="success" @click="submitUpload">确认更改</el-button>
          <div slot="tip" class="el-upload__tip">只支持JPG格式，头像大小不超过2MB</div>
        </el-upload>
      </el-form-item>
      <el-form-item label="昵称" prop="nickNime">
        <el-input v-model="userItem.nickNime"></el-input>
      </el-form-item>
      <el-form-item label="公司名称" prop="name">
        <el-input v-model="userItem.name"></el-input>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="userItem.phone"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="userItem.password"></el-input>
      </el-form-item>
      <el-form-item label="公司法人" prop="peopleName">
        <el-input v-model="userItem.peopleName"></el-input>
      </el-form-item>
      <el-form-item label="注册资金(万)" prop="money">
        <el-input v-model="userItem.money"></el-input>
      </el-form-item>
      <el-form-item label="经营时长(年)" prop="year">
        <el-input v-model="userItem.year"></el-input>
      </el-form-item>
      <el-form-item label="竞标成功个数" prop="projectNumber">
        <el-input v-model="userItem.projectNumber"></el-input>
      </el-form-item>
      <el-form-item label="公司规模(人)" prop="number">
        <el-input v-model="userItem.number"></el-input>
      </el-form-item>
      <el-form-item label="公司地址" prop="address">
        <el-input v-model="userItem.address"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('userItem')" class="butt">提交</el-button>
        <el-button @click="cancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { updateUser, getIcon } from '@/api/user'
import { Notification } from 'element-ui'
import md5 from 'js-md5'
// 修改个人信息
export default {
  data () {
    return {
      fileIcon: [],
      rules: {
        nickNime: [
          { min: 1, message: '长度不小于1个字符', trigger: 'blur' }
        ],
        name: [
          { min: 3, message: '长度不小于3个字符', trigger: 'blur' }
        ],
        phone: [
          { min: 11, max: 11, message: '长度为11位', trigger: 'blur' }
        ],
        password: [
          { min: 6, max: 12, message: '长度为6-12位', trigger: 'blur' }
        ]
      },
      userItem: this.userItemXg,
      oldUserItem: this.userItemXg,
      imgUrl: ''
    }
  },
  props: {
    userItemXg: {
      type: Object
    }
  },
  async created () {
    if (this.userItem.icon !== null && this.userItem.icon !== '') {
      const res = await getIcon(this.userItem.icon)
      const blob = new Blob([res])
      // 获取后端发送的图片地址
      this.imgUrl = window.URL.createObjectURL(blob)
    }
  },
  methods: {
    beforeAvatarUpload (file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },
    async onSuccess (response) {
      this.userItem.icon = response.message
      if (response.isSuccess) {
        Notification({
          title: '头像更新成功',
          type: 'success'
        })
      }
    },
    submitUpload () {
      // 上传头像文件
      this.$refs.upload.submit()
    },
    submitForm (formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          // 验证成功
          // 更新用户信息
          if (this.userItem.password !== '') {
            this.userItem.password = md5(this.userItem.name + this.userItem.password)
          }
          const res = await updateUser(this.userItem)
          if (res.isSuccess) {
            this.$router.go(0)
          }
        } else {
          return false
        }
      })
    },
    cancel () {
      this.userItem = this.oldUserItem
      this.$router.go(0)
    }
  }
}
</script>

<style lang="less" scoped>
.img_size {
  width: 60px;
  height: 60px;
}
.el-input {
  width: 400px;
}
.xg {
  margin-left: 150px;
  margin-top: 50px;
  margin-bottom: 50px;
  margin-right: 100px;
}
.butt {
  margin-right: 100px;
}
</style>
