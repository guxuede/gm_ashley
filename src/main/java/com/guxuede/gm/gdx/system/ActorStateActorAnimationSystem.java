package com.guxuede.gm.gdx.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.guxuede.gm.gdx.Mappers;
import com.guxuede.gm.gdx.component.ActorStateComponent;
import com.guxuede.gm.gdx.component.ActorAnimationComponent;

/**
 * Created by guxuede on 2017/5/29 .
 */
public class ActorStateActorAnimationSystem extends IteratingSystem {

    private static final Family family = Family.all(ActorStateComponent.class,ActorAnimationComponent.class).get();

    public ActorStateActorAnimationSystem(){
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ActorStateComponent actorStateComponent = Mappers.actorStateCM.get(entity);
        ActorAnimationComponent actorAnimationComponent = Mappers.animationHolderCM.get(entity);
        actorAnimationComponent.animationPosition.set(actorStateComponent.position);
        actorAnimationComponent.direction = actorStateComponent.direction;
        actorAnimationComponent.isMoving = actorStateComponent.isMoving;
    }

}
