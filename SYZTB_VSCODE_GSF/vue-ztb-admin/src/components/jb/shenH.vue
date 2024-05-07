<template>
      <div class="zb_nr">
        <el-table :data="juBao" class="table">
          <el-table-column prop="projectName" label="项目名称" fixed="left" width="150px"></el-table-column>
          <el-table-column prop="name" label="公司名称" width="200px"></el-table-column>
          <el-table-column prop="myName" label="举报发起者" width="200px"></el-table-column>
          <el-table-column prop="sm" label="举报说明" width="220px"></el-table-column>
          <el-table-column prop="resource" label="举报原因" sortable width="200px"></el-table-column>
          <el-table-column label="是否隐藏" width="100px">
            <template slot-scope="scope">
              <div>{{ scope.row.hide ? '是' : '否' }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="举报时间" sortable width="180px"></el-table-column>
          <el-table-column fixed="right" label="举报文件" width="100px">
            <template slot-scope="scope">
              <el-button @click="handleTb(scope.$index, scope.row)" size="mini" type="danger" class="butt">{{ scope.row.fileName ? '下载' : '暂无文件' }}</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
</template>

<script>
// 举报信息审核
import { getJuBao, getJuBaoFile } from '@/api/juBao'
export default {
  data () {
    return {
      juBao: [],
      jbFile: [
        { name: '', url: [] }
      ],
      imgUrl: [{ name: '', url: [] }],
      type: '',
      fileItem: [{ name: '', fileName: [] }]
    }
  },
  props: ['search'],
  async created () {
    if (this.search !== null && this.search !== '') {
      const { data: { JuBao, url } } = await getJuBao(this.search)
      this.getJuB(JuBao, url)
    } else {
      const { data: { JuBao, url } } = await getJuBao()
      this.getJuB(JuBao, url)
    }
  },
  methods: {
    async getJuB (JuBao, url) {
      this.juBao = JuBao
      if (url.length !== 0) {
        for (let i = 0; i < JuBao.length; i++) {
          this.fileItem[i].name = JuBao[i].name
          this.imgUrl[i].name = JuBao[i].name
          for (let j = 0; j < JuBao[i].fileName.length; j++) {
            this.fileItem[i].fileName[j] = JuBao[i].fileName[j]
          }
        }
        for (let i = 0; i < url.length; i++) {
          this.jbFile[i].name = url[i].pop()
          for (let j = 0; j < url[i].length; j++) {
            const arrItem = url[i][j]
            this.jbFile[i].url[j] = arrItem
            const res = await getJuBaoFile(this.jbFile[i].url[j])
            if (this.jbFile[i].name === this.fileItem[i].name) {
              this.type = this.fileItem[i].fileName[j].split('.')[1]
            }
            const blob = new Blob([res], { type: this.type })
            this.imgUrl[i].url[j] = window.URL.createObjectURL(blob)
          }
        }
      }
    },
    handleTb (n, res) {
      const item = this.imgUrl.filter((item) => item.name === res.name)
      if (item[0].url.length > 0) {
        const a = document.createElement('a')
        a.style.display = 'none'
        for (let i = 0; i < item[0].url.length; i++) {
          a.download = res.fileName[i]
          a.href = item[0].url[i]
          a.click()
        }
      }
    }
  }
}
</script>

<style lang="less" scoped>
  .zb_nr {
    float: left;
    width: 1250px;
    height: 650px;
    padding-left: 20px;
    .table {
      padding-left: 10px;
      padding-right: 10px;
      padding-bottom: 10px;

    }
  }
</style>
