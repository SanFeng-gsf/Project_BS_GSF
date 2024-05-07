<template>
      <div class="zb_nr">
        <el-table :data="zbUnArr" class="table">
          <el-table-column prop="projectName" label="项目名称" fixed="left" width="180px"></el-table-column>
          <el-table-column prop="name" label="公司名称" width="200px"></el-table-column>
          <el-table-column prop="peopleName" label="公司法人"></el-table-column>
          <el-table-column prop="phone" label="联系电话" width="120px"></el-table-column>
          <el-table-column prop="price" label="项目报价" sortable width="140px"></el-table-column>
          <el-table-column prop="ex" label="项目说明" width="200px"></el-table-column>
          <el-table-column prop="updateTime" label="更新时间" sortable width="180px"></el-table-column>
          <el-table-column prop="endTime" label="更新时间" sortable width="180px"></el-table-column>
          <el-table-column fixed="right" label="是否审核通过" width="200px">
            <template slot-scope="scope">
              <div v-if="scope.row.pass === '0'">
                <el-button @click="changePsss(scope.row)" size="mini" type="danger">
                  通过
                </el-button>
                <el-button @click="changeUnPsss(scope.row)" size="mini" type="danger">
                  不通过
                </el-button>
              </div>
              <div v-else-if="scope.row.pass === '1'" class="pass">已通过</div>
              <div v-else-if="scope.row.pass === '-1'" class="unPass">不通过</div>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination background layout="prev, pager, next" @current-change="changePage" :current-page="1" :page-count="page"></el-pagination>
      </div>
</template>

<script>
// 招标发布审核
import { Notification } from 'element-ui'
import { getInsertAll, updateCheck, getByProjectName } from '@/api/zbUn'
export default {
  name: 'ZbInsertIndex',
  data () {
    return {
      page: 0,
      zbUnArr: []
    }
  },
  props: {
    search: {
      type: String
    }
  },
  async created () {
    if (this.search !== null && this.search !== '') {
      const res = await getByProjectName(this.search)
      if (res.isSuccess) {
        this.zbUnArr = res.data
      }
    } else {
      const { data: { records, pages } } = await getInsertAll()
      this.zbUnArr = records
      this.page = pages
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
    async changePsss (row) {
      const res = await updateCheck({ id: row.id, pass: '1' })
      this.show(res)
      this.$router.go()
      console.log(res)
    },
    async changeUnPsss (row) {
      const res = await updateCheck({ id: row.id, pass: '-1' })
      this.show(res)
      this.$router.go()
      console.log(res)
    },
    async changePage (page) {
      const { data: { records } } = await getInsertAll(page)
      this.zbUnArr = records
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

.pass {
  color: red;
}
.unPass {
  color: rgb(193, 193, 193);
}
.success {
  background-color: rgb(47, 255, 0);
  color: black;
}
</style>
