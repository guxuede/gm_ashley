package com.guxuede.gm.gdx.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.guxuede.gm.gdx.actions.Action;

/**
 * Created by guxuede on 2017/6/3 .
 */
public class ActionsComponent implements Component {

    public final Array<Action> actions = new Array(0);

    public void addAction (Entity entity,Action action) {
        action.setActor(entity);
        actions.add(action);
    }

    public void removeAction (Action action) {
        if (actions.removeValue(action, true)) action.setActor(null);
    }

    /** Returns true if the actor has one or more actions. */
    public boolean hasActions () {
        return actions.size > 0;
    }

    /** Removes all actions on this actor. */
    public void clearActions () {
        for (int i = actions.size - 1; i >= 0; i--)
            actions.get(i).setActor(null);
        actions.clear();
    }

}
