package com.siondream.superjumper.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by guxuede on 2017/5/21 .
 */
public class OptFrameEncoder extends MessageToByteEncoder<ClientNetOptLoop.NetOpt> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ClientNetOptLoop.NetOpt msg, ByteBuf out) throws Exception {
        out.writeInt(msg.playerId);
        out.writeLong(msg.frame==null?0:msg.frame);
        out.writeFloat(msg.x);
    }
}
