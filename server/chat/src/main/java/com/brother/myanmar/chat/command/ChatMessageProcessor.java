package com.brother.myanmar.chat.command;

import com.brother.myanmar.chat.bean.Packet;
import com.brother.myanmar.chat.dao.PacketDao;
import org.jim.core.ImChannelContext;
import org.jim.core.packets.ChatBody;
import org.jim.core.packets.ChatType;
import org.jim.server.processor.chat.BaseAsyncChatMessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatMessageProcessor extends BaseAsyncChatMessageProcessor {

    private static Logger logger = LoggerFactory.getLogger(ChatMessageProcessor.class);

    private PacketDao packetDao = new PacketDao();

    @Override
    public void doProcess(ChatBody chatBody, ImChannelContext imChannelContext){
        //红包处理
        if(chatBody.getMsgType() == 2){
            Packet packet = new Packet();
            if(ChatType.CHAT_TYPE_PRIVATE.getNumber() == chatBody.getChatType()){
                packet.setNum(1);
                packet.setType(0);
            }else{
                packet.setNum(chatBody.getPacketNum());
                packet.setType(chatBody.getSystemCmd());
            }
            packet.setId(chatBody.getId());
            packet.setState(0);
            packet.setAmount(chatBody.getPacketAmount());
            packet.setSender(Integer.parseInt(imChannelContext.getUserId()));
            packet.setTime(System.currentTimeMillis());
            packetDao.insertPacket(packet);
        }
    }
}
