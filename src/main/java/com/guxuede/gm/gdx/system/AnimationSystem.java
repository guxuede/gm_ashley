package com.guxuede.gm.gdx.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.guxuede.gm.gdx.AnimationHolder;
import com.guxuede.gm.gdx.Mappers;
import com.guxuede.gm.gdx.component.ActorStateComponent;
import com.guxuede.gm.gdx.component.AnimationComponent;
import com.guxuede.gm.gdx.component.TextureComponent;

/**
 * Created by guxuede on 2017/5/29 .
 */
public class AnimationSystem extends IteratingSystem {

    private static final Family family = Family.all(AnimationComponent.class,ActorStateComponent.class,TextureComponent.class).get();

    public AnimationSystem(){
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ActorStateComponent stateComponent = Mappers.actorStateCM.get(entity);
       if(stateComponent.isMoving){
           switch (stateComponent.direction) {
               case ActorStateComponent.UP:
                   doAnimation(entity,deltaTime,AnimationHolder.WALK_UP_ANIMATION);
                   break;
               case ActorStateComponent.DOWN:
                   doAnimation(entity,deltaTime,AnimationHolder.WALK_DOWN_ANIMATION);
                   break;
               case ActorStateComponent.RIGHT:
                   doAnimation(entity,deltaTime,AnimationHolder.WALK_RIGHT_ANIMATION);
                   break;
               case ActorStateComponent.LEFT:
                   doAnimation(entity,deltaTime,AnimationHolder.WALK_LEFT_ANIMATION);
                   break;
               default:
                   doAnimation(entity,deltaTime,AnimationHolder.WALK_DOWN_ANIMATION);
                   break;
           }
       }else{
           switch (stateComponent.direction) {
               case ActorStateComponent.UP:
                   doAnimation(entity,deltaTime,AnimationHolder.STOP_UP_ANIMATION);
                   break;
               case ActorStateComponent.DOWN:
                   doAnimation(entity,deltaTime,AnimationHolder.STOP_DOWN_ANIMATION);
                   break;
               case ActorStateComponent.RIGHT:
                   doAnimation(entity,deltaTime,AnimationHolder.STOP_RIGHT_ANIMATION);
                   break;
               case ActorStateComponent.LEFT:
                   doAnimation(entity,deltaTime,AnimationHolder.STOP_LEFT_ANIMATION);
                   break;
               default:
                   doAnimation(entity,deltaTime,AnimationHolder.STOP_DOWN_ANIMATION);
                   break;
           }
       }
    }

    public void doAnimation(Entity entity, float deltaTime,int animationName){
        AnimationComponent animationComponent = Mappers.animationCM.get(entity);
        animationComponent.stateTime +=deltaTime;
        Animation animation = animationComponent.animationHolder.getAnimation(animationName);
        TextureRegion textureRegion = animation.getKeyFrame(animationComponent.stateTime,true);
        entity.getComponent(TextureComponent.class).region = textureRegion;
    }
}
