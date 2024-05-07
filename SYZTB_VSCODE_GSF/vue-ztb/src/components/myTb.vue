<template>
  <div class="">
    <div class="title">{{ userItem.name }}</div>
    <div class="tb_nr">
      <el-table :data="tbArr">
        <el-table-column prop="suoName" label="公司名称" width="200px" fixed="left"></el-table-column>
        <el-table-column prop="projectName" label="项目名称" width="200px"></el-table-column>
        <el-table-column prop="price" label="我的报价" sortable  width="120px"></el-table-column>
        <el-table-column prop="updateTime" label="投标时间" sortable width="170px"></el-table-column>
        <el-table-column label="是否成功"  width="120px">
          <template slot-scope="scope" v-if="!isSucces">
            <div>{{ scope.row.success ? '投标成功' : '暂无结果' }}</div>
          </template>
          <template slot-scope="scope" v-else>
            <div>{{ scope.row.success ? '投标成功' : '投标失败' }}</div>
          </template>
        </el-table-column>
        <el-table-column  width="120px" fixed="right">
          <template slot-scope="scope">
            <el-button @click="handleTb(scope.$index, scope.row)" size="mini" type="danger">招标详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
// 我的投标
import { selectByName } from '@/api/tb'
export default {
  data () {
    return {
      text: '',
      isSucces: false,
      tbArr: [],
      obj: {}
    }
  },
  props: {
    userItem: {
      type: Object
    }
  },
  async created () {
    const res = await selectByName(this.userItem.name)
    if (res.isSuccess) {
      this.tbArr = res.data
    } else {
      this.text = res.message
    }
  },
  methods: {
    handleTb (index, row) {
      // row 当前行数据
      this.$router.push(`/zhaoBdetail/${row.id}`)
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
.tb_nr {
  border: 1px solid;
  padding: 10px;
  width: 800px;
  margin-left: 100px;
  margin-bottom: 20px;
}
</style>
