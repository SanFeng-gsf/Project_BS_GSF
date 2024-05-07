<template>
  <div class="grzx">
    <DaoHang></DaoHang>
    <div class="nr">
      <el-container style="width: 100%; border: 1px solid #eee">
        <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
          <el-menu :default-openeds="['1', '3']">
            <el-submenu index="1">
              <template slot="title"><i class="el-icon-message"></i>个人信息</template>
              <el-menu-item-group>
                <el-menu-item index="1-1" @click="changeShow(1)">基本信息</el-menu-item>
                <el-menu-item index="1-2" @click="changeShow(2)">修改信息</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
            <el-submenu index="2">
              <template slot="title"><i class="el-icon-menu"></i>招投标信息</template>
              <el-menu-item-group>
                <el-menu-item index="2-1" @click="changeShow(3)">我的招标</el-menu-item>
                <el-menu-item index="2-2" @click="changeShow(4)">我的投标</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
            <el-submenu index="3">
              <template slot="title"><i class="el-icon-setting"></i>举报信息</template>
              <el-menu-item-group>
                <el-menu-item index="3-1" @click="changeShow(5)">举报记录</el-menu-item>
                <el-menu-item index="3-2" @click="changeShow(6)">违规提示</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
          </el-menu>
        </el-aside>
        <el-container class="right">
          <div class="xx">
            <UserXx v-if="this.show === 1" :userItem="userItem"></UserXx>
            <el-button v-else-if="this.show === 2" @click="changeShow(22)" type="primary" class="change">去修改个人信息</el-button>
            <UserXg v-else-if="this.show === 22" :userItemXg="userItem"></UserXg>
            <MyZb v-else-if="this.show === 3" :name="userItem.name"></MyZb>
            <MyTb v-else-if="this.show === 4" :userItem="userItem"></MyTb>
            <JL v-else-if="this.show === 5" :myName="userItem.name"></JL>
            <WeiGui v-else-if="this.show === 6" :name="userItem.name"></WeiGui>
            <div v-else></div>
          </div>
        </el-container>
      </el-container>
    </div>
    <FooterPage></FooterPage>
  </div>
</template>

<script>
import DaoHang from '@/components/daoHang.vue'
import FooterPage from '@/components/foot.vue'
import UserXx from '@/components/userXx.vue'
import UserXg from '@/components/userXg.vue'
import JL from '@/components/juB/JL.vue'
import WeiGui from '@/components/juB/weiGui.vue'
import { mapGetters } from 'vuex'
import { getUserById } from '@/api/user'
import MyZb from '@/components/myZb.vue'
import MyTb from '@/components/myTb.vue'

// 个人中心
export default {
  name: 'userPage',
  data () {
    return {
      show: 1,
      userItem: {}
    }
  },
  computed: {
    ...mapGetters('user', ['getUserInfo'])
  },
  async created () {
    // 获取根据 id 数据
    const id = this.getUserInfo.userId
    const { data } = await getUserById(id)
    this.userItem = data
  },
  methods: {
    changeShow (n) {
      this.show = n
    }
  },
  components: {
    DaoHang,
    FooterPage,
    UserXx,
    UserXg,
    MyZb,
    MyTb,
    JL,
    WeiGui
  }
}
</script>

<style lang="less" scoped>
.grzx {
  background-color: #c5dfea;
}
.nr {
  margin-left: 150px;
  width: 80%;
}
.right {
  background-color: #ffffff;
}
.xx {
  float: left;
  width: 100%;
  border: 1px solid;
}
.change {
  margin-top: 80px;
  margin-left: 150px;
}
</style>
