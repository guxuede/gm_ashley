package com.siondream.superjumper.net;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by guxuede on 2017/5/23 .
 */
public class ServerNetOptLoop implements Runnable {

    private boolean isFirst = true;
    private static AtomicLong currentFrame = new AtomicLong(0);
    private static List<ClientNetOptLoop.NetOpt> currentFrameOpt = new ArrayList<ClientNetOptLoop.NetOpt>(10);
    public static final int[] players = new int[]{0,1};

    private boolean isWait = false;

    @Override
    public void run() {
        boolean allPlayerOkInCurrentFrame = true;
        if(isFirst){
            if(!SecureChatServerHandler.channels.isEmpty() && SecureChatServerHandler.channels.size() == players.length){
                for(int p:players){
                    ClientNetOptLoop.NetOpt startOpt = new ClientNetOptLoop.NetOpt();
                    startOpt.frame = 0L;
                    startOpt.x = 0;
                    startOpt.playerId = p;
                    SecureChatServerHandler.channels.writeAndFlush(startOpt);
                }
                currentFrame.incrementAndGet();
                isFirst = false;
                return;
            }else{
                System.out.println("等等所有玩家就位");
                return;
            }
        }
        for(int p:players){
            if(!checkHasPlayersOpt(p)){
                if(!isWait){
                    System.out.println("wait for player:"+p);
                    isWait = true;
                }
                allPlayerOkInCurrentFrame = false;
            }
        }
        if(allPlayerOkInCurrentFrame){
            isWait = false;
            for(ClientNetOptLoop.NetOpt opt:currentFrameOpt){
                SecureChatServerHandler.channels.writeAndFlush(opt);
            }
            currentFrame.incrementAndGet();
            currentFrameOpt.clear();
        }
    }

    public static boolean addClientOpt(ClientNetOptLoop.NetOpt opt){
        if(opt.frame == currentFrame.get()){
            if(!checkHasPlayersOpt(opt.playerId)){
                currentFrameOpt.add(opt);
            }else{
                System.err.println("每一帧只允许一个操作");
            }
        }else{
            System.err.println("客户端p"+opt.playerId+"帧数("+opt.frame+")和服务端帧数("+currentFrame.get()+")不一致，不允许增加.");
        }
        return false;
    }

    public static boolean checkHasPlayersOpt(int player){
        for(ClientNetOptLoop.NetOpt opt:currentFrameOpt){
            if(opt.playerId == player){
                return true;
            }
        }
        return false;
    }


}
