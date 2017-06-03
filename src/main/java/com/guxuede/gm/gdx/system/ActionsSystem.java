package com.guxuede.gm.gdx.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.guxuede.gm.gdx.Mappers;
import com.guxuede.gm.gdx.actions.Action;
import com.guxuede.gm.gdx.component.ActionsComponent;

/**
 * Created by guxuede on 2017/6/3 .
 */
public class ActionsSystem extends IteratingSystem {

    private static final Family family = Family.all(ActionsComponent.class).get();

    public ActionsSystem(){
        super(family);
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ActionsComponent actionsComponent = Mappers.actionCM.get(entity);
        act(actionsComponent.actions,deltaTime);
    }

    public void act (Array<Action> actions,float delta) {
        if (actions.size > 0) {
            for (int i = 0; i < actions.size; i++) {
                Action action = actions.get(i);
                if (action.act(delta) && i < actions.size) {
                    Action current = actions.get(i);
                    int actionIndex = current == action ? i : actions.indexOf(action, true);
                    if (actionIndex != -1) {
                        actions.removeIndex(actionIndex);
                        action.setActor(null);
                        i--;
                    }
                }
            }
        }
    }
}
