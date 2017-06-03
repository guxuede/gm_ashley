package com.guxuede.gm.gdx.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.guxuede.gm.gdx.Mappers;
import com.guxuede.gm.gdx.component.ActorStateComponent;

/**
 * Created by guxuede on 2017/5/29 .
 */
public class ActorLifeBarRenderingSystem extends IteratingSystem {

    public static final float BLOOD_WIDTH = 50;
    public static final float BLOOD_HEIGHT = 6;
    private static final Family family = Family.all(ActorStateComponent.class).get();
    SpriteBatch batch;
    private ShapeRenderer shapes;

    public ActorLifeBarRenderingSystem(int priority,SpriteBatch spriteBatch) {
        super(family);
        this.priority = priority;
        batch = spriteBatch;
        shapes = new ShapeRenderer();
        shapes.setAutoShapeType(true);
    }

    @Override
    public void update(float deltaTime) {
        shapes.setProjectionMatrix(batch.getProjectionMatrix());
        batch.begin();
        shapes.begin();
        super.update(deltaTime);
        shapes.end();
        batch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ActorStateComponent actorStateComponent = Mappers.actorStateCM.get(entity);

        //if(!animationActor.isInScreen)continue;
        Vector2 p = actorStateComponent.position.cpy();
        p.add(-BLOOD_WIDTH / 2, 0);
        shapes.set(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Color.GRAY);
        shapes.rect(p.x, p.y, BLOOD_WIDTH, BLOOD_HEIGHT);
        shapes.set(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.BLACK);
        shapes.rect(p.x, p.y, BLOOD_WIDTH, BLOOD_HEIGHT);

        shapes.set(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Color.GREEN);
        shapes.rect(p.x + 1, p.y + 1, BLOOD_WIDTH * (actorStateComponent.currentHitPoint / actorStateComponent.hitPoint) - 1, 4f);
    }

}
