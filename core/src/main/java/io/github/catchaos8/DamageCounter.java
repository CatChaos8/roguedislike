package io.github.catchaos8;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;


public class DamageCounter {
    private final float damage;
    private final int crits;
    private final float x, y;
    private final BitmapFont font;
    private float time;

    private static final float speed = 20f;

    private static final float existenceTime = 1f;

    public DamageCounter(float damage, int crits, float x, float y, BitmapFont font) {
        this.damage = damage;
        this.crits = crits;
        this.x = x;
        this.y = y;

        this.font = font.getCache().getFont();

        this.time = 0;
    }

    public boolean shouldDelete() {
        return existenceTime < time;
    }

    public void update(float deltaTime) {
        this.time += deltaTime;
    }

    public void render(SpriteBatch batch, Viewport worldViewport) {

        this.font.getData().setScale(1f);

        String text;
        float alpha = 1 - (time/existenceTime);
        if(crits > 0) {
            this.font.setColor(1, 0, 0, alpha); //Red
            if(crits > 1) {
                text = "Crit X" + crits + "!\n " + Math.round(damage*10);
            } else {
                text = "Crit!\n " + Math.round(damage*10);
            }
        } else {
            this.font.setColor(1, 1, 1, alpha); //White
            text = String.valueOf(Math.round(damage*10));
        }
        Vector2 screenCoords = worldViewport.project(new Vector2(x, y));
        // Convert to UI space (top-left origin if needed)
        font.draw(batch, text, screenCoords.x, screenCoords.y + speed*time);
    }

}
