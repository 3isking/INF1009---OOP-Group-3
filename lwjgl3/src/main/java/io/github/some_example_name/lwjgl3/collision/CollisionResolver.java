package io.github.some_example_name.lwjgl3.collision;

import java.util.ArrayList;
import java.util.List;

import io.github.some_example_name.lwjgl3.entities.Answer;
import io.github.some_example_name.lwjgl3.entities.Collectable;
import io.github.some_example_name.lwjgl3.entities.Obstacle;
import io.github.some_example_name.lwjgl3.entities.PlayableEntity;
import io.github.some_example_name.lwjgl3.entities.iCollidable;
import io.github.some_example_name.lwjgl3.outputs.iOutputManager;
import io.github.some_example_name.lwjgl3.scenes.iSceneManager;

public final class CollisionResolver {
    private iSceneManager sceneManager;
    private iOutputManager outputManager;
    private iCollisionManager collisionManager;
    private Boolean criticalCollisionOccurred = false;

    public CollisionResolver(iSceneManager sceneManager, iOutputManager outputManager, iCollisionManager collisionManager) {
        this.sceneManager = sceneManager;
        this.outputManager = outputManager;
        this.collisionManager = collisionManager;
    }

    public boolean hasCriticalCollisionOccurred() {
        if (criticalCollisionOccurred) {
            criticalCollisionOccurred = false;
            return true;
        }
        return false;
    }

    // Generic Collisions
    public void resolveCollisions(iCollidable a, iCollidable b) {
        a.collide(b);
    }

    // Player vs Any Obstacle
    public void resolveCollisions(PlayableEntity player, Obstacle obstacle) {
        // Skip if player is currently invincible
        if (player.isInvincible()) {
            return;
        }

        if (obstacle.getHitPlayer()) {
            return;
        }

        if (obstacle instanceof Answer) {
            Answer answer = (Answer) obstacle;
            if (answer.isCorrect()) {
                return;
            }
        }

        player.takeDamage(1);
        outputManager.playSound("HIT_EVENT"); // hit.mp3 when player takes damage
        player.triggerInvincibility(); // 1 second of invincibility + blink
        obstacle.setHitPlayer();
    }

    public void resolveCollisions(PlayableEntity player, Answer answer) {
    answer.setWasHit();

    List<iCollidable> toRemove = new ArrayList<>();
        for (iCollidable c : collisionManager.getCollidableEntities()) {
            if (c instanceof Answer) {
                ((Answer) c).setVisible(false);
                toRemove.add(c); // always collect for removal
            }
        }

        // Remove from collision manager regardless of correct/wrong
        for (iCollidable c : toRemove) {
            collisionManager.removeCollidableEntity(c);
        }

        if (answer.isCorrect()) {
            outputManager.playSound("CORRECT_EVENT");
        }
    }

    // MULTIPLIER vs PLAYER
    public void resolveCollisions(PlayableEntity player, Collectable collectable) {
        if (collectable.isCollected()) {
            return;
        }

        player.addPowerUp();
        collectable.setCollected(true);
    }
}