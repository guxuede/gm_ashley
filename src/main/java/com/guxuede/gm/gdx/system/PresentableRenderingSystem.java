package com.guxuede.gm.gdx.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.guxuede.gm.gdx.Mappers;
import com.guxuede.gm.gdx.component.PresentableComponent;

import java.util.Comparator;

/**
 * Created by guxuede on 2017/5/29 .
 */
public class PresentableRenderingSystem extends IteratingSystem {

    private static final Family family = Family.all(PresentableComponent.class).get();
    SpriteBatch batch;
    private Array<PresentableComponent> renderQueue;
    private static final Comparator<PresentableComponent> comparator = new Comparator<PresentableComponent>() {
        @Override
        public int compare(PresentableComponent entityA, PresentableComponent entityB) {
            return (int) Math.signum(entityA.zIndex - entityB.zIndex);
        }
    };

    public PresentableRenderingSystem(int priority, SpriteBatch spriteBatch) {
        super(family);
        this.priority = priority;
        batch = spriteBatch;
        renderQueue = new Array<PresentableComponent>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        renderQueue.sort(comparator);
        //stage.draw();
        batch.begin();
        for (PresentableComponent entity : renderQueue) {
            renderEntity(entity, deltaTime);
        }
        batch.end();
        renderQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(Mappers.presentableCM.get(entity));
    }

    protected void renderEntity(PresentableComponent presentableComponent, float deltaTime) {
        TextureRegion textureRegion = presentableComponent.region;
        if (textureRegion != null) {
            batch.draw(textureRegion, presentableComponent.renderPosition.x, presentableComponent.renderPosition.y);
        }
    }
}
