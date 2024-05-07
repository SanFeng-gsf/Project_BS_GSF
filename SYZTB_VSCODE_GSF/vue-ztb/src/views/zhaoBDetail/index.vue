<template>
  <div>
    <DaoHang></DaoHang>
    <div class="main_one">
      <div class="zb_gs">
        <span class="zb_sp">详细信息</span>
        <div class="butto"><el-button @click="insertTb" type="success" round>参与投标</el-button></div>
        <el-dialog title="投标信息" :visible.sync="dialogFormVisible" width="600px">
          <el-form :model="form">
            <el-form-item label="招标公司" :label-width="formLabelWidth">
              <div class="xians">{{ form.suoName }}</div>
            </el-form-item>
            <el-form-item label="项目名称" :label-width="formLabelWidth">
              <div class="xians">{{ form.projectName }}</div>
            </el-form-item>
            <el-form-item label="投标公司" :label-width="formLabelWidth">
              <div class="xians">{{ form.name }}</div>
            </el-form-item>
            <el-form-item label="公司法人" :label-width="formLabelWidth">
              <div class="xians">{{ form.peopleName }}</div>
            </el-form-item>
            <el-form-item label="注册资金" :label-width="formLabelWidth">
              <div class="xians">{{ form.money }}</div>
            </el-form-item>
            <el-form-item label="成立时长" :label-width="formLabelWidth">
              <div class="xians">{{ form.year }}</div>
            </el-form-item>
            <el-form-item label="项目完成数" :label-width="formLabelWidth">
              <div class="xians">{{ form.projectNumber }}</div>
            </el-form-item>
            <el-form-item label="公司规模" :label-width="formLabelWidth">
              <div class="xians">{{ form.number }}</div>
            </el-form-item>
            <el-form-item label="联系电话" :label-width="formLabelWidth">
              <div class="inp"><el-input v-model="form.phone"></el-input></div>
            </el-form-item>
            <el-form-item label="投标报价" :label-width="formLabelWidth">
              <div class="inp"><el-input v-model="form.price"></el-input></div>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog_footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="touBInsert" style="margin-left: 200px;">投 标</el-button>
          </div>
        </el-dialog>
      </div>
      <div class="title">招标信息</div>
      <ZbItem :item="zhaoB"><span class="nr">项目说明:</span><span class="nr_ex">{{ zhaoB.ex }}</span></ZbItem>
      <div class="title">投标信息</div>
      <TbItem :item="touB" :isFinish="isFinish"></TbItem>
    </div>
    <FooterPage></FooterPage>
  </div>
</template>

<script>
import DaoHang from '@/components/daoHang.vue'
import FooterPage from '@/components/foot.vue'
import ZbItem from '@/components/zbItem.vue'
import TbItem from '@/components/tbItem.vue'
import { getById, isFinish } from '@/api/zb'
import { getUserById } from '@/api/user'
import { touBInsert } from '@/api/tb'

// 招标信息详情
export default {
  name: 'detailIndex',
  data () {
    return {
      zhaoB: {},
      touB: [],
      isFinish: false,
      formLabelWidth: '120px',
      dialogFormVisible: false,
      form: {
        suoName: '',
        projectName: '',
        name: '',
        phone: '',
        peopleName: '',
        money: undefined,
        year: undefined,
        projectNumber: undefined,
        number: undefined,
        price: undefined
      }
    }
  },
  async created () {
    if (this.user.token === undefined || this.user.token === null || this.user.token === '') {
      this.$confirm('检测到当前页面需要登入权限，是否登入获取权限 ', '登入提示', {
        distinguishCancelAndClose: true,
        confirmButtonText: '去登入',
        cancelButtonText: '取消'
      })
        .then(() => {
          this.$router.push('/login')
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消'
          })
          this.$router.go(-1)
        })
    } else {
      this.getData()
    }
  },
  methods: {
    async getData () {
      const id = this.getId
      this.isFinish = await isFinish(id)
      const { data } = await getById(id)
      if (data === undefined) {
        this.$router.go()
      }
      this.zhaoB = data[0].zhaoB
      if (data[1].touB === -1 || data[1].touB.length === 0) {
        return null
      } else {
        this.touB = data[1].touB
      }
    },
    checkUser (user) {
      if (user.ban === 1) {
        this.$message({
          type: 'error',
          message: '您的账号已被冻结，请联系官方客服查询'
        })
        return false
      }
      if (user.money === null || user.number === null || user.peopleName === '' || user.projectNumber === null || user.year === null) {
        this.$confirm('参与投标需要先完善个人信息，是否去完善个人信息 ', '完善个人信息提示', {
          distinguishCancelAndClose: true,
          confirmButtonText: '前往个人中心',
          cancelButtonText: '取消'
        })
          .then(() => {
            this.$router.push('/user')
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消'
            })
            return false
          })
      }
      return true
    },
    async check () {
      const { data } = await getUserById(this.user.id)
      if (!this.checkUser(data)) {
        return false
      } else {
        this.form.name = data.name
        this.form.peopleName = data.peopleName
        this.form.money = data.money
        this.form.year = data.year
        this.form.projectNumber = data.projectNumber
        this.form.number = data.number
        return true
      }
    },
    insertTb () {
      if (this.zhaoB !== null) {
        if (this.zhaoB.close === 1) {
          this.$message({
            type: 'error',
            message: '该项目存在风险已被禁止'
          })
          return
        }
        this.form.suoName = this.zhaoB.name
        this.form.projectName = this.zhaoB.projectName
      }
      if (this.check()) {
        this.dialogFormVisible = true
      }
    },
    async touBInsert () {
      // 投标注册
      const res = await touBInsert(this.form)
      console.log(this.form)
      console.log(res)
      this.dialogFormVisible = false
    }
  },
  computed: {
    getId () {
      return this.$route.params.id
    },
    user () {
      const token = this.$store.getters.token
      const id = this.$store.getters.userId
      return { token: token, id: id }
    }
  },
  components: {
    DaoHang,
    FooterPage,
    ZbItem,
    TbItem
  }
}
</script>

<style lang="less" scoped>
.title {
  float: left;
  margin-top: 25px;
  margin-left: 100px;
  width: 1000px;
  text-align: center;
  height: 40px;
  line-height: 40px;
  font-size: 20px;
  font-weight: bold;
  color: #855f5f;
}
.butto {
  margin-top: 25px;
  margin-left: 900px;
  height: 50px;
}
.xians {
  padding-left: 20px;
  text-align: left;
  width: 250px;
}
.inp {
  width: 250px;
}
.dialog_footer {
  width: 450px;
}

.none {
  float: left;
  margin-left: 100px;
  width: 1000px;
  line-height: 200px;
  height: 200px;
  text-align: center;
  font-size: 30px;
  color: #855f5f;
  background-color: #c8f2ff;
}
</style>
