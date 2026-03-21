package io.github.some_example_name.lwjgl3.collision;

import com.badlogic.gdx.math.Rectangle;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import io.github.some_example_name.lwjgl3.entities.AiEntity;
import io.github.some_example_name.lwjgl3.entities.Answer;
import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.Obstacle;
import io.github.some_example_name.lwjgl3.entities.Collectable;
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
                List<iCollidable> toRemove = new ArrayList<>();
                // snapshot via a method that returns a copy
                for (iCollidable c : collisionManager.getCollidableEntities()) {
                    if (c instanceof Answer) {
                        toRemove.add(c);
                    }
                }
                for (iCollidable c : toRemove) {
                    collisionManager.removeCollidableEntity(c);
                }
                return;
            }
        }

        player.takeDamage(1);
        player.triggerInvincibility(); // 1 second of invincibility + blink
        obstacle.setHitPlayer();
    }

    // AI vs WALL
    public void resolveCollisions(AiEntity ai, Obstacle obstacle) {
        resolveEntityObstacle(ai, obstacle);
    }

    // PLAYER vs AI
    public void resolveCollisions(PlayableEntity player, AiEntity ai) {
        System.out.println("[Collision] Player collided with AI!");
        outputManager.playSound("HIT_EVENT");
        sceneManager.setCurrentScene("Scene3");
        criticalCollisionOccurred = true;
    }

    // MULTIPLIER vs PLAYER
    public void resolveCollisions(PlayableEntity player, Collectable collectable) {
        if (collectable.isCollected()) {
            return;
        }

        player.addPowerUp();
        collectable.setCollected(true);
    }

    // Shared Entity-Obstacle Collision Logic
    public void resolveEntityObstacle(Entity entity, Obstacle obstacle) {
        System.out.println("[Collision] " + entity.getId() + " collided with " + obstacle.getId());

        if (entity instanceof PlayableEntity) {
            PlayableEntity player = (PlayableEntity) entity;
            if (player.isNewObstacleCollision()) {
                outputManager.playSound("COLLISION_EVENT");
            }
        }

        Rectangle entityBounds   = ((iCollidable) entity).getCollisionBounds();
        Rectangle obstacleBounds = obstacle.getCollisionBounds();

        float overlapLeft   = (entityBounds.x + entityBounds.width)     - obstacleBounds.x;
        float overlapRight  = (obstacleBounds.x + obstacleBounds.width)  - entityBounds.x;
        float overlapBottom = (entityBounds.y + entityBounds.height)     - obstacleBounds.y;
        float overlapTop    = (obstacleBounds.y + obstacleBounds.height) - entityBounds.y;

        float minX = Math.min(overlapLeft, overlapRight);
        float minY = Math.min(overlapBottom, overlapTop);

        float pushBuffer = 0.5f;

        if (minX < minY) {
            // Horizontal collision
            if (overlapLeft < overlapRight)
                entity.getPosition().x -= (overlapLeft + pushBuffer);
            else
                entity.getPosition().x += (overlapRight + pushBuffer);
        } else {
            // Vertical collision
            if (overlapBottom < overlapTop)
                entity.getPosition().y -= (overlapBottom + pushBuffer);
            else
                entity.getPosition().y += (overlapTop + pushBuffer);
        }
    }
}