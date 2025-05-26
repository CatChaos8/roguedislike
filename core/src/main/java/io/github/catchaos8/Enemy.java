package io.github.catchaos8;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Enemy {
    float hp;
    float maxHP;
    float damage;
    float speed;

    float x, y;
    float size;
    float xp;

    float maxDist = 100f;

    Texture texture;

    private final Vector2 velocity = new Vector2();

    public Enemy(float x, float y, float maxHP, float damage, float speed, Texture texture, float size, int xp) {
        this.x = x;
        this.y = y;
        this.maxHP = maxHP;
        this.hp = maxHP;
        this.damage = damage;
        this.speed = speed;
        this.texture = texture;

        this.xp = xp;

        this.size = size;  // Default size (adjust to match your asset)
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, size, size);
    }

    public void update(float deltaTime, float targetX, float targetY, Rectangle playerBounds, PlayerInfo playerInfo) {

        //Apply Knockback stuff
        x += velocity.x * deltaTime;
        y += velocity.y * deltaTime;

        //Decrease the strength of the kb over time
        velocity.scl((float)Math.pow(0.85f, Gdx.graphics.getDeltaTime() * 60f)); //Makes it not fps dependent

        // movement towards the player
        float dx = targetX - x;
        float dy = targetY - y;
        float length = (float) Math.sqrt(dx * dx + dy * dy);

        if (length != 0) {
            dx /= length;
            dy /= length;
        }

        // Move the enemy
        x += dx * speed * deltaTime;
        y += dy * speed * deltaTime;

        // Prevent enemy from going inside the player
        Rectangle enemyBounds = getBounds();
        if (enemyBounds.overlaps(playerBounds) && playerInfo.isDoCollision()) {

            // Push enemy out of player
            float overlapX = Math.min(
                enemyBounds.x + enemyBounds.width - playerBounds.x,
                playerBounds.x + playerBounds.width - enemyBounds.x
            );
            float overlapY = Math.min(
                enemyBounds.y + enemyBounds.height - playerBounds.y,
                playerBounds.y + playerBounds.height - enemyBounds.y
            );

            if (overlapX < overlapY) {
                if (enemyBounds.x < playerBounds.x) {
                    x -= overlapX;
                } else {
                    x += overlapX;
                }
            } else {
                if (enemyBounds.y < playerBounds.y) {
                    y -= overlapY;
                } else {
                    y += overlapY;
                }
            }

            if(playerInfo.getiFrames() >= 0.5f) {
                playerInfo.dealDamage(damage);
                playerInfo.setiFrames(0);
                playerInfo.applyKB(5f, new Vector2(x + size/2, y + size/2));
                playerInfo.setDoCollision(false);
            }
        }
    }


    public void takeDamage(float amount) {
        hp -= amount;
        if (hp < 0) hp = 0;
    }

    public boolean isDead() { //If its dead
        return hp <= 0;
    }

    public Rectangle getBounds() { //Like the hitbox
        return new Rectangle(x, y, size, size);
    }

    public Vector2 getPosition() {
        return new Vector2(x + size / 2, y + size / 2);
    }

    public void doCollision(Array<Enemy> allEnemies) { //So that the enemies cannot overlap
        Vector2 myPos = getPosition(); //Current pos
        Vector2 repel = new Vector2(0, 0); //repel amount
        int count = 0;

        for (Enemy other : allEnemies) {//Checks every enemy
            if (other == this) continue;
            Vector2 otherPos = other.getPosition(); //Other enemy pos
            float dist = myPos.dst(otherPos);

            float minDistance = (this.size+other.size)/2; //Gets the minimum distance from the other enemy

            if (dist < minDistance && dist > 0.001f) {
                Vector2 diff = new Vector2(myPos).sub(otherPos).nor().scl(1 / dist);
                repel.add(diff);
                count++;
            }
        }

        if (count > 0) {
            repel.scl(1f / count).nor().scl(speed * 0.5f); // control strength
            x += repel.x * Gdx.graphics.getDeltaTime();
            y += repel.y * Gdx.graphics.getDeltaTime();
        } //Moves the other enemies away

    }

    public void applyKnockBack(float strength, float sourceX, float sourceY) {
        float deltaX = (x + size/2f) - sourceX;
        float deltaY = (y + size/2f) - sourceY;
        float distFromShot = (float) Math.sqrt(deltaX*deltaX + deltaY*deltaY);

        if(distFromShot != 0) {
            deltaX /=distFromShot;
            deltaY /=distFromShot;

            velocity.x += deltaX*strength;
            velocity.y += deltaY*strength;
        }
    }

}
