package com.guxuede.gm.gdx.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.guxuede.gm.gdx.Mappers;
import com.guxuede.gm.gdx.ResourceManager;
import com.guxuede.gm.gdx.component.ActorStateComponent;

/**
 * Created by guxuede on 2017/5/31 .
 */
public class ActorShadowRenderingSystem extends IteratingSystem {

    private static final Family family = Family.all(ActorStateComponent.class).get();

    SpriteBatch spriteBatch;

    public ActorShadowRenderingSystem(int priority,SpriteBatch spriteBatch){
        super(family);
        this.spriteBatch = spriteBatch;
        this.priority = priority;
    }

    @Override
    public void update(float deltaTime) {
        spriteBatch.begin();
        super.update(deltaTime);
        spriteBatch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ActorStateComponent actorComponent = Mappers.actorStateCM.get(entity);
        spriteBatch.draw(ResourceManager.shadow,actorComponent.position.x,actorComponent.position.y);
    }
}
