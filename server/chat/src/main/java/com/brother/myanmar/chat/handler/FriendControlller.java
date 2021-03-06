package com.brother.myanmar.chat.handler;

import com.brother.myanmar.chat.bean.Friend;
import com.brother.myanmar.chat.bean.Group;
import com.brother.myanmar.chat.bean.User;
import com.brother.myanmar.chat.dao.UserDao;
import com.brother.myanmar.chat.dao.WindowDao;
import org.jim.core.ImPacket;
import org.jim.core.ImStatus;
import org.jim.core.config.ImConfig;
import org.jim.core.http.HttpRequest;
import org.jim.core.http.HttpResponse;
import org.jim.core.message.MessageHelper;
import org.jim.core.packets.ChatBody;
import org.jim.core.packets.ChatType;
import org.jim.core.packets.Command;
import org.jim.core.packets.RespBody;
import org.jim.core.session.id.impl.UUIDSessionIdGenerator;
import org.jim.server.JimServerAPI;
import org.jim.server.config.ImServerConfig;
import org.jim.server.protocol.http.annotation.RequestPath;
import org.jim.server.util.ChatKit;
import org.jim.server.util.HttpResps;

import java.util.ArrayList;
import java.util.List;

@RequestPath(value = "/api/friend")
public class FriendControlller {

    @RequestPath(value = "/list")
    public HttpResponse list(HttpRequest request) throws Exception {
        HttpResponse resp = TokenFilter.filter(request);
        if(resp != null) return resp;
        //1:approved,2:applying,3:reject
        Integer state = request.getParams().get("state") == null ? null : Integer.parseInt((String) request.getParams().get("state")[0]);
        Friend user = new Friend();
        List<Friend> friends;
        user.setMyId(request.getUserId());
        if(state == null || state == 0){
            friends = WindowDao.findFriend(user);
        }else {
            user.setState(state);
            friends = WindowDao.findFriendByState(user);
        }
        User chatWindowReqBody = new User();
        chatWindowReqBody.setCode(ImStatus.C10027.getCode());
        chatWindowReqBody.setMsg(ImStatus.C10027.getMsg());
        chatWindowReqBody.setFriends(friends);
        return TokenFilter.crossOrigin(HttpResps.json(request, chatWindowReqBody));
    }

    @RequestPath(value = "/group")
    public HttpResponse group(HttpRequest request) throws Exception {
        HttpResponse resp = TokenFilter.filter(request);
        if(resp != null) return resp;
        Friend user = new Friend();
        List<Friend> friends;
        user.setMyId(request.getUserId());
        user.setState(0);
        friends = WindowDao.findFriendByState(user);
        List<Group> groups = new ArrayList<>(friends.size());
        for(int i=0;i<friends.size();i++){
            Friend friend = friends.get(i);
            Group group = WindowDao.findGroup(friend.getFriendId());
            group.setMembers(WindowDao.findGroupMembers(friend.getFriendId()));
            groups.add(group);
        }
        User chatWindowReqBody = new User();
        chatWindowReqBody.setCode(ImStatus.C10027.getCode());
        chatWindowReqBody.setMsg(ImStatus.C10027.getMsg());
        chatWindowReqBody.setGroups(groups);
        return TokenFilter.crossOrigin(HttpResps.json(request, chatWindowReqBody));
    }

