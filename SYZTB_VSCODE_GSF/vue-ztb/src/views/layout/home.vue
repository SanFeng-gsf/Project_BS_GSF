<template>
  <div class="all">
    <DaoHang></DaoHang>
    <div class="main_top">
      <div class="img_top">
        <img class="main_img" src="@/assets/1.jpg">
        <img class="main_img" src="@/assets/a.jpeg">
      </div>
      <div class="text_title"><span>信息公开，公平竞争，你值得信赖</span></div>
    </div>
    <div class="main_one">
      <div class="zb_gs">
        <span @click="$router.push('/successShow')" class="zb_sp">招标公示</span>
        <span @click="$router.push('/successShow')" class="more">更多信息>>></span>
      </div>
      <ZbItem v-for="item in successList" :key="item.id" :item="item"></ZbItem>
    </div>
    <div class="main_one">
      <div class="zb_gs">
        <span @click="$router.push('/zhaoBList')" class="zb_sp">招标信息</span>
        <span @click="$router.push('/zhaoBList')" class="more">更多信息>>></span>
      </div>
      <ZbItem v-for="item in zbList" :key="item.id" :item="item"></ZbItem>
    </div>
    <div class="main_one">
      <div class="zb_gs">
        <span @click="$router.push('/weiGuiShow')" class="zb_sp">违规公示</span>
        <span @click="$router.push('/weiGuiShow')" class="more">更多信息>>></span>
      </div>
      <ZbItem v-for="item in weiGuiList" :key="item.id" :item="item">
        <span class="nr">违规项目</span><span class="nr_right">该项目已违规</span>
      </ZbItem>
    </div>
    <FooterPage></FooterPage>
  </div>
</template>

<script>
// 首页
import DaoHang from '@/components/daoHang.vue'
import FooterPage from '@/components/foot.vue'
import ZbItem from '@/components/zbItem.vue'
import { getSuccessZb, getUnSuccessZb, getWeiGui } from '@/api/zb'

export default {
  name: 'LayoutIndex',
  data () {
    return {
      zbList: [], // 排除了已完成的招标信息
      successList: [], // 成功的招标信息
      weiGuiList: []
    }
  },
  created () {
    this.getSuccessZb()
    this.getUnSuccessZb()
    this.getWeiGui()
  },
  methods: {
    async getSuccessZb () {
      const { data } = await getSuccessZb()
      if (data === undefined || data === null) {
        return
      }
      this.successList = data.records
      if (this.successList.length > 3) {
        this.successList = this.successList.slice(0, 3) // 包前不包后 左闭右开
      }
    },
    async getUnSuccessZb () {
      const { data } = await getUnSuccessZb()
      if (data === undefined || data === null) {
        return
      }
      this.zbList = data.records
      if (this.zbList.length > 3) {
        this.zbList = this.zbList.slice(0, 3) // 包前不包后 左闭右开
      }
    },
    async getWeiGui () {
      const { data } = await getWeiGui()
      if (data === undefined || data === null) {
        return
      }
      this.weiGuiList = data.records
      if (this.weiGuiList.length > 3) {
        this.weiGuiList = this.weiGuiList.slice(0, 3) // 包前不包后 左闭右开
      }
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
.main_top {
  float: left;
  width: 100%;
  height: 410px;

  .img_top {
    float: left;
    width: 100%;
    .main_img {
      width: 50%;
      height: 350px;
    }
  }
  .text_title {
    float: left;
    width: 100%;
    height: 60px;
    color: #ff0000;
    text-align: center;
    font-size: 25px;
    font-weight: bold;
    line-height: 60px;
    border-bottom: 1px solid;
  }
}
</style>
