package com.guxuede.gm.gdx.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.guxuede.gm.gdx.Mappers;
import com.guxuede.gm.gdx.component.AnimationComponent;
import com.guxuede.gm.gdx.component.PresentableComponent;

/**
 * Created by guxuede on 2017/5/29 .
 */
public class AnimationSystem extends IteratingSystem {

    private static final Family family = Family.all(AnimationComponent.class,PresentableComponent.class).get();

    public AnimationSystem(){
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent animationComponent = Mappers.animationCM.get(entity);
        animationComponent.stateTime += deltaTime;
        PresentableComponent presentableComponent = Mappers.presentableCM.get(entity);
        presentableComponent.region = animationComponent.animation.getKeyFrame(animationComponent.stateTime,true);
        presentableComponent.renderPosition.set(animationComponent.animationPosition);
        presentableComponent.zIndex = animationComponent.animationPosition.y;
    }
}
