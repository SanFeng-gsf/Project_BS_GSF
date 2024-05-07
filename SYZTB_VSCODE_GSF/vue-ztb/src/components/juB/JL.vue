<template>
  <div class="tb_nr">
    <el-table :data="juBao">
      <el-table-column prop="name" label="公司名称" fixed="left" width="180px"></el-table-column>
      <el-table-column prop="type" label="举报原因" width="180px"></el-table-column>
      <el-table-column prop="resource" label="其他原因" width="250px"></el-table-column>
      <el-table-column prop="sm" label="举报说明" width="250px"></el-table-column>
      <el-table-column label="是否隐藏">
        <template slot-scope="scope">
          <div>{{ scope.row.hide ? '是' : '否' }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="举报时间" sortable width="180px"></el-table-column>
      <el-table-column label="举报文件" fixed="right">
        <template slot-scope="scope">
          <el-button @click="handleTb(scope.$index, scope.row)" size="mini" type="danger" class="butt">{{ scope.row.fileName ? '下载' : '暂无文件' }}</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
// 举报信息
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
  props: ['myName'],
  async created () {
    const { data: { JuBao, url } } = await getJuBao(this.myName)
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
  methods: {
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
.tb_nr {
  border: 1px solid;
  margin-left: 50px;
  margin-top: 20px;
  width: 900px;
  padding: 10px;
}
</style>
