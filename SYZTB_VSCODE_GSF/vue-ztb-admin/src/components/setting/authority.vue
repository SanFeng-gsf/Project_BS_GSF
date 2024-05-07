<template>
  <div class="nr">
        <el-table :data="list" class="table">
          <el-table-column prop="name" label="账号" fixed="left" width="150px"></el-table-column>
          <el-table-column prop="password" label="密码" width="300px"></el-table-column>
          <el-table-column label="高级管理员" width="150px">
            <template slot-scope="scope">
              <el-switch v-model="scope.row.admin" :active-value="value" :inactive-value="defaultValue" @change="changeAdmin(scope.row.name)" active-color="red"></el-switch>
            </template>
          </el-table-column>
          <el-table-column width="150px">
            <template slot-scope="scope">
              <el-button type="danger" @click="del(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
  </div>
</template>

<script>
import { Notification } from 'element-ui'
import { getAll, getSysUser, deleteById, updateAdmin } from '@/api/user'
export default {
  name: 'AuthorityIndex',
  data () {
    return {
      list: [],
      value: 'Admin',
      defaultValue: ''
    }
  },
  computed: {
    name () {
      return this.$store.getters.name
    }
  },
  async created () {
    const res = await getSysUser(this.name)
    this.user = res.data
    if (this.user.admin !== 'Admin') {
      this.$store.commit('zbUn/setSearchInfo', { search: '', show: 1 })
      this.$router.go()
    }
    const res1 = await getAll()
    if (!res1.isSuccess) {
      return null
    } else {
      this.list = res1.data
    }
  },
  methods: {
    async changeAdmin (name) {
      const res = await updateAdmin(name)
      console.log(res)
      if (res.isSuccess) {
        Notification({
          title: res.message,
          type: 'success'
        })
      } else {
        Notification({
          title: res.message,
          type: 'error'
        })
      }
      this.$store.commit('zbUn', {})
      this.$router.go()
    },
    async del (id) {
      const res = await deleteById(id)
      console.log(res)
      Notification({
        title: '头像更新成功',
        type: 'success'
      })
    }
  }
}
</script>

<style lang="less" scoped>
  .nr {
    margin-top: 100px;
    margin-left: 200px;
    padding-left: 20px;
    width: 800px;
    border: 1px solid;
    .table {
      padding-left: 10px;
      padding-right: 10px;
      padding-bottom: 10px;

    }
  }
</style>