    @RequestPath(value = "/apply")
    public HttpResponse apply(HttpRequest request) throws Exception {
        HttpResponse resp = TokenFilter.filter(request);
        if(resp != null) return resp;
        Integer applyUser = request.getParams().get("applyUser") == null ? null : Integer.parseInt((String) request.getParams().get("applyUser")[0]);
        if(applyUser == null){
            return TokenFilter.crossOrigin(HttpResps.json(request, new RespBody(ImStatus.C10028)));
        }
        Friend user = new Friend();
        user.setMyId(request.getUserId());
        user.setFriendId(applyUser);
        Friend friend = WindowDao.findOneFriend(user);
        if(friend != null) {
            return TokenFilter.crossOrigin(HttpResps.json(request, new RespBody(ImStatus.C10028)));
        }else {
            user.setMyId(applyUser);
            user.setFriendId(request.getUserId());
            friend = WindowDao.findOneFriend(user);
            if(friend != null) {
                return TokenFilter.crossOrigin(HttpResps.json(request, new RespBody(ImStatus.C10028)));
            }
            user.setState(2);
            user.setApplyTime(System.currentTimeMillis());
            User me = UserDao.findUserById(request.getUserId());
            user.setFriendNick(me.getName());
            WindowDao.insertFriend(user);

            ChatBody chatBody = ChatBody.newBuilder().from(String.valueOf(user.getFriendId()))
                    .to(String.valueOf(user.getMyId())).chatType(ChatType.CHAT_TYPE_PRIVATE.getNumber())
                    .msgType(6).content("加个朋友呗").build();
            chatBody.setId(UUIDSessionIdGenerator.instance.sessionId(null));
            ImPacket chatPacket = new ImPacket(Command.COMMAND_FRIEND_NOTIFY,new RespBody(Command.COMMAND_FRIEND_NOTIFY,chatBody).toByte());
            JimServerAPI.sendToUser(String.valueOf(applyUser), chatPacket);
        }
        return TokenFilter.crossOrigin(HttpResps.json(request, new RespBody(ImStatus.C10029)));
    }

    @RequestPath(value = "/answer")
    public HttpResponse answer(HttpRequest request) throws Exception {
        HttpResponse resp = TokenFilter.filter(request);
        if(resp != null) return resp;
        Integer applyUser = request.getParams().get("applyUser") == null ? null : Integer.parseInt((String) request.getParams().get("applyUser")[0]);
        //1:approved,3:reject
        Integer answerState = request.getParams().get("state") == null ? null : Integer.parseInt((String) request.getParams().get("state")[0]);
        if(applyUser == null || answerState ==null || (answerState != 1 && answerState !=3)){
            return TokenFilter.crossOrigin(HttpResps.json(request, new RespBody(ImStatus.C10028)));
        }
        Friend user = new Friend();
        user.setMyId(request.getUserId());
        user.setFriendId(applyUser);
        Friend friend = WindowDao.findOneFriend(user);
        if(friend == null) {
            return TokenFilter.crossOrigin(HttpResps.json(request, new RespBody(ImStatus.C10028)));
        }else {
            friend.setState(answerState);
            WindowDao.updateFriend(friend);
            if(answerState==1){
                friend.setFriendId(friend.getMyId());
                friend.setMyId(applyUser);
                User me = UserDao.findUserById(request.getUserId());
                friend.setFriendNick(me.getName());
                WindowDao.insertFriend(friend);

                ChatBody chatBody = ChatBody.newBuilder().from(String.valueOf(user.getFriendId()))
                        .to(String.valueOf(user.getMyId())).chatType(ChatType.CHAT_TYPE_PRIVATE.getNumber())
                        .msgType(6).content("我通过了你的朋友验证请求，现在我们可以开始聊天了").build();
                chatBody.setCreateTime(System.currentTimeMillis());
                chatBody.setId(UUIDSessionIdGenerator.instance.sessionId(null));

                chatBody.setChatId(String.valueOf(user.getMyId()));
                ImPacket chatPacket = new ImPacket(Command.COMMAND_CHAT_REQ,new RespBody(Command.COMMAND_CHAT_REQ,chatBody).toByte());
                JimServerAPI.sendToUser(String.valueOf(applyUser), chatPacket);

                chatBody.setChatId(String.valueOf(user.getFriendId()));
                chatPacket = new ImPacket(Command.COMMAND_CHAT_REQ,new RespBody(Command.COMMAND_CHAT_REQ,chatBody).toByte());
                JimServerAPI.sendToUser(String.valueOf(request.getUserId()), chatPacket);

                String from = String.valueOf(user.getFriendId());
                String to = String.valueOf(user.getMyId());
                String sessionId = ChatKit.sessionId(from,to);
                ImServerConfig imServerConfig = ImConfig.Global.get();
                MessageHelper messageHelper = imServerConfig.getMessageHelper();
                messageHelper.writeMessage("store", "user:"+sessionId, chatBody);
            }
        }
        return TokenFilter.crossOrigin(HttpResps.json(request, new RespBody(ImStatus.C10029)));
    }
}
