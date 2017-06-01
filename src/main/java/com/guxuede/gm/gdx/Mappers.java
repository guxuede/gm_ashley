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
    public static final ComponentMapper<TextureComponent> textureCM = ComponentMapper.getFor(TextureComponent.class);

}
