<template>
  <div class="input">
    <el-input v-model="search" placeholder="请输入项目名称" prefix-icon="el-icon-search" class="inp"></el-input>
    <el-button @click="goSearch" type="primary" size="medium" icon="el-icon-search">搜索</el-button>
  </div>
</template>

<script>
// 搜索框

export default {
  name: 'SearchIndex',
  data () {
    return {
      search: ''
    }
  },
  props: {
    // 确保哪个页面在搜索
    show: {
      type: Number
    },
    newSearch: {
      type: String
    }
  },
  created () {
    if (this.newSearch !== null && this.newSearch !== '') {
      this.search = this.newSearch
    }
  },
  methods: {
    goSearch () {
      if (this.search !== null) {
        this.$store.commit('zbUn/setSearchInfo', { search: this.search, show: this.show })
        this.$router.go()
      }
    }
  }
}
</script>

<style lang="less" scoped>
.input {
  margin-left: 50px;
  margin-top: 10px;
  width: 510px;
  .inp {
    width: 400px;
    margin-right: 20px;
  }
}

</style>
