<template>
      <div class="zb_nr">
        <el-table :data="zbArr" class="table">
          <el-table-column prop="name" label="公司名称" fixed="left" width="200px"></el-table-column>
          <el-table-column prop="peopleName" label="公司法人" width="120px"></el-table-column>
          <el-table-column prop="phone" label="联系电话" width="120px"></el-table-column>
          <el-table-column prop="nickName" label="账号昵称" width="200px"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" sortable width="200px"></el-table-column>
          <el-table-column prop="updateTime" label="更新时间" sortable width="200px"></el-table-column>
          <el-table-column fixed="right" label="是否禁用" width="120px">
            <template slot-scope="scope">
              <el-switch v-model="scope.row.ban" :active-value="1" :inactive-value="0" @change="changeBan(scope.row)" active-color="red"></el-switch>
            </template>
          </el-table-column>
        </el-table>
      </div>
</template>

<script>
// 违规账号禁用
import { Notification } from 'element-ui'
import { getUser, updateUser } from '@/api/user'
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
      const res = await getUser(this.search)
      if (!res.isSuccess) {
        return
      }
      this.zbArr = res.data
    } else {
      const res = await getUser()
      if (!res.isSuccess) {
        return
      }
      this.zbArr = res.data
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
    async changeBan (row) {
      console.log(row)
      console.log(row.close)
      const res = await updateUser({ id: row.id, ban: row.ban === 0 ? 0 : 1 })
      this.show(res)
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
