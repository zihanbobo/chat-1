package com.brother.myanmar.chat.dao;

import com.brother.myanmar.chat.SqlConnection;
import com.brother.myanmar.chat.bean.ChatWindow;
import com.brother.myanmar.chat.bean.Friend;
import com.brother.myanmar.chat.bean.Group;
import com.brother.myanmar.chat.mapper.ChatWindow2Mapper;
import com.brother.myanmar.chat.mapper.Friend2Mapper;
import com.brother.myanmar.chat.mapper.Group2Mapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class WindowDao {

    SqlSession session = SqlConnection.getSession();
    public int insertWindow(ChatWindow window){
        ChatWindow2Mapper mapper = session.getMapper(ChatWindow2Mapper.class);
        int rint = mapper.insertWindow(window);
        session.commit();
        return rint;
    }
    public int updateWindow(ChatWindow window){
        ChatWindow2Mapper mapper = session.getMapper(ChatWindow2Mapper.class);
        int rint = mapper.updateWindow(window);
        session.commit();
        return rint;
    }
    public ChatWindow findWindow(ChatWindow window){
        ChatWindow2Mapper mapper = session.getMapper(ChatWindow2Mapper.class);
        ChatWindow result = mapper.findWindow(window);
        session.commit();
        return result;
    }
    public List<ChatWindow> getWindowList(ChatWindow window){
        ChatWindow2Mapper mapper = session.getMapper(ChatWindow2Mapper.class);
        List<ChatWindow> result = mapper.getWindowList(window);
        session.commit();
        return result;
    }

    public List<Friend> findFriend(Friend user){
        Friend2Mapper mapper = session.getMapper(Friend2Mapper.class);
        List<Friend> result = mapper.findFriend(user);
        session.commit();
        return result;
    }
    public List<Friend> findFriendByState(Friend user){
        Friend2Mapper mapper = session.getMapper(Friend2Mapper.class);
        List<Friend> result = mapper.findFriendByState(user);
        session.commit();
        return result;
    }
    public Group findGroup(int id){
        Group2Mapper mapper = session.getMapper(Group2Mapper.class);
        Group result = mapper.findGroup(id);
        session.commit();
        return result;
    }

    public List<Friend> findGroupMembers(int id){
        Friend2Mapper mapper = session.getMapper(Friend2Mapper.class);
        List<Friend> result = mapper.findGroupMembers(id);
        session.commit();
        return result;
    }

    public Friend findOneFriend(Friend user){
        Friend2Mapper mapper = session.getMapper(Friend2Mapper.class);
        Friend result = mapper.findOneFriend(user);
        session.commit();
        return result;
    }
    public int insertFriend(Friend user){
        Friend2Mapper mapper = session.getMapper(Friend2Mapper.class);
        int rint = mapper.insertFriend(user);
        session.commit();
        return rint;
    }
    public int updateFriend(Friend user){
        Friend2Mapper mapper = session.getMapper(Friend2Mapper.class);
        int rint = mapper.updateFriend(user);
        session.commit();
        return rint;
    }

}
