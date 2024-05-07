<template>
  <div class="userxx">
    <el-descriptions class="margin-top" title="基本信息" :column="2" border>
      <slot></slot>
      <el-descriptions-item>
        <template slot="label">头像</template>
        <img v-if="this.imgUrl !== ''" :src="this.imgUrl" class="img_size">
        <el-button @click="getIcon(userItem.icon)" v-else type="info" round>获取头像</el-button>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">昵称</template>{{ userItem.nickNime }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">公司名称</template>{{ userItem.name }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">手机号</template>{{ userItem.phone }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">密码</template>******
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">公司法人</template>{{ userItem.peopleName }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">注册资金</template>{{ userItem.money }}万元
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">经营时长</template>{{ userItem.year }}年
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">竞标成功数</template>{{ userItem.projectNumber }}个
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">公司规模</template>{{ userItem.number }}人
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">公司地址</template>{{ userItem.address }}
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script>
import { getIcon } from '@/api/user'
export default {
  data () {
    return {
      imgUrl: ''
    }
  },
  props: {
    userItem: {
      type: Object
    }
  },
  methods: {
    async getIcon (icon) {
      if (icon === '') {
        this.$message({
          message: '您还未设置头像',
          type: 'info'
        })
        return
      }
      const res = await getIcon(icon)
      const blob = new Blob([res])
      // 获取后端发送的图片地址
      this.imgUrl = window.URL.createObjectURL(blob)
    }
  }
}
</script>

<style lang="less" scoped>
.img_size {
  width: 60px;
  height: 60px;
}
.userxx {
  width: 90%;
}
.margin-top {
  margin-top: 50px;
  margin-left: 100px;
  margin-bottom: 50px;
}
</style>
