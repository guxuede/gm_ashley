package com.siondream.superjumper.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

/**
 * Created by guxuede on 2017/5/22 .
 */
public class TickIdleStateHandler extends IdleStateHandler {
    int state = 0;
    private final long tickIdleTimeNanos = 1000;
    private long lastTickTime;
    private java.util.concurrent.ScheduledFuture<?> tickIdleTimeout;

    public TickIdleStateHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
        super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
    }

    public TickIdleStateHandler(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
        super(readerIdleTime, writerIdleTime, allIdleTime, unit);
    }

    public TickIdleStateHandler(boolean observeOutput, long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
        super(observeOutput, readerIdleTime, writerIdleTime, allIdleTime, unit);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isActive() && ctx.channel().isRegistered()) {
            // channelActive() event has been fired already, which means this.channelActive() will
            // not be invoked. We have to initialize here instead.
            initialize(ctx);
        } else {
            // channelActive() event has not been fired yet.  this.channelActive() will be invoked
            // and initialization will occur there.
        }
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        destroy();
        super.handlerRemoved(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        // Initialize early if channel is active already.
        if (ctx.channel().isActive()) {
            initialize(ctx);
        }
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // This method will be invoked only if this handler was added
        // before channelActive() event is fired.  If a user adds this handler
        // after the channelActive() event, initialize() will be called by beforeAdd().
        initialize(ctx);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        destroy();
        super.channelInactive(ctx);
    }

    public void initialize(ChannelHandlerContext ctx) {
        // Avoid the case where destroy() is called before scheduling timeouts.
        // See: https://github.com/netty/netty/issues/143
        switch (state) {
            case 1:
            case 2:
                return;
        }

        state = 1;
        lastTickTime = System.nanoTime();
        tickIdleTimeout = schedule(ctx, new TickIdleTimeoutTask(ctx),
                1000, TimeUnit.NANOSECONDS);

    }

    public void destroy() {
        state = 2;
        if (tickIdleTimeout != null) {
            tickIdleTimeout.cancel(false);
            tickIdleTimeout = null;
        }
    }

    private abstract static class AbstractIdleTask implements Runnable {

        private final ChannelHandlerContext ctx;

        AbstractIdleTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            if (!ctx.channel().isOpen()) {
                return;
            }

            run(ctx);
        }

        protected abstract void run(ChannelHandlerContext ctx);
    }

    private final class TickIdleTimeoutTask extends AbstractIdleTask {
        TickIdleTimeoutTask(ChannelHandlerContext ctx) {
            super(ctx);
        }
        private final Integer TICK_EVENT = Integer.valueOf(9999);
        @Override
        protected void run(ChannelHandlerContext ctx) {

            long lastTickTime = TickIdleStateHandler.this.lastTickTime;
            long nextDelay = tickIdleTimeNanos - (System.nanoTime() - lastTickTime);
            if (nextDelay <= 0) {
                // Writer is idle - set a new timeout and notify the callback.
                tickIdleTimeout = schedule(ctx, this, tickIdleTimeNanos, TimeUnit.NANOSECONDS);

                try {
                    ctx.fireUserEventTriggered(TICK_EVENT);
                } catch (Throwable t) {
                    ctx.fireExceptionCaught(t);
                }
            } else {
                // Write occurred before the timeout - set a new timeout with shorter delay.
                tickIdleTimeout = schedule(ctx, this, nextDelay, TimeUnit.NANOSECONDS);
            }
        }
    }

    ScheduledFuture<?> schedule(ChannelHandlerContext ctx, Runnable task, long delay, TimeUnit unit) {
        return ctx.executor().schedule(task, delay, unit);
    }


}
