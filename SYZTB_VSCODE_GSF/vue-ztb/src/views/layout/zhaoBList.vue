<template>
  <div class="unsuccess">
    <DaoHang></DaoHang>
    <div class="zb_xx">
      <span class="xx">招标信息</span>
    </div>
    <div class="zb">
      <ZbItem v-for="item in newList" :key="item.id" :item="item"></ZbItem>
    </div>
    <hr>
    <el-pagination background layout="prev, pager, next" @current-change="changePage" :current-page="1" :page-count="page"></el-pagination>
    <hr>
    <FooterPage></FooterPage>
  </div>
</template>

<script>
// 招标信息

import DaoHang from '@/components/daoHang.vue'
import FooterPage from '@/components/foot.vue'
import ZbItem from '@/components/zbItem.vue'
import { getUnSuccessZb } from '@/api/zb'

export default {
  data () {
    return {
      page: 0,
      newList: []
    }
  },
  async created () {
    const { data: { records, pages } } = await getUnSuccessZb(1)
    this.newList = records
    this.page = pages
  },
  methods: {
    async changePage (page) {
      const { data: { records } } = await getUnSuccessZb(page)
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
.unsuccess {
  text-align: center;
  .zb_xx {
    width: 100%;
    height: 60px;
    color: #a87070;
    font-weight: bold;
    font-size: 25px;
    text-align: center;
    border-bottom: 1px solid;
    .xx {
      float: left;
      width: 100%;
      text-align: center;
      line-height: 60px;
    }
  }
  .zb {
    margin-left: 150px;
  }
}
</style>
