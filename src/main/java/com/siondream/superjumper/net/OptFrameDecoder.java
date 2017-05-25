package com.siondream.superjumper.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;

import java.util.List;

/**
 * Created by guxuede on 2017/5/21 .
 */
public class OptFrameDecoder extends ByteToMessageDecoder {

    private int frameLength = 16;

    @Override
    protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decoded = decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }

    protected Object decode(
            @SuppressWarnings("UnusedParameters") ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in.readableBytes() < frameLength) {
            return null;
        } else {
            int playId = in.readInt();
            long frame = in.readLong();
            float x = in.readFloat();
            NetOptQueen.NetOpt opt = new NetOptQueen.NetOpt();
            opt.playerId = playId;
            opt.frame = frame;
            opt.x = x;
            return opt;
        }
    }
}
