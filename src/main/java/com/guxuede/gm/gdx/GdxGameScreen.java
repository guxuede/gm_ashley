package com.guxuede.gm.gdx;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.guxuede.gm.gdx.actions.DelayAction;
import com.guxuede.gm.gdx.actions.SequenceAction;
import com.guxuede.gm.gdx.actions.movement.BlinkAction;
import com.guxuede.gm.gdx.actions.movement.MoveAction;
import com.guxuede.gm.gdx.component.*;
import com.guxuede.gm.gdx.system.*;
import com.guxuede.gm.gdx.system.CameraSystem;
import com.guxuede.gm.gdx.system.MovementSystem;

/**
 * Created by guxuede on 2017/5/30 .
 */
public class GdxGameScreen extends ScreenAdapter {

    SpriteBatch spriteBatch;
    PooledEngine engine;

    public GdxGameScreen(){
        spriteBatch = new SpriteBatch();
        engine = new PooledEngine();
        engine.addSystem(new ActorAnimationSystem());
        engine.addSystem(new ActorStateActorAnimationSystem());
        engine.addSystem(new ActionsSystem());
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new ActorStateSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem( new StageSystem());
        engine.addSystem(new CameraSystem(spriteBatch));
        engine.addSystem(new PresentableRenderingSystem(300,spriteBatch));
        engine.addSystem(new ActorShadowRenderingSystem(200,spriteBatch));
        engine.addSystem(new ActorLifeBarRenderingSystem(400,spriteBatch));
        createActor();
        createPresentableComponentEntity();
        createPresentableComponentAnimationComponentEntity();
        createPresentableComponentAnimationComponentActorAnimationComponentEntity();
        createPresentableComponentAnimationComponentActorAnimationComponentActorStateComponentEntity();
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
        processKeyEvent();
    }

    //测试只有一个PresentableComponent 时可以画出静态图片
    private Entity createPresentableComponentEntity() {
        Entity entity = engine.createEntity();
        PresentableComponent presentableComponent = engine.createComponent(PresentableComponent.class);
        presentableComponent.renderPosition.set(100,100);
        presentableComponent.region = ResourceManager.getTextureRegion("Aquatic");
        presentableComponent.zIndex = -1;

        entity.add(presentableComponent);
        engine.addEntity(entity);
        return entity;
    }
    //测试只有一个PresentableComponent+一个AnimationComponent时可以画出动态动画
    private Entity createPresentableComponentAnimationComponentEntity() {
        Entity entity = engine.createEntity();
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        animationComponent.animation = ResourceManager.getAnimationHolder("Undead").getAnimation(AnimationHolder.WALK_DOWN_ANIMATION);
        animationComponent.animationPosition.set(20,20);
        PresentableComponent presentableComponent = engine.createComponent(PresentableComponent.class);

        entity.add(animationComponent);
        entity.add(presentableComponent);
        engine.addEntity(entity);
        return entity;
    }
    //测试只有一个PresentableComponent+一个AnimationComponent+一个ActorAnimationComponent时可以画出角色状态动态动画
    private Entity createPresentableComponentAnimationComponentActorAnimationComponentEntity() {
        Entity entity = engine.createEntity();
        ActorAnimationComponent actorAnimationComponent = engine.createComponent(ActorAnimationComponent.class);
        actorAnimationComponent.animationHolder = ResourceManager.getAnimationHolder("Undead");
        actorAnimationComponent.isMoving = true;
        actorAnimationComponent.direction = ActorAnimationComponent.LEFT;
        actorAnimationComponent.animationPosition.set(30,30);

        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        PresentableComponent presentableComponent = engine.createComponent(PresentableComponent.class);

        entity.add(actorAnimationComponent);
        entity.add(animationComponent);
        entity.add(presentableComponent);
        engine.addEntity(entity);
        return entity;
    }

    //测试只有一个PresentableComponent+一个AnimationComponent+一个ActorAnimationComponent+一个ActorStateComponent时可以画出以一定速度移动地角色状态动态动画
    private Entity createPresentableComponentAnimationComponentActorAnimationComponentActorStateComponentEntity() {
        Entity entity = engine.createEntity();
        ActorStateComponent actorStateComponent = engine.createComponent(ActorStateComponent.class);
        actorStateComponent.isMoving = true;
        actorStateComponent.direction = ActorAnimationComponent.RIGHT;
        actorStateComponent.position.set(50,50);
        actorStateComponent.velocity.set(5,5);
        actorStateComponent.acceleration.set(10,10);
        ActorAnimationComponent actorAnimationComponent = engine.createComponent(ActorAnimationComponent.class);
        actorAnimationComponent.animationHolder = ResourceManager.getAnimationHolder("Undead");

        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        PresentableComponent presentableComponent = engine.createComponent(PresentableComponent.class);

        ActionsComponent actionsComponent = engine.createComponent(ActionsComponent.class);
        actionsComponent.addAction(entity, new SequenceAction(new DelayAction(2),new BlinkAction()));
        entity.add(actionsComponent);

        entity.add(actorStateComponent);
        entity.add(actorAnimationComponent);
        entity.add(animationComponent);
        entity.add(presentableComponent);
        engine.addEntity(entity);
        return entity;
    }

    private Entity createActor() {
        Entity entity = engine.createEntity();
        ActorAnimationComponent animationHolder = engine.createComponent(ActorAnimationComponent.class);
        animationHolder.animationHolder = ResourceManager.getAnimationHolder("Undead");
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        PresentableComponent presentableComponent = engine.createComponent(PresentableComponent.class);

        ActorStateComponent actorStateComponent = engine.createComponent(ActorStateComponent.class);
        ActorComponent gdxActorComponent = engine.createComponent(ActorComponent.class);
        ActorShadowComponent actorShadowComponent = engine.createComponent(ActorShadowComponent.class);

        CameraComponent cameraComponent = engine.createComponent(CameraComponent.class);
        cameraComponent.camera = new MovableOrthographicCamera();
        cameraComponent.target = entity;
        entity.add(cameraComponent);

        entity.add(animationHolder);
        entity.add(animationComponent);
        entity.add(actorStateComponent);
        entity.add(gdxActorComponent);
        entity.add(presentableComponent);
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
        Entity entity = engine.getEntities().first();
        if(entity!=null){
            ActorStateComponent actorStateComponent = entity.getComponent(ActorStateComponent.class);
            if(actorStateComponent!=null)
            actorStateComponent.acceleration.set(accelX*10,accelY*10);
        }
    }
}
