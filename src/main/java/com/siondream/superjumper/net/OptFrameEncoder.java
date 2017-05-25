package com.siondream.superjumper.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.CharBuffer;
import java.util.List;

/**
 * Created by guxuede on 2017/5/21 .
 */
public class OptFrameEncoder extends MessageToByteEncoder<NetOptQueen.NetOpt> {

    @Override
    protected void encode(ChannelHandlerContext ctx, NetOptQueen.NetOpt msg, ByteBuf out) throws Exception {
        out.writeInt(msg.playerId);
        out.writeLong(msg.frame==null?0:msg.frame);
        out.writeFloat(msg.x);
    }
}
