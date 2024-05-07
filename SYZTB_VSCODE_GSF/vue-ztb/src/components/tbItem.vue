<template>
  <div class="tb_nr">
    <el-table :data="item">
      <el-table-column prop="projectName" label="投标项目" fixed="left" width="180px"></el-table-column>
      <el-table-column prop="name" label="公司名称" width="180px"></el-table-column>
      <el-table-column prop="peopleName" label="公司法人" width="100px"></el-table-column>
      <el-table-column prop="phone" label="联系电话" width="140px"></el-table-column>
      <el-table-column prop="suoName" label="所投公司" width="180px"></el-table-column>
      <el-table-column prop="price" label="项目报价" sortable width="100px"></el-table-column>
      <el-table-column prop="money" label="注册资金" sortable width="100px"></el-table-column>
      <el-table-column prop="year" label="成立时长" sortable width="100px"></el-table-column>
      <el-table-column prop="projectNumber" label="成功投标个数" sortable width="150px"></el-table-column>
      <el-table-column prop="number" label="公司人员规模" sortable width="150px"></el-table-column>
      <el-table-column prop="updateTime" label="投标时间" sortable width="180px"></el-table-column>
      <el-table-column label="是否成功" width="100px">
        <template slot-scope="scope" v-if="!isFinish">
          <div>{{ scope.row.success ? '投标成功' : '暂无结果' }}</div>
        </template>
        <template slot-scope="scope" v-else>
          <div>{{ scope.row.success ? '投标成功' : '投标失败' }}</div>
        </template>
      </el-table-column>
      <el-table-column label="评分" width="180px" fixed="right">
        <template>
          <el-rate v-model="grade" disabled-colors="red" disabled show-score text-color="red" score-template="{value}" disabled-void-color="#5c99ab"></el-rate>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { comput } from '@/utils/computer'

export default {
  name: 'TbItemIndex',
  props: {
    item: {
      type: Array
    },
    isFinish: {
      type: Boolean
    }
  },
  data () {
    return {
      grade: 0
    }
  },
  created () {
    this.grade = comput(this.item)
  },
  methods: {

  }
}
</script>

<style lang="less" scoped>
.tb_nr {
  border: 1px solid;
  padding: 10px;
  width: 1100px;
  margin-left: 50px;
  margin-bottom: 20px;
}
</style>
