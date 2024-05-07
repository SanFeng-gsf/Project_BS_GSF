<template>
    <div class="nr">
      <el-container style="width: 100%; border: 1px solid #eee">
        <el-aside width="200px" class="left">
          <div class="tit">招得快后台管理系统</div>
          <el-menu :default-openeds="['1', '3']" text-color="white" background-color="#1f2569" active-text-color="red">
            <el-submenu index="1">
              <template slot="title">招标信息</template>
              <el-menu-item-group>
                <el-menu-item index="1-1" @click="changeShow(1)">招标发布审核</el-menu-item>
                <el-menu-item index="1-2" @click="changeShow(2)">招标信息管理</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
            <el-submenu index="2">
              <template slot="title">举报信息</template>
              <el-menu-item-group>
                <el-menu-item index="2-1" @click="changeShow(3)">举报信息审核</el-menu-item>
                <el-menu-item index="2-2" @click="changeShow(4)">违规账号禁用</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
            <el-submenu index="3">
              <template slot="title">设置管理</template>
              <el-menu-item-group>
                <el-menu-item index="3-1" @click="changeShow(5)">个人信息管理</el-menu-item>
                <el-menu-item index="3-2" @click="changeShow(6)">新建管理员</el-menu-item>
                <el-menu-item index="3-3" @click="changeShow(7)">权限管理</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
          </el-menu>
        </el-aside>
        <el-container class="right">
          <div class="xx">
            <div class="top">
              <div class="search"><Search :show="show" :newSearch="search"></Search></div>
              <div class="user"><User></User></div>
            </div>
            <ZbInsert v-if="this.show === 1" :search="search"></ZbInsert>
            <ZbSelect v-else-if="this.show === 2" :search="search"></ZbSelect>
            <ShenH v-else-if="this.show === 3" :search="search"></ShenH>
            <WeiGuiUser v-else-if="this.show === 4" :search="search"></WeiGuiUser>
            <Self v-else-if="this.show === 5"></Self>
            <Insert v-else-if="this.show === 6"></Insert>
            <Authority v-else-if="this.show === 7"></Authority>
          </div>
        </el-container>
      </el-container>
    </div>
</template>

<script>
import Search from '@/components/search.vue'
import User from '@/components/user.vue'
import ZbSelect from '@/components/zbCheck/zbSelect.vue'
import ZbInsert from '@/components/zbCheck/zbInsert.vue'
import ShenH from '@/components/jb/shenH.vue'
import WeiGuiUser from '@/components/jb/weiGuiUser.vue'
import Self from '@/components/setting/self.vue'
import Insert from '@/components/setting/insert.vue'
import Authority from '@/components/setting/authority.vue'
import { mapGetters } from 'vuex'
export default {
  name: 'HomeIndex',
  data () {
    return {
      show: 1,
      search: ''
    }
  },
  computed: {
    ...mapGetters('zbUn', ['getSearchInfo'])
  },
  created () {
    if (this.getSearchInfo.search !== null && this.getSearchInfo.search !== '') {
      this.search = this.getSearchInfo.search
      this.show = this.getSearchInfo.show
      this.$store.dispatch('zbUn/delete')
    }
    if (this.getSearchInfo.show !== undefined) {
      this.show = this.getSearchInfo.show
      this.$store.dispatch('zbUn/delete')
    }
  },
  methods: {
    changeShow (n) {
      this.show = n
    }
  },
  components: {
    Search,
    User,
    ZbInsert,
    ZbSelect,
    ShenH,
    WeiGuiUser,
    Self,
    Insert,
    Authority
  }
}
</script>

<style lang="less" scoped>
  .top {
    float: left;
    padding-top: 5px;
    padding-bottom: 15px;
    width: 1310px;
    border-bottom: 1px solid;
    .search {
      float: left;
      width: 600px;
    }
    .user {
      margin-left: 450px;
      width: 325px;
    }
  }
.tit {
  line-height: 40px;
  font-size: small;
  color: rgb(255, 0, 0);
  font-weight: bold;
}
.left {
  background-color: #1f2569;
}
.right {
  background-color: #ffffff;
}
.xx {
  float: left;
  width: 100%;
  height: 720px;
  border: 1px solid;
}
.change {
  margin-top: 80px;
  margin-left: 150px;
}
</style>
