package com.siondream.superjumper.net;

import com.badlogic.gdx.Net;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by guxuede on 2017/5/20 .
 */
public class NetOptQueen {

    private static final List<NetOpt> incomeFrameOptList = Collections.synchronizedList(new LinkedList<NetOpt>());
    public static final ConcurrentLinkedQueue<NetOpt> outcomeFrameOptList =new ConcurrentLinkedQueue();

    public static void addIncomeFrameOpt(NetOpt opt){
        incomeFrameOptList.add(opt);
    }

    private static List<NetOpt> getBeforeFrameOpt(long frame){
        List<NetOpt> opts = new ArrayList<NetOpt>();
        for(NetOpt opt : incomeFrameOptList){
            if(opt.frame >= frame){
                opts.add(opt);
            }
        }
        return opts;
    }

    public static Map<Long,List<NetOpt>> readAllAfterFrameOpt(long frame){
        List<NetOpt> opts = getBeforeFrameOpt(frame);
        incomeFrameOptList.removeAll(opts);
        Collections.sort(opts, new Comparator<NetOpt>() {
            @Override
            public int compare(NetOpt o1, NetOpt o2) {
                return o1.frame.compareTo(o2.frame);
            }
        });
        LinkedHashMap<Long,List<NetOpt>> optsMap = new LinkedHashMap<Long, List<NetOpt>>();
        for(NetOpt opt : opts){
            List<NetOpt> sdf = optsMap.get(opt.frame);
            if(sdf==null){
                sdf = new ArrayList<NetOpt>();
                optsMap.put(opt.frame,sdf);
            }
            sdf.add(opt);
        }
        return optsMap;
    }

    public static void addOpt(int playId, long frame, float x){
        NetOpt opt = new NetOpt();
        opt.playerId = playId;
        opt.frame = frame;
        opt.x = x;
        synchronized (NetOptQueen.outcomeFrameOptList){
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
