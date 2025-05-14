package io.github.catchaos8;

import com.badlogic.gdx.graphics.Texture;

public class EnemyType {
    public float health, speed, size, damage;
    public int xp;
    public Texture texture;
    public float weight;

    public EnemyType( float weight, float health, float speed, float size, int xp, float damage, Texture texture) {
        this.health = health;
        this.speed = speed;
        this.size = size;
        this.damage = damage;
        this.xp = xp;
        this.weight = weight;
        this.texture = texture;
    }
}
