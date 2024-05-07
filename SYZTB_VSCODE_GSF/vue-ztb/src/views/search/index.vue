<template>
  <div>
    <DaoHang></DaoHang>
    <div class="sous">
      <div v-if="getN" class="title">{{ getN }}</div>
      <ZbItem v-for="i in zbArr" :key="i.id" :item="i"></ZbItem>
    </div>
    <FooterPage></FooterPage>
  </div>
</template>

<script>
import DaoHang from '@/components/daoHang.vue'
import FooterPage from '@/components/foot.vue'
import ZbItem from '@/components/zbItem.vue'
import { getByName } from '@/api/zb'

// 搜索页
export default {
  name: 'SearchIndex',
  data () {
    return {
      zbArr: []
    }
  },
  async created () {
    if (this.getN !== null) {
      const res = await getByName(this.getN)
      this.zbArr = res.data
    }
  },
  computed: {
    getN () {
      return this.$route.params.n
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
.title {
  float: left;
  width: 1200px;
  text-align: center;
  line-height: 60px;
  color: #8e3636;
  font-size: 25px;
  font-weight: bold;
  border-bottom: 1px solid;
}
.sous {
  float: left;
  width: 1200px;
  align-items: center;
  border: 1px solid;
  margin-left: 160px;
  margin-bottom: 20px;
}
</style>
