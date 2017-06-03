package com.guxuede.gm.gdx.actions.movement;

import com.guxuede.gm.gdx.Mappers;
import com.guxuede.gm.gdx.actions.RelativeTemporalAction;
import com.guxuede.gm.gdx.component.ActorStateComponent;

/**
 * Created by guxuede on 2017/6/3 .
 */
public class MoveAction extends RelativeTemporalAction {

    public MoveAction(float duration){
        this.setDuration(duration);
    }

    @Override
    protected void updateRelative(float percentDelta) {
        ActorStateComponent actorStateComponent = Mappers.actorStateCM.get(actor);
        actorStateComponent.acceleration.set(5,5);
    }

    @Override
    protected void end() {
        super.end();
        ActorStateComponent actorStateComponent = Mappers.actorStateCM.get(actor);
        actorStateComponent.acceleration.set(0,0);
    }
}
