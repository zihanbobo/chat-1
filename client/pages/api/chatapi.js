import {http, chatListTimeToString} from './common.js'

export default {
  chatList(data, result) {
	  data.list = []
	for (let chat of result) {
		let lastMessage = JSON.parse(chat.lastMessage)
		let lastContent = ''
		if (lastMessage.msgType == 2) {
			lastContent = '[红包]'
		} else if (lastMessage.msgType == 3) {
			lastContent = '[图片]'
		} else {
			lastContent = lastMessage.content
		}
		
		let item = {
			id: chat.userGroupId,
			name: chat.userGroupName, 
			avatar: chat.userGroupAvatar,
			lastMsg: lastContent, 
			lastTime: chatListTimeToString(chat.lastTime)
		}
		item.avatar = item.avatar ? item.avatar : '../../static/icon/default_avatar.png'
		data.list.push(item)
	}
  },
  
  handleMsg(data, result) {
	  if (!result || !result.data || (result.cmd != 11 && result.cmd != 33 && result.cmd != 35 && result.cmd != 36)) {
		  return
	  }
	  
	  let rd = result.data
	  
	  if (result.cmd == 33 || result.cmd == 35) {
		  let id = result.groupId
		  let index = -1 
		  for (let i = 0; i < data.list.length; i++) {
			if (data.list[i].id == id) {
				index = i
				break
			}
		  }
		  if (index >= 0) {
			  data.list.splice(index, 1); 
		  }
		  return
	  }
	  if (result.cmd == 36) {
		  for (let i = 0; i < data.list.length; i++) {
			if (data.list[i].id == result.groupId) {
				data.list[i].name = result.content
				break
			}
		  }
	  }
	  let lastContent = ''
	  if (rd.msgType == 2) {
	  	lastContent = '[红包]'
	  } else if (rd.msgType == 3) {
	  	lastContent = '[图片]'
	  } else {
	  	lastContent = rd.content
	  }
	  let index = -1
	  let item = {
	  	id: rd.chatType == 1 ? rd.groupId : rd.chatId,
	  	lastMsg: lastContent, 
	  	lastTime: chatListTimeToString(rd.createTime),
		name: '',
		avatar: ''
	  }
	  
	  for (let i = 0; i < data.list.length; i++) {
		if (data.list[i].id == item.id) {
			index = i
			item.name = data.list[i].name
			item.avatar = data.list[i].avatar
			item.avatar = item.avatar ? item.avatar : '@/static/icon/default_avatar.png'
			break
		}
	  }
	  if (index > -1) { 
	  	 data.list.splice(index, 1); 
	  } else {
		  if (rd.chatType == 1) {
			  http.post('api/group/info', {groupId: item.id}, function(res) {
				  if (res.code == '10031') {
					  item.name = res.groupName
					  item.avatar = res.avatar
					  item.avatar = item.avatar ? item.avatar : '../../static/icon/default_avatar.png'
				  } else {
				  }
			  })
		  } else {
			  http.get('api/user/userinfo', {userId: item.id}, function(res) {
			  	if (res.code == '10003') {
			  		item.name = res.name
			  		item.avatar = res.avatar
			  		item.avatar = item.avatar ? item.avatar : '../../static/icon/default_avatar.png'
			  	} else {
			  	}
			  })
		  }
	  }
 	  
	  data.list.unshift(item)
  },
  getChatList(data) {
	  let that = this
	  http.get('api/chat/list', {}, function(res) {
		  if (res.code == 10023) {
			  that.chatList(data, res.windows)
			  uni.$on('cmd11', function(result) {
				  that.handleMsg(data, result)
			  })
			  uni.$on('cmd33', function(result) {
				  that.handleMsg(data, result)
			  })
			  uni.$on('cmd35', function(result) {
			  	 that.handleMsg(data, result)
			  })
		  } else {
			  uni.showModal({
			      title: '错误提示',
			      content: '系统错误，请稍后再试！'
			  });
		  }
	  })
  }
}
