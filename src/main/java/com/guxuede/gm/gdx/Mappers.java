package com.guxuede.gm.gdx;

import com.badlogic.ashley.core.ComponentMapper;
import com.guxuede.gm.gdx.component.*;

/**
 * Created by guxuede on 2017/5/31 .
 */
public class Mappers {
    public static final ComponentMapper<ActorStateComponent> actorStateCM = ComponentMapper.getFor(ActorStateComponent.class);
    public static final ComponentMapper<AnimationComponent> animationCM = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<CameraComponent> cameraCM = ComponentMapper.getFor(CameraComponent.class);
    public static final ComponentMapper<ActorComponent> actorCM = ComponentMapper.getFor(ActorComponent.class);
    public static final ComponentMapper<PresentableComponent> presentableCM = ComponentMapper.getFor(PresentableComponent.class);
    public static final ComponentMapper<ActorAnimationComponent> animationHolderCM = ComponentMapper.getFor(ActorAnimationComponent.class);
    public static final ComponentMapper<ActionsComponent> actionCM = ComponentMapper.getFor(ActionsComponent.class);

}
