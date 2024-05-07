import Vue from 'vue'
import 'element-ui/lib/theme-chalk/index.css'

import {
  Input, Button, MessageBox, Message, Rate, Pagination, Form, FormItem, Container, Aside, Menu, Submenu,
  MenuItemGroup, MenuItem, Checkbox, CheckboxGroup, Switch, Upload, Descriptions, DescriptionsItem, Avatar,
  Table, TableColumn, TimePicker, DatePicker, Dialog
} from 'element-ui'

Vue.use(Button)
Vue.use(Input)
Vue.use(Rate)
Vue.use(Pagination)
Vue.use(Form)
Vue.use(FormItem)
Vue.use(Container)
Vue.use(Aside)
Vue.use(Menu)
Vue.use(Submenu)
Vue.use(MenuItemGroup)
Vue.use(MenuItem)
Vue.use(Checkbox)
Vue.use(CheckboxGroup)
Vue.use(Switch)
Vue.use(Upload)
Vue.use(Descriptions)
Vue.use(DescriptionsItem)
Vue.use(Avatar)
Vue.use(Table)
Vue.use(TableColumn)
Vue.use(TimePicker)
Vue.use(DatePicker)
Vue.use(Dialog)

Vue.prototype.$confirm = MessageBox.confirm
// Vue.prototype.$notify = Notification
Vue.prototype.$message = Message
// Vue.prototype.$loading = Loading
