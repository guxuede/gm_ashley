/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.guxuede.gm.gdx.system;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.guxuede.gm.gdx.component.ActorStateComponent;
import com.guxuede.gm.gdx.component.ActorComponent;
import com.siondream.superjumper.components.BobComponent;

import static com.guxuede.gm.gdx.Mappers.actorCM;
import static com.guxuede.gm.gdx.Mappers.actorStateCM;

public class MovementSystem extends IteratingSystem {
    private Vector2 tmp = new Vector2();

    public MovementSystem() {
        super(Family.all(ActorComponent.class, ActorStateComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        ActorStateComponent pos = actorStateCM.get(entity);
        pos.velocity.x = -pos.acceleration.x / 10.0f * BobComponent.MOVE_VELOCITY;
        pos.velocity.y = -pos.acceleration.y / 10.0f * BobComponent.MOVE_VELOCITY;
        tmp.set(pos.velocity).scl(deltaTime);

        ActorComponent mov = actorCM.get(entity);
        mov.setPosition(mov.getX() + tmp.x, mov.getY() + tmp.y);

        pos.isMoving = tmp.x != 0 || tmp.y!=0;
        if (pos.isMoving){
            pos.degrees = tmp.angle();
            pos.direction = convertDegreesToDirection(pos.degrees);
            System.out.println("degrees = [" + pos.degrees + "], direction = [" + pos.direction + "]");
        }
    }


    public int convertDegreesToDirection(float degrees){
        int direction = 0;
        if(degrees >= 45 && degrees < 135){
            direction = ActorStateComponent.UP;
        }else if(degrees >= 135 && degrees < 225){
            direction = ActorStateComponent.LEFT;
        }else if(degrees >= 225 && degrees < 325){
            direction = ActorStateComponent.DOWN;
        }else if(degrees >= 325 || degrees < 45){
            direction = ActorStateComponent.RIGHT;
        }
        return direction;
    }

}
