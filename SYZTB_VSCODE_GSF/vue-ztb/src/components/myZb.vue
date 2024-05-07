<template>
  <div>
    <div class="title">{{ name }}</div>
    <div v-for="item in zbArr" :key="item.id" class="nr" >
      <div class="zb_nr" v-if="text !==null">
        <el-descriptions class="margin-top" title="项目信息" :column="3" :colon="false">
          <el-descriptions-item label="招标项目:">{{ item.projectName }}</el-descriptions-item>
          <el-descriptions-item label="联系电话:">{{ item.phone }}</el-descriptions-item>
          <el-descriptions-item label="项目报价:">{{ item.price }}万元</el-descriptions-item>
          <el-descriptions-item label="开始时间:">{{ item.createTime }}</el-descriptions-item>
          <el-descriptions-item label="结束时间:">{{ item.endTime }}</el-descriptions-item>
          <el-descriptions-item ></el-descriptions-item>
          <el-descriptions-item label="项目说明:">{{ item.ex }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div v-else class="text">{{ text }}</div>
      <div class="tb_nr">
        <el-table :data="obj[item.projectName]">
          <el-table-column prop="name" label="公司名称" width="200px" fixed="left"></el-table-column>
          <el-table-column prop="peopleName" label="公司法人" width="120px"></el-table-column>
          <el-table-column prop="phone" label="联系电话" width="120px"></el-table-column>
          <el-table-column prop="price" label="项目报价" sortable width="100px"></el-table-column>
          <el-table-column prop="updateTime" label="投标时间" sortable width="180px"></el-table-column>
          <el-table-column width="120px" fixed="right">
            <template slot-scope="scope">
              <el-button @click="handleTb(scope.$index, scope.row)" size="mini" type="danger" class="butt" :class="{success: scope.row.success}">{{ scope.row.success ? '投标成功' : '通过投标' }}</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
// 我的招标信息

import { getByName } from '@/api/zb'
import { getByN, setSuccessById } from '@/api/tb'

export default {
  data () {
    return {
      text: '',
      zbArr: [],
      obj: {}
    }
  },
  props: ['name'],
  async created () {
    const res = await getByName(this.name)
    console.log(res)
    if (res.isSuccess) {
      this.zbArr = res.data
      if (this.zbArr.length > 0) {
        for (let i = 0; i < this.zbArr.length; i++) {
          this.zbArr[i].i = i
          const res1 = await getByN({ suoName: this.name, projectName: this.zbArr[i].projectName })
          this.$set(this.obj, this.zbArr[i].projectName, res1.data)
        }
      }
    } else {
      this.text = res.message
    }
  },
  methods: {
    check (projectName) {
      // 检查是否有人投标成功
      const arr = this.obj[projectName].filter((item) => item.success === true)
      if (arr.length > 0) {
        return true
      }
      return false
    },
    handleTb (index, row) {
      // row 当前行数据
      if (this.check(row.projectName)) {
        this.$message({
          type: 'info',
          message: '该项目已竞标成功'
        })
        return
      }
      this.$confirm('每个项目只能通过一个投标公司，是否确让当前投标公司通过', '是否通过招标', {
        distinguishCancelAndClose: true,
        confirmButtonText: '是',
        cancelButtonText: '否'
      })
        .then(async () => {
          // 修改投标信息 (成功投标)
          await setSuccessById(row.id)
          this.$message({
            type: 'success',
            message: '确认成功'
          })
          this.$router.go(0)
        })
        .catch(() => {
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
.title {
  margin-top: 20px;
  margin-left: 100px;
  color: rgb(195, 90, 90);
  line-height: 50px;
  font-size: 25px;
  font-weight: bold;
}
.zb_nr {
  border: 1px solid;
  border-bottom: none;
  padding: 10px;
  width: 800px;
  margin-top: 20px;
  margin-left: 100px;
}
.tb_nr {
  border: 1px solid;
  padding: 10px;
  width: 800px;
  margin-left: 100px;
  margin-bottom: 20px;
}
.butt {
  background-color: rgb(80, 125, 110);
}
.success {
  background-color: red;
}
.text {
  text-align: center;
  margin-left: 100px;
  padding: 10px;
  width: 800px;
  line-height: 40px;
  font-size: 20px;
}
</style>
