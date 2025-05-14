package io.github.catchaos8;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Bullet {
    int bounces;
    int pierce;
    float damage;
    float size;
    float speedX;
    float speedY;

    float x, y;
    float distanceTraveled = 0f;
    float maxDistance;

    Animation<TextureRegion> animation;
    float animationTime = 0f;  // Track time for animation

    // In Bullet class
    Array<Enemy> hitEnemies = new Array<>();  // This will store the enemies the bullet has hit

    public Bullet(float x, float y, float speedX, float speedY, float size, int bounces, int pierce, Animation<TextureRegion> animation, float damage, float maxDistance) {
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.size = size;
        this.bounces = bounces;
        this.pierce = pierce;
        this.animation = animation;
        this.damage = damage;
        this.maxDistance = maxDistance;
    }

    // Update bullet position and travel distance
    // In Bullet class, update logic to reset hitEnemies when the bullet moves away from the enemy
    public void update(float deltaTime) {
        // Update bullet position
        x += speedX * deltaTime;
        y += speedY * deltaTime;

        // Update the animation
        animationTime += deltaTime;

        // Check if bullet is no longer overlapping with the enemies
        for (int i = hitEnemies.size - 1; i >= 0; i--) {
            Enemy enemy = hitEnemies.get(i);
            if (!getBounds().overlaps(enemy.getBounds())) {
                hitEnemies.removeIndex(i);  // Remove from the list of enemies hit
            }
        }

        // Increase the distance traveled
        distanceTraveled += (float) (Math.sqrt(speedX * speedX + speedY * speedY) * deltaTime);
    }

    // Check if the bullet should be removed (out of range)
    public boolean shouldRemove() {
        return distanceTraveled > maxDistance || pierce <= 0;
    }

    // Render the bullet on the screen
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(animationTime, true); // Pass the updated stateTime
        float width = size;
        float height = size;
        batch.draw(currentFrame, x - size / 2, y - size / 2, width, height);
    }

    // Get the bounding box of the bullet for collision detection
    public Rectangle getBounds() {
        return new Rectangle(x - size / 2, y - size / 2, size, size);
    }
}
