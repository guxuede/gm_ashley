package com.guxuede.gm.gdx;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.guxuede.gm.gdx.component.*;
import com.guxuede.gm.gdx.system.*;

/**
 * Created by guxuede on 2017/5/30 .
 */
public class GdxGameScreen extends ScreenAdapter {

    PooledEngine engine;

    public GdxGameScreen(){
        engine = new PooledEngine();
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new CameraSystem());
        StageSystem stageSystem = new StageSystem();
        engine.addSystem(stageSystem);
        engine.addSystem(new StageRenderingSystem(300,stageSystem.getStage()));
        engine.addSystem(new MovementSystem());
        engine.addSystem(new ActorShadowRenderingSystem(200));
        createActor();
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
        processKeyEvent();
    }

    private Entity createActor() {
        Entity entity = engine.createEntity();
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        animationComponent.animationHolder = ResourceManager.getAnimationHolder("Undead");
        ActorStateComponent actorStateComponent = engine.createComponent(ActorStateComponent.class);
        ActorComponent gdxActorComponent = engine.createComponent(ActorComponent.class);
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        ActorShadowComponent actorShadowComponent = engine.createComponent(ActorShadowComponent.class);
        entity.add(animationComponent);
        entity.add(actorStateComponent);
        entity.add(gdxActorComponent);
        entity.add(textureComponent);
        entity.add(actorShadowComponent);
        engine.addEntity(entity);
        return entity;
    }


    private void processKeyEvent(){
        Application.ApplicationType appType = Gdx.app.getType();
        // should work also with Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)
        float accelX;
        if (appType == Application.ApplicationType.Android || appType == Application.ApplicationType.iOS) {
            accelX = Gdx.input.getAccelerometerX();
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)){
                accelX = 5f;
            }else if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)){
                accelX = -5f;
            }else{
                accelX = 0;
            }
        }
        float accelY;
        if (appType == Application.ApplicationType.Android || appType == Application.ApplicationType.iOS) {
            accelY = Gdx.input.getAccelerometerY();
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)){
                accelY = 5f;
            }else if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)){
                accelY = -5f;
            }else{
                accelY = 0;
            }
        }
        engine.getEntities().first().getComponent(ActorStateComponent.class).acceleration.set(accelX*10,accelY*10);
    }
}
