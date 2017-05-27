package com.siondream.superjumper.net;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by guxuede on 2017/5/20 .
 */
public class ClientNetOptLoop {

    private static final ConcurrentLinkedQueue<NetOpt> incomeFrameOptList = new ConcurrentLinkedQueue();
    public static final ConcurrentLinkedQueue<NetOpt> outcomeFrameOptList =new ConcurrentLinkedQueue();

    private static final List<NetOpt> returnNetOpt = new ArrayList<NetOpt>(5);

    public static void addIncomeFrameOpt(NetOpt opt){
        incomeFrameOptList.add(opt);
    }

    public static List<NetOpt> readCurrentFrameOpt(long frame){
        returnNetOpt.clear();
        boolean allPlayerOkInCurrentFrame = true;
        for(int p: ServerNetOptLoop.players){
            if(!checkHasPlayersOpt(p)){
                allPlayerOkInCurrentFrame = false;
                break;
            }
        }
        if(allPlayerOkInCurrentFrame){
            synchronized (incomeFrameOptList){
                returnNetOpt.addAll(incomeFrameOptList);
                incomeFrameOptList.clear();
            }
        }
        return returnNetOpt;
    }

    public static boolean checkHasPlayersOpt(int player){
        for(ClientNetOptLoop.NetOpt opt:incomeFrameOptList){
            if(opt.playerId == player){
                return true;
            }
        }
        return false;
    }

    public static void addOpt(int playId, long frame, float x){
        NetOpt opt = new NetOpt();
        opt.playerId = playId;
        opt.frame = frame;
        opt.x = x;
        synchronized (ClientNetOptLoop.outcomeFrameOptList){
            outcomeFrameOptList.add(opt);
        }
    }

    public static class NetOpt{
        public int playerId;
        public Long frame;
        public float x;

        @Override
        public String toString() {
            return "NetOpt{" +
                    "playerId=" + playerId +
                    ", frame=" + frame +
                    ", x=" + x +
                    '}';
        }
    }
}
