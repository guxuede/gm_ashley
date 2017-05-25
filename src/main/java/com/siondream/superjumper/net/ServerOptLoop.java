package com.siondream.superjumper.net;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by guxuede on 2017/5/23 .
 */
public class ServerOptLoop implements Runnable {

    private boolean isFirst = true;
    private static AtomicLong currentFrame = new AtomicLong(0);
    private static List<NetOptQueen.NetOpt> currentFrameOpt = new ArrayList<NetOptQueen.NetOpt>(10);
    private int[] players = new int[]{0,1};

    @Override
    public void run() {
        System.out.println("run11.");
        boolean allPlayerOkInCurrentFrame = true;
        if(isFirst){
            if(!SecureChatServerHandler.channels.isEmpty() && SecureChatServerHandler.channels.size() == players.length){
                NetOptQueen.NetOpt startOpt = new NetOptQueen.NetOpt();
                startOpt.frame = 0L;
                startOpt.playerId = -1;
                SecureChatServerHandler.channels.writeAndFlush(startOpt);
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
                System.out.println("wait for player:"+p);
                allPlayerOkInCurrentFrame = false;
            }
        }
        if(allPlayerOkInCurrentFrame){
            for(NetOptQueen.NetOpt opt:currentFrameOpt){
                SecureChatServerHandler.channels.writeAndFlush(opt);
            }
            currentFrame.incrementAndGet();
            currentFrameOpt.clear();
        }
    }

    public static boolean addClientOpt(NetOptQueen.NetOpt opt){
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
        for(NetOptQueen.NetOpt opt:currentFrameOpt){
            if(opt.playerId == player){
                return true;
            }
        }
        return false;
    }


}
