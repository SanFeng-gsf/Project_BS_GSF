<template>
      <div class="zb_nr">
        <el-table :data="zbArr" class="table">
          <el-table-column prop="projectName" label="项目名称" fixed="left" width="150px"></el-table-column>
          <el-table-column prop="name" label="公司名称" width="200px"></el-table-column>
          <el-table-column prop="peopleName" label="公司法人"></el-table-column>
          <el-table-column prop="phone" label="联系电话" width="120px"></el-table-column>
          <el-table-column prop="price" label="项目报价" sortable width="120px"></el-table-column>
          <el-table-column prop="ex" label="项目说明" width="200px"></el-table-column>
          <el-table-column prop="updateTime" label="更新时间" sortable width="180px"></el-table-column>
          <el-table-column prop="endTime" label="更新时间" sortable width="180px"></el-table-column>
          <el-table-column fixed="right" label="是否禁用" width="100px">
            <template slot-scope="scope">
              <el-switch v-model="scope.row.close" :active-value="1" :inactive-value="0" @change="changeClose(scope.row)" active-color="red"></el-switch>
            </template>
          </el-table-column>
        </el-table>
      </div>
</template>

<script>
// 招标信息查询 禁用
import { Notification } from 'element-ui'
import { getAllzb, update, getByprojectName } from '@/api/zb'
export default {
  data () {
    return {
      n: false,
      zbArr: []
    }
  },
  props: {
    search: {
      type: String
    }
  },
  async created () {
    if (this.search !== null && this.search !== '') {
      const { data: { records } } = await getByprojectName(this.search, 1)
      this.zbArr = records
    } else {
      const { data: { records } } = await getAllzb()
      this.zbArr = records
    }
  },
  methods: {
    show (res) {
      if (res.isSuccess) {
        Notification({
          title: res.message + ' 请注意刷新',
          type: 'success'
        })
      } else {
        Notification({
          title: res.message + ' 请注意刷新',
          type: 'error'
        })
      }
    },
    async changeClose (row) {
      const res = await update({ id: row.id, close: row.close === 0 ? 0 : 1 })
      this.show(res)
      // this.$store.commit('zbUn/setSearchInfo', { search: '', show: 2 })
      // this.$router.go()
    }
  }
}
</script>

<style lang="less" scoped>
  .zb_nr {
    float: left;
    width: 1250px;
    height: 650px;
    padding-left: 20px;
    .table {
      padding-left: 10px;
      padding-right: 10px;
      padding-bottom: 10px;

    }
  }
</style>
