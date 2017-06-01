package com.guxuede.gm.gdx.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.guxuede.gm.gdx.component.ActorComponent;

/**
 * Created by guxuede on 2017/5/29 .
 */
public class StageRenderingSystem extends IteratingSystem {

    private static final Family family = Family.all(ActorComponent.class).get();

    private Stage stage;

    public StageRenderingSystem(int priority , Stage stage) {
        super(family);
        this.stage = stage;
        this.priority = priority;
    }

    @Override
    public void update(float deltaTime) {
        stage.draw();
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
