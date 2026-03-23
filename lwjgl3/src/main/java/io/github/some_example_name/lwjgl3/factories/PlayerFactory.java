package io.github.some_example_name.lwjgl3.factories;

import io.github.some_example_name.lwjgl3.collision.iCollisionManager;
import io.github.some_example_name.lwjgl3.entities.PlayableEntity;
import io.github.some_example_name.lwjgl3.movement.iMovementManager;

public class PlayerFactory implements EntityFactory<PlayableEntity> {

    private iCollisionManager collisionManager;
    private iMovementManager movementManager;

    public PlayerFactory(iCollisionManager collisionManager, iMovementManager movementManager) {
        this.collisionManager = collisionManager;
        this.movementManager = movementManager;
    }

    @Override
    public PlayableEntity createEntity(Class<PlayableEntity> type, float x, float y, Object extra) {
        PlayableEntity player =  new PlayableEntity(x, y, movementManager, collisionManager);
        player.setId("player_1");
        return player;
    }

}