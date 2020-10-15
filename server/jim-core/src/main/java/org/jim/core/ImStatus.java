/**
 * 
 */
package org.jim.core;

/**
 * 版本: [1.0]
 * 功能说明: 
 *  创建时间: 7月27日 上午10:33:14
 */
public enum ImStatus implements Status{
	
	C10000(10000,"ok","发送成功"),
	C10001(10001,"offline","用户不在线"),
	C10019(10019,"online","用户在线"),
	C10002(10002,"send failed","消息发送失败,数据格式不正确,请参考:{'from':来源ID,'to':目标ID,'cmd':'消息命令码','createTime':消息创建时间(Long型),'msgType':Integer消息类型,'content':内容}"),
	C10003(10003,"ok","获取用户信息成功!"),
	C10004(10004,"get user failed !","获取用户信息失败!"),
	C10005(10005,"ok","获取所有在线用户信息成功!"),
	C10006(10006,"ok","获取所有离线用户信息成功!"),
	C10007(10007,"ok","登录成功!"),
	C10008(10008,"login failed !","登录失败!"),
	C10009(10009,"ok","鉴权成功!"),
	C10010(10010,"auth failed!","鉴权失败!"),
	C10011(10011,"join group ok!","加入群组成功!"),
	C10012(10012,"join group failed!","加入群组失败!"),
	C10013(10013,"Protocol version number does not match","协议版本号不匹配!"),
	C10014(10014,"unsupported cmd command","不支持的cmd命令!"),
	C10015(10015,"get user message failed!","获取用户消息失败!"),
	C10016(10016,"get user message ok!","获取离线消息成功!"),
	C10017(10017,"cmd failed!","未知的cmd命令!"),
	C10018(10018,"get user message ok!","获取历史消息成功!"),
	C10020(10020,"Invalid verification!","不合法校验"),
	C10021(10021,"close ok!","关闭成功"),
	C10022(10022,"get chat list failed!","获取聊天列表失败!"),
	C10023(10023,"get chat list ok!","获取聊天列表成功!"),
	C10024(10024,"get packet state failed!","获取红包状态失败!"),
	C10025(10025,"get packet state ok!","获取红包状态成功!"),
	C10026(10026,"get friend list failed!","获取好友列表失败!"),
	C10027(10027,"get friend list ok!","获取好友列表成功!"),
	C10028(10028,"apply friend failed!","申请好友失败!"),
	C10029(10029,"apply friend ok!","申请好友成功!"),
	C10030(10030,"get group failed!","获取群聊列表失败!"),
	C10031(10031,"get group ok!","获取群聊列表成功!"),
	C10032(10032,"get Friend Society failed!","获取朋友圈列表失败!"),
	C10033(10033,"get Friend Society ok!","获取朋友圈列表成功!"),
	C10034(10034,"get Cash list failed!","获取提现列表失败!"),
	C10035(10035,"get Cash list ok!","获取提现列表成功!"),
	C10036(10036," get packet failed!","红包已被抢完!"),
	C10037(10037,"get settings list ok!","获取设置成功!"),
	C10038(10038,"get packet state failed!","领取红包失败!"),
	C10039(10039,"account has exist!","账户存在!"),
	C10040(10040,"register success!","注册成功!"),
	C10041(10041,"register failed!","注册失败!"),
	C10042(10042,"not group owner!","不是群主!"),
	C10043(10043,"not group member!","不是群群成员!"),
	C10044(10044,"exit group ok!","成功已经被移除出群!"),
	C10045(10045,"upload file ok!","上传文件成功!"),
	C10046(10046,"upload file failed!","上传文件失败!"),
	C10047(10047,"user info updated!","用户信息更新完成!");
	
	private int status;
	
	private String description;
	
	private String text;

	ImStatus(int status, String description, String text) {
		this.status = status;
		this.description = description;
		this.text = text;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public int getCode() {
		return this.status;
	}

	@Override
	public String getMsg() {
		return this.getDescription()+" "+this.getText();
	}
}
