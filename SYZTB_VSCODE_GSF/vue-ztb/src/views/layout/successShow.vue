<template>
  <div class="success">
    <DaoHang></DaoHang>
    <div class="zb_gs">
      <span class="zb_sp">招标公示</span>
    </div>
    <div class="zb">
      <ZbItem v-for="item in newList" :key="item.id" :item="item"></ZbItem>
    </div>
    <hr>
    <!--
      @size-change 改变每页数据量时会触发
      @current-change 切换当前页面时触发
    -->
    <el-pagination background layout="prev, pager, next" @current-change="changePage" :current-page="1" :page-count="page"></el-pagination>
    <hr>
    <FooterPage></FooterPage>
  </div>
</template>

<script>
import DaoHang from '@/components/daoHang.vue'
import FooterPage from '@/components/foot.vue'
import ZbItem from '@/components/zbItem.vue'
import { getSuccessZb } from '@/api/zb'

// 招标成功的信息公示

export default {
  name: 'SuccessShowPage',
  data () {
    return {
      page: 0,
      newList: []
    }
  },
  async created () {
    const { data } = await getSuccessZb(1)
    if (data === undefined || data === null) {
      return null
    } else {
      this.newList = data.records
      this.page = data.pages
    }
  },
  methods: {
    async changePage (page) {
      const { data: { records } } = await getSuccessZb(page)
      this.newList = records
    }
  },
  components: {
    DaoHang,
    FooterPage,
    ZbItem
  }
}
</script>

<style lang="less" scoped>
.success {
  text-align: center;
}

.zb {
  margin-left: 150px;
}
</style>
