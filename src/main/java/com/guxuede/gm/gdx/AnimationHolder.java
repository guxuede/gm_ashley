package com.guxuede.gm.gdx;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guxuede on 2016/9/16 .
 */
public class AnimationHolder {

    public static final int WALK_DOWN_ANIMATION = "walkDownAnimation".hashCode();
    public static final int WALK_LEFT_ANIMATION = "walkLeftAnimation".hashCode();
    public static final int WALK_RIGHT_ANIMATION = "walkRightAnimation".hashCode();
    public static final int WALK_UP_ANIMATION = "walkUpAnimation".hashCode();
    public static final int STOP_DOWN_ANIMATION = "stopDownAnimation".hashCode();
    public static final int STOP_LEFT_ANIMATION = "stopLeftAnimation".hashCode();
    public static final int STOP_RIGHT_ANIMATION = "stopRightAnimation".hashCode();
    public static final int STOP_UP_ANIMATION = "stopUpAnimation".hashCode();
    public static final int DEATH_ANIMATION = "deathAnimation".hashCode();
    public static final int ATTACK_ANIMATION = "attackAnimation".hashCode();

    /*********************************************/
    private IntMap<Animation> animationMap = new IntMap<Animation>();
    public int width,height;
    public String name;
    /*********************************************/


    public AnimationHolder(){

    }

    public AnimationHolder(Animation allInOneAnimation){
        animationMap.put(WALK_DOWN_ANIMATION,allInOneAnimation);
        animationMap.put(WALK_LEFT_ANIMATION,allInOneAnimation);
        animationMap.put(WALK_RIGHT_ANIMATION,allInOneAnimation);
        animationMap.put(WALK_UP_ANIMATION,allInOneAnimation);
        animationMap.put(STOP_DOWN_ANIMATION,allInOneAnimation);
        animationMap.put(STOP_LEFT_ANIMATION,allInOneAnimation);
        animationMap.put(STOP_RIGHT_ANIMATION,allInOneAnimation);
        animationMap.put(STOP_UP_ANIMATION,allInOneAnimation);
        animationMap.put(DEATH_ANIMATION,allInOneAnimation);
    }


    public AnimationHolder(Animation allInOneAnimation, Animation deathAnimation){
        animationMap.put(WALK_DOWN_ANIMATION,allInOneAnimation);
        animationMap.put(WALK_LEFT_ANIMATION,allInOneAnimation);
        animationMap.put(WALK_RIGHT_ANIMATION,allInOneAnimation);
        animationMap.put(WALK_UP_ANIMATION,allInOneAnimation);
        animationMap.put(STOP_DOWN_ANIMATION,allInOneAnimation);
        animationMap.put(STOP_LEFT_ANIMATION,allInOneAnimation);
        animationMap.put(STOP_RIGHT_ANIMATION,allInOneAnimation);
        animationMap.put(STOP_UP_ANIMATION,allInOneAnimation);
        animationMap.put(DEATH_ANIMATION,deathAnimation);
    }

    public AnimationHolder(Animation walkDownAnimation, Animation walkLeftAnimation, Animation walkRightAnimation, Animation walkUpAnimation, Animation stopDownAnimation, Animation stopLeftAnimation, Animation stopRightAnimation, Animation stopUpAnimation, Animation deathAnimation) {
        animationMap.put(WALK_DOWN_ANIMATION,walkDownAnimation);
        animationMap.put(WALK_LEFT_ANIMATION,walkLeftAnimation);
        animationMap.put(WALK_RIGHT_ANIMATION,walkRightAnimation);
        animationMap.put(WALK_UP_ANIMATION,walkUpAnimation);
        animationMap.put(STOP_DOWN_ANIMATION,stopDownAnimation);
        animationMap.put(STOP_LEFT_ANIMATION,stopLeftAnimation);
        animationMap.put(STOP_RIGHT_ANIMATION,stopRightAnimation);
        animationMap.put(STOP_UP_ANIMATION,stopUpAnimation);
        animationMap.put(DEATH_ANIMATION,deathAnimation);
    }

    public void addAnimation(int name, Animation animation){
        this.animationMap.put(name,animation);
    }

    public Animation getWalkDownAnimation() {
        return getAnimation(WALK_DOWN_ANIMATION);
    }

    public Animation getWalkLeftAnimation() {
        return getAnimation(WALK_LEFT_ANIMATION);
    }

    public Animation getWalkRightAnimation() {
        return getAnimation(WALK_RIGHT_ANIMATION);
    }

    public Animation getWalkUpAnimation() {
        return getAnimation(WALK_UP_ANIMATION);
    }

    public Animation getStopDownAnimation() {
        return getAnimation(STOP_DOWN_ANIMATION);
    }


    public Animation getStopLeftAnimation() {
        return getAnimation(STOP_LEFT_ANIMATION);
    }

    public Animation getStopRightAnimation() {
        return getAnimation(STOP_RIGHT_ANIMATION);
    }


    public Animation getStopUpAnimation() {
        return getAnimation(STOP_UP_ANIMATION);
    }

    public Animation getDeathAnimation() {
        return getAnimation(DEATH_ANIMATION);
    }


    public Animation getAnimation(int name){
        return animationMap.get(name);
    }

    public AnimationHolder getCopy(){
        AnimationHolder animationHolder = new AnimationHolder();
        animationHolder.name = this.name;
        animationHolder.width = this.width;
        animationHolder.height = this.height;
        animationHolder.animationMap = new IntMap<Animation>(this.animationMap);
        return animationHolder;
    }

}
