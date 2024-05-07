<template>
  <div class="user">
    <el-dropdown split-button type="primary" @click="handleClick" @command="handleCommand">管理员 &nbsp;&nbsp; {{ name }}
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="n">退出系统</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
// 管理员显示
export default {
  name: 'userIndex',
  data () {
    return {

    }
  },
  computed: {
    name () {
      return this.$store.getters.name
    }
  },
  methods: {
    handleClick () {

    },
    handleCommand () {
      this.$store.dispatch('user/logout')
      this.$router.go()
    }
  }
}
</script>

<style lang="less" scoped>
.user {
  margin-top: 10px;
}
</style>
