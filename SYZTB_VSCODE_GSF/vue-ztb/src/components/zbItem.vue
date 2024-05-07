<template>
  <div class="item"  @click="$router.push(`/zhaoBDetail/${item.id}`)">
    <div class="gs">
      <span class="nr">招标项目:</span><span class="nr_right">{{ item.projectName }}</span>
      <span class="nr">招标公司:</span><span class="nr_right">{{ item.name }}</span>
      <span class="nr">法人:</span><span class="nr_right">{{ item.peopleName }}</span>
      <span class="nr">联系电话:</span><span class="nr_right">{{ item.phone }}</span>
      <span class="nr">项目报价:</span><span class="nr_right">{{ item.price }}万元</span>
      <span class="nr"></span><span class="nr_right"></span>
      <span class="nr">招标开始时间:</span><span class="nr_right">{{ item.createTime }}</span>
      <span class="nr">招标结束时间:</span><span class="nr_right">{{ item.endTime }}</span>
      <span class="nr"></span><span class="nr_right"></span>
      <slot class="nr"></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ZbItemIndex',
  props: {
    item: {
      type: Object
    }
  },
  data () {
    return {
    }
  }
}
</script>

<style>

</style>
