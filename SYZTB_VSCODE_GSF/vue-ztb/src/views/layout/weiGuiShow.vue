<template>
  <div class="success">
    <DaoHang></DaoHang>
    <div class="zb_gs">
      <span class="zb_sp">招标公示</span>
    </div>
    <div class="zb">
      <ZbItem v-for="item in newList" :key="item.id" :item="item">
        <span class="nr">违规项目</span><span class="nr_right">该项目已违规</span>
      </ZbItem>
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
import { getWeiGui } from '@/api/zb'

// 违规信息公示

export default {
  name: 'SuccessShowPage',
  data () {
    return {
      page: 0,
      newList: []
    }
  },
  async created () {
    const { data } = await getWeiGui(1)
    if (data === undefined) {
      return
    }
    this.newList = data.records
    this.page = data.pages
  },
  methods: {
    async changePage (page) {
      const { data: { records } } = await getWeiGui(page)
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
.zb_gs {
  width: 100%;
  height: 60px;
  color: #a87070;
  font-weight: bold;
  font-size: 25px;
  text-align: center;
  border-bottom: 1px solid;
  .zb_sp {
    float: left;
    width: 100%;
    text-align: center;
    line-height: 60px;
  }
}
.zb {
  margin-left: 150px;
}
</style>
