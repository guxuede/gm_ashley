/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.siondream.superjumper.net;

import com.siondream.superjumper.GameScreen;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 */
public final class SecureChatClient extends Thread {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8992"));
    private EventLoopGroup group;
    private boolean goRun;
    private GameScreen gameScreen;

    public SecureChatClient(GameScreen gameScreen){
        goRun = true;
        this.gameScreen = gameScreen;
    }

    @Override
    public void run() {
        group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new SecureChatClientInitializer());

            // Start the connection attempt.
            Channel ch = b.connect(HOST, PORT).sync().channel();
            while (goRun && gameScreen.state!=-1) {
                synchronized (NetOptQueen.outcomeFrameOptList) {
                    while (!NetOptQueen.outcomeFrameOptList.isEmpty()) {
                        NetOptQueen.NetOpt opt = NetOptQueen.outcomeFrameOptList.poll();
                        ch.writeAndFlush(opt);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("disconnect link.");
            group.shutdownGracefully();
        }
    }
}
