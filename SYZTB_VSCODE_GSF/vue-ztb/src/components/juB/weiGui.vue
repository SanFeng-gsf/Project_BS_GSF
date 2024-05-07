<template>
  <div class="wg">
    <el-table :data="weiGui">
      <el-table-column label="发起公司" fixed="left" width="180px">
        <template slot-scope="scope">
          <div class="color">{{ scope.row.hide ? '该公司已匿名' : scope.row.myName }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="举报原因" width="250px"></el-table-column>
      <el-table-column prop="resource" label="其他原因" width="250px"></el-table-column>
      <el-table-column prop="sm" label="举报说明" width="250px"></el-table-column>
      <el-table-column prop="createTime" label="举报时间" sortable width="180px"></el-table-column>
      <el-table-column label="" width="180px" fixed="right">
        <template>
          <div class="color">您已违规</div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getByName } from '@/api/juBao'
export default {
  data () {
    return {
      weiGui: []
    }
  },
  props: ['name'],
  async created () {
    const res = await getByName(this.name)
    if (res.isSuccess) {
      this.weiGui = res.data
    }
  }
}
</script>

<style lang="less" scoped>
.wg {
  border: 1px solid;
  margin-left: 35px;
  margin-top: 20px;
  width: 900px;
  padding: 10px;
}
.color {
  color: #ff0000;
  font-weight: bold;
}
</style>
