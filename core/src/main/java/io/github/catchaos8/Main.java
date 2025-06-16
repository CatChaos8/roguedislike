package io.github.catchaos8;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.List;
import java.util.Random;



/** {@link ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    float damageDealt;
    int enemiesKilled;

    ShapeRenderer shapeRenderer;


    float fadeTime;
    float fadeAlpha;
    boolean isFading;

    float time;
    boolean showHelp = false;

    Texture background;

    float delta;
    float gameSpeed;

    boolean started;

    Texture mouseHitboxTexture;
    Sprite mouseHitbox;

    SpriteBatch batch;
    FitViewport viewport;
    float velocityX;
    float velocityY;

    float acceleration = 10f;       // How quickly you speed up
    float friction = 6f;           // How quickly you slow down

    Random random = new Random();

    boolean isNotPaused = true;
    private boolean canUnPause = true;

    Texture clearing;
    TextureRegion tiledRegion;

    float hpBarTotalWidth;
    float stmBarTotalWidth;
    PlayerInfo player;
    Texture playerT;
    Sprite playerS;
    Texture barsFull;
    TextureRegion barLeftEmpty;
    TextureRegion hpBarRight;
    TextureRegion barRightEmpty;
    TextureRegion stmBarRight;
    TextureRegion barEmpty;

    Array<Bullet> bullets = new Array<>();
    Animation<TextureRegion> bulletAnimation;
    Texture playerBullet;

    Array<Enemy> enemies = new Array<>();
    Texture enemyTexture;
    Array<EnemyType> enemyTypes = new Array<>();

    float shotCD = 0;

    Texture crosshairTexture;
    Sprite crosshairSprite;

    OrthographicCamera uiCamera;

    boolean levelUp = false ;
    Texture levelUpSelectionBG;
    Texture levelUpSelectionOption;
    Texture levelUpSelectionOptionHover;
    BitmapFont font;
    Array<StatOption> statOptions = new Array<>();

    Texture xpBarFull;
    Texture xpBarEmpty;

    Vector2 dash;
    byte dashUp;
    byte dashRight;

    Array<DamageCounter> damageCounters;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        fadeTime = 2f;

        background = new Texture("background.png");

        mouseHitboxTexture = new Texture("empty.png");
        mouseHitbox = new Sprite(mouseHitboxTexture);

        mouseHitbox.setScale(0.01f);

        //batch and cam
        batch = new SpriteBatch();
        viewport = new FitViewport(10, 6);

        //Set to fullscreen
        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(displayMode);

        //UI cam
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //Fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/slkscr.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 32;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);

        started = false;
    }

    private void set() {
        damageDealt = 0;
        enemiesKilled = 0;

        fadeAlpha = 0;
        time = 0;

        showHelp = false;

        Gdx.input.setCursorCatched(true);

        isNotPaused = true;
        canUnPause = true;

        clearing = new Texture("grassBackground.png");
        clearing.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat); //Makes it tile
        tiledRegion = new TextureRegion(clearing);


        player = new PlayerInfo();

        playerT = new Texture("player.png");
        playerS = new Sprite(playerT);
        playerS.setSize(player.getSize(), player.getSize());

        playerBullet = new Texture("playerProjectile.png");

        // SET UP ANIMATION
        TextureRegion[][] bulletFrames = TextureRegion.split(playerBullet, 15, 15);
        bulletAnimation = new Animation<>(
            0.04f,  // Frame duration
            bulletFrames[0]
        );

        enemyTexture = new Texture("enemy.png"); // Add your enemy texture
        enemies = new Array<>();
        enemyTypes.add(new EnemyType(100, 10, 1.1f, 0.25f, 60, 10, enemyTexture)); //Normal
//        enemyTypes.add(new EnemyType(70, 5, 1.5f, 0.15f, 40, 10, enemyTexture)); //Fast(Deleted cause I don't like it
        enemyTypes.add(new EnemyType(10, 25, 1f, 0.5f, 50, 10, enemyTexture)); //Tank
        enemyTypes.add(new EnemyType(1, 250, 0.8f, 0.65f, 500, 10, enemyTexture)); //Mini boss


        bullets = new Array<>();

        //Crosshair
        crosshairTexture = new Texture("crosshair.png");
        crosshairSprite = new Sprite(crosshairTexture);
        crosshairSprite.setSize(0.2f, 0.2f);

        barsFull = new Texture("bars/bars.png");
        barLeftEmpty =  new TextureRegion(barsFull,0, 15, 1, 5);
        hpBarRight =    new TextureRegion(barsFull, barsFull.getWidth()-4, 0, 4, 5);
        barRightEmpty = new TextureRegion(barsFull, barsFull.getWidth()-4, 15, 4, 5);
        stmBarRight =   new TextureRegion(barsFull, barsFull.getWidth()-4, 5, 4, 5);
        barEmpty = new TextureRegion(barsFull, 4, 15, 1, 5);

        levelUpSelectionBG = new Texture("levelUpBackground.png");
        levelUpSelectionOption = new Texture("levelUpOption.png");
        levelUpSelectionOptionHover = new Texture("levelUpOptionSelect.png");
        statOptions.clear();
        xpBarEmpty = new Texture("xpBarEmpty.png");
        xpBarFull  = new Texture("xpBarFull.png");

        dash = new Vector2(0,0);
        dashUp = 0;
        dashRight = 0;
        hpBarTotalWidth = calculateMaxHPWidth();
        stmBarTotalWidth = calculateMaxStmWidth();

        damageCounters = new Array<>();
    }

    @Override
    public void render() {
        gameSpeed = 2f;
        delta = Gdx.graphics.getDeltaTime()*gameSpeed; //Can change the speed the game runs at with this
        if(started) {
            inputs();
            logic();
            draw();
        } else {
            time +=delta;
            if(player == null || time > 2f) {
                ScreenUtils.clear(Color.BLACK);
            }

            batch.setProjectionMatrix(viewport.getCamera().combined);
            batch.begin();

            batch.end();

            //UI
            uiCamera.update();
            batch.setProjectionMatrix(uiCamera.combined);
            batch.begin();

            if(time > fadeTime || player == null) {
                //Draw in ui so that it follows the player(if they started a game already)
                batch.draw(background, 0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());

                //Make it sorta fade out and stuff
                font.setColor(1, 1, 1, (float) Math.max(Math.abs(Math.sin(time)), 0.1f));

                GlyphLayout nameLayout;
                if (player == null) {
                    nameLayout = new GlyphLayout(font, "Press SPACE to start");
                } else {
                    nameLayout = new GlyphLayout(font, "Press SPACE to play again");
                }
                float nameX = uiCamera.viewportWidth / 2 - nameLayout.width / 2f; //Centered
                float nameY = uiCamera.viewportHeight / 25 * 2;

                font.draw(batch, nameLayout, nameX, nameY);

                if (player == null) {
                    nameLayout = new GlyphLayout(font, "Press H for help");

                    nameX = uiCamera.viewportWidth / 2 - nameLayout.width / 2f; //Centered
                    nameY = uiCamera.viewportHeight / 25;

                    font.draw(batch, nameLayout, nameX, nameY);
                } else {
                    font.setColor(Color.WHITE);
                    font.getData().setScale(1.25f);
                    //Draw the player's stats if they r dead
                    nameLayout = new GlyphLayout(font, player.getString());

                    nameX = 150;
                    nameY = uiCamera.viewportHeight / 2 + nameLayout.height/2f;

                    font.draw(batch, nameLayout, nameX, nameY);

                    int score = (int) (Math.floor((player.getSurvivedTime()*5 + player.getLvl()*100 + enemiesKilled *10 + damageDealt*5)/10)*10);
                    String string =
                        "Time Survived: " + player.getSurvivedTimeString() + "\n" +
                        "Player Level: " + player.getLvl() + "\n" +
                        "Enemies Killed: " + enemiesKilled + "\n" +
                        "Damage Dealt: " + damageDealt + "\n" +
                        "Total Score: " + score;
                    nameLayout = new GlyphLayout(font, string);
                    nameX = uiCamera.viewportWidth - 250 - nameLayout.width;
                    nameY = uiCamera.viewportHeight/2 + nameLayout.height/2f;

                    font.draw(batch, nameLayout, nameX, nameY);
                }

                //Reset Font
                font.setColor(1, 1, 1, 1);

                //Help Menu
                if (showHelp) {
                    nameLayout = new GlyphLayout(font, "Press ESC to Pause");

                    nameX = uiCamera.viewportWidth / 2 - nameLayout.width / 2f; //Centered
                    nameY = uiCamera.viewportHeight / 25 * 16;

                    font.draw(batch, nameLayout, nameX, nameY);

                    nameLayout = new GlyphLayout(font, "Press SPACE to dash");

                    nameX = uiCamera.viewportWidth / 2 - nameLayout.width / 2f; //Centered
                    nameY = uiCamera.viewportHeight / 25 * 15;

                    font.draw(batch, nameLayout, nameX, nameY);

                    nameLayout = new GlyphLayout(font, "Left Click to Shoot");

                    nameX = uiCamera.viewportWidth / 2 - nameLayout.width / 2f; //Centered
                    nameY = uiCamera.viewportHeight / 25 * 14;

                    font.draw(batch, nameLayout, nameX, nameY);

                    nameLayout = new GlyphLayout(font, "Kill Enemies to Gain XP and Level Up!");

                    nameX = uiCamera.viewportWidth / 2 - nameLayout.width / 2f; //Centered
                    nameY = uiCamera.viewportHeight / 25 * 13;

                    font.draw(batch, nameLayout, nameX, nameY);


                    nameLayout = new GlyphLayout(font, "Level Up to Gain Upgrades!");

                    nameX = uiCamera.viewportWidth / 2 - nameLayout.width / 2f; //Centered
                    nameY = uiCamera.viewportHeight / 25 * 12;

                    font.draw(batch, nameLayout, nameX, nameY);
                }
            }

            batch.end();
            if(time < fadeTime && player != null) {
                draw();
                //Fade the screen so that when you die it fades into the death screen
                fadeAlpha += delta/fadeTime;


                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                shapeRenderer.setProjectionMatrix(uiCamera.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(0, 0, 0, fadeAlpha);
                shapeRenderer.rect(0, 0, uiCamera.viewportWidth, uiCamera.viewportHeight);
                shapeRenderer.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);


            }

            testStart();


        }
    }

    private void testStart() {

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && (player == null || time >= fadeTime)) {
            set();
            isFading = false;
            started = true;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.H) && player == null) {
            showHelp = !showHelp;
        }
    }


    private void inputs() {
        if(isNotPaused) {
            Gdx.input.setCursorCatched(true);
            float  maxSpeed;
            if(!player.isExhausted()) {
                maxSpeed = player.getSpeed() / 2f;
            } else {
                //Exhausted speed debuff
                maxSpeed = player.getSpeed() / 4f;
            }

            // Apply friction (slows down movement over time if u arent pressing inputs)
            if (velocityX > 0) {
                velocityX -= friction * delta;
                if (velocityX < 0) velocityX = 0;
            } else if (velocityX < 0) {
                velocityX += friction * delta;
                if (velocityX > 0) velocityX = 0;
            }

            if (velocityY > 0) {
                velocityY -= friction * delta;
                if (velocityY < 0) velocityY = 0;
            } else if (velocityY < 0) {
                velocityY += friction * delta;
                if (velocityY > 0) velocityY = 0;
            }
            dashUp = 0;
            dashRight = 0;
            //For arrow keys/wasd for moving
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                velocityX += acceleration * delta;
                dashRight += 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                velocityX -= acceleration * delta;
                dashRight += 2;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                velocityY += acceleration * delta;
                dashUp += 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                velocityY -= acceleration * delta;
                dashUp += 2;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !player.isExhausted()) { //Cant dash while exhausted
                int dashStrength = 10;
                boolean dashed = false;
                if(dashUp == 1) {
                    dash.y = 1;
                    dashed = true;
                } else if(dashUp == 2){
                    dash.y = -1;
                    dashed = true;
                }
                if(dashRight == 1) {
                    dash.x = 1;
                    dashed = true;
                } else if (dashRight == 2){
                    dash.x = -1;
                    dashed = true;
                } else if(dashed) {
                    dash.x = 0;
                }
                if (dashed && dashUp == 0) {
                    dash.y = 0;
                }
                float normalizerThing = (float) Math.sqrt(dash.x*dash.x + dash.y*dash.y);

                if(dashed && player.spendStamina(50)) {
                    player.setDoCollision(false); //Also makes them invulnerable
                    if (normalizerThing > 0) {
                        dash.x /= normalizerThing;//Normalizes the thing so that diagonal isnt better than going in the 4 directions
                        dash.y /= normalizerThing;
                        dash.scl(dashStrength);
                    }
                }
            }

            // Normalize the movement vector to prevent faster diagonal movement
            float movementLength = (float) Math.sqrt(velocityX * velocityX + velocityY * velocityY);
            if (movementLength > maxSpeed) {
                velocityX = (velocityX / movementLength) * maxSpeed;
                velocityY = (velocityY / movementLength) * maxSpeed;
            }

            // Apply velocity
            playerS.translate(velocityX * delta, velocityY * delta);

            float attackSpeed;
            if(player.isExhausted()) {
                attackSpeed = player.getAttackSpeed()*2; //Double attack speed, but lower damage while exhausted
            } else {
                attackSpeed = player.getAttackSpeed();
            }
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && shotCD >= 1f / attackSpeed) {
                shotCD = 0;
                shoot();
                player.spendStamina(player.getPlayerAttackStmCost());
            }

            if ((Math.abs(dash.x) < 0.5f && Math.abs(dash.y) < 0.5f) && (player.getPlayerKB().x < 0.2f && player.getPlayerKB().y < 0.2f)) {
                player.setDoCollision(true);
            }
            playerS.translate(dash.x * delta, dash.y * delta);
            dash.scl((float)Math.pow(0.90f, delta*60f)); //Makes it not fps dependent

        } else {
            Gdx.input.setCursorCatched(false);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && canUnPause) {
            isNotPaused = !isNotPaused;
        }
    }


    private void logic() {
        if (isNotPaused) {
            time += delta;

            player.setX(playerS.getX());
            player.setY(playerS.getY());
            //Apply player kb
            playerS.translate(player.getPlayerKB().x * delta, player.getPlayerKB().y * delta);
            player.setPlayerKB(player.getPlayerKB().scl((float)Math.pow(0.9f, delta * 60f))); //Makes it not fps dependent);

            if (player.getXp() >= player.getXpToLevelUp()) {
                levelUp();
            }//If they have enough xp to levelup

            shotCD += delta;//Shot Cooldown
            player.addIFrames(delta);//Manages IFrames
            player.heal(delta * player.getHpRegen());//HP Regen
            player.regenStamina(delta * player.getStaminaRegen()); //stm regen

            // Spawn 1 enemy every 2 seconds
            float enemySpawnTime = 2f;
            if (random.nextDouble(0,1) < delta * 1 / enemySpawnTime) {
                for (int i = 0; i < Math.min(player.getLvl(), 10); i++) {
                    if(enemies.size < 250) {
                        spawnEnemy();
                    }
                }
            }

            // Get the player coords
            float playerX = playerS.getX() + playerS.getWidth() / 4f;
            float playerY = playerS.getY() + playerS.getHeight() / 4f;
            Rectangle playerBounds = playerS.getBoundingRectangle();  // Get the player's hitbox

            // Check collision with each enemy
            for (int i = enemies.size - 1; i >= 0; i--) {
                Enemy enemy = enemies.get(i);
                // Update the enemy's position and state

                if((enemy.x-playerX)*(enemy.x-playerX) + (enemy.y-playerY) * (enemy.y-playerY) > enemy.maxDist) {
                    enemies.removeIndex(i); //Despawns enemy if the player gets too far from it
                    spawnEnemy(); //Respawns a new enemy so that u cant just liek run aaway forever
                }
                enemy.update(delta, playerX, playerY, playerBounds, player);
                enemy.doCollision(enemies);
                if (enemy.isDead()) { // Remove enemy if dead
                    enemies.removeIndex(i);
                    player.gainXp((int) enemy.xp); //Gives the player xp and increases the kill counter
                    enemiesKilled++;
                }

            }

            // Handle bullet collisions with enemies
            handleBulletCollisions();

            if (player.getHp() <= 0) {
                isNotPaused = false;//If game over
                canUnPause = false; //Basically freezes the game
                started = false; //Sets started to false so that it resets when u hit space
                player.setSurvivedTime(time);
                time = 0;
                Gdx.input.setCursorCatched(false);
            }
            updateBullets(delta);
        }
    }

    private void handleBulletCollisions() {
        for (int i = bullets.size - 1; i >= 0; i--) {
            if (i >= bullets.size) continue; // Ensure index is still valid
            Bullet bullet = bullets.get(i);
            boolean bulletHit = false;

            for (Enemy enemy : enemies) {
                if (bullet.getBounds().overlaps(enemy.getBounds()) && !bullet.hitEnemies.contains(enemy, true)) {
                    bullet.hitEnemies.add(enemy);

                    enemy.applyKnockBack(player.getBulletKnockback(), bullet.x, bullet.y);
                    float critsMulti = bullet.critMulti;
                    float bulletDmg = bullet.damage*critsMulti; //Damages the enemy based on crit chance and stuff
                    enemy.takeDamage(bulletDmg);
                    damageDealt += bulletDmg*10;
                    player.heal(bullet.damage * player.getLifeSteal());
                    damageCounters.add(new DamageCounter(bulletDmg, (int) ((critsMulti - 1)/player.getCritAmount()), bullet.x, bullet.y, font));

                    if (bullet.pierce <= 0) { //If there r no pierces left, bounce
                        bullet.bounce(enemies);
                    }
                    bullet.pierce--; //Decrease pierce so that it pierces

                    if (bullet.shouldRemove()) {
                        bullets.removeIndex(i);
                        bulletHit = true;
                        break; // exit enemy loop
                    }

                    bulletHit = true;
                    break;
                }
            }

            if (!bulletHit && bullet.shouldRemove() && i < bullets.size) {
                bullets.removeIndex(i);
            }
        }
    }




    private void draw() {
        //Make camera
        // Get player x and y
        float playerX = playerS.getX() + playerS.getWidth() / 2f;
        float playerY = playerS.getY() + playerS.getHeight() / 2f;

        // Get mouse position (I have to use a vector cause I cant use ints for some reason)
        Vector2 mouseVector = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mouseVector);

        // Get difference from mouse to player
        float dirX = mouseVector.x - playerX;
        float dirY = mouseVector.y - playerY;

        // Get Dist from player to mouse
        float distance = (float) Math.sqrt(dirX * dirX + dirY * dirY);

        // Normalize direction
        if (distance != 0) {
            dirX /= distance;
            dirY /= distance;
        }

        // Scale camera offset by how far the mouse ia  s from the player
        float offsetAmount = distance * 0.0f;

        float cameraX = playerX + dirX * offsetAmount;
        float cameraY = playerY + dirY * offsetAmount;

        // Set camera position
        viewport.getCamera().position.set(cameraX, cameraY, 0);
        viewport.getCamera().update();

        // Drawing
        ScreenUtils.clear(Color.BLACK);
        float width = viewport.getWorldWidth();
        float height = viewport.getWorldHeight();

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        // Draw background
        // Get background size in world units
        float scale = 0.25f;
        float tileWidth = 10f * scale;
        float tileHeight = 10f * scale;

        // Calculate camera view bounds
        float camLeft = viewport.getCamera().position.x - width / 2f;
        float camBottom = viewport.getCamera().position.y - height / 2f;
        float camRight = camLeft + width;
        float camTop = camBottom + height;

        // Tile the bg
        for (float x = camLeft - (camLeft % tileWidth) - tileWidth; x < camRight + tileWidth; x += tileWidth) {
            for (float y = camBottom - (camBottom % tileHeight) - tileHeight; y < camTop + tileHeight; y += tileHeight) {
                batch.draw(clearing, x, y, tileWidth, tileHeight);
            }
        }

        //Draw Enemies
        for (Enemy enemy : enemies) {
            enemy.render(batch);
        }


        // Draw player
        playerS.draw(batch);

        // Draw bullets
        for (Bullet bullet : bullets) {
            bullet.render(batch);
        }

        //Draw Crosshair
        // Position crosshair on mouse location
        Vector2 mouseWorld = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mouseWorld);

        crosshairSprite.setPosition(
            mouseWorld.x - crosshairSprite.getWidth() / 2f,
            mouseWorld.y - crosshairSprite.getHeight() / 2f
        );


        if(isNotPaused) {
            crosshairSprite.draw(batch);
        }

        mouseHitbox.setPosition( //Sets the hitbox for the mouse hitbox(To detect if it clicks and stuff)
            mouseWorld.x - mouseHitbox.getWidth()/2f,
            mouseWorld.y - mouseHitbox.getHeight()/2f
        );

        batch.end();

        //UI
        float uiHeight = uiCamera.viewportHeight;
        float uiWidth = uiCamera.viewportWidth;
        uiCamera.update();
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();


        Array<Integer> deletableDmgCounters = new Array<>();
        //Draw dmg counters
        for (int i = 0; i < damageCounters.size; i++) {
            DamageCounter damageCounter = damageCounters.get(i);
            damageCounter.render(batch, viewport);
            if(isNotPaused) {
                damageCounter.update(delta);
            }
            if(damageCounter.shouldDelete()) {
                deletableDmgCounters.add(i); //Adds it to an array of dmg counters to delete
            }
        }
        for(int i = 0; i < deletableDmgCounters.size; i++) { //Deletes the deletable counters
            damageCounters.removeIndex(deletableDmgCounters.get(i) - i);
        }

        font.setColor(1, 1, 1, 1);

        float xpPercent = (float) player.getXp() /player.getXpToLevelUp();
        int filledXP = (int) (xpPercent* xpBarFull.getWidth());

        float xpScale = 8f;
        TextureRegion xpFullRegion = new TextureRegion(xpBarFull, 0,0, filledXP, xpBarFull.getHeight());
        batch.draw(xpBarEmpty, 50, uiHeight - 50 - xpBarEmpty.getHeight()*xpScale, xpBarEmpty.getWidth()*xpScale, xpBarEmpty.getHeight()*xpScale);
        batch.draw(xpFullRegion, 50, uiHeight - 50 - xpFullRegion.getRegionHeight()*xpScale, xpFullRegion.getRegionWidth()*xpScale, xpFullRegion.getRegionHeight()*xpScale);




        int barScale = 10;
        //Health Bar
        TextureRegion hpFilled = new TextureRegion(barsFull, 4, 0, 1, 5);

        batch.draw(barLeftEmpty, 50, uiHeight-150, barLeftEmpty.getRegionWidth()*barScale, barLeftEmpty.getRegionHeight()*barScale);

        int segmentCount = Math.round(hpBarTotalWidth / (barScale/25f) - 4); //Draws the empty bar
        for (int i = 0; i < segmentCount; i++) {
            batch.draw(barEmpty, 60 + i * barScale, uiHeight - 150,
                barEmpty.getRegionWidth() * barScale, barEmpty.getRegionHeight() * barScale);
        }
        batch.draw(barRightEmpty, 60 + segmentCount*barScale, uiHeight - 150,
            barRightEmpty.getRegionWidth() * barScale, barRightEmpty.getRegionHeight()*barScale); //The end

        int hpBarFilledSegments = (int) (Math.ceil(((segmentCount + 4) * (player.getHp() / player.getMaxHP())) - 4)); //The filled area
        for (int i = 0; i < hpBarFilledSegments; i++) {
            batch.draw(hpFilled, 60 + i * barScale, uiHeight - 150,
                hpFilled.getRegionWidth() * barScale, hpFilled.getRegionHeight() * barScale);
        }

        hpBarRight = new TextureRegion(barsFull, barsFull.getWidth()-4 - Math.min(hpBarFilledSegments+ 1, 0), 0, 4 + Math.min(hpBarFilledSegments + 1, 0), 5);


        if(player.getHp() > 0) {
            batch.draw(hpBarRight, 60 + Math.max(hpBarFilledSegments, 0) * barScale, uiHeight - 150,
                hpBarRight.getRegionWidth() * barScale, hpBarRight.getRegionHeight() * barScale); //The end of the bar
        }


        //Stamina Bar
        TextureRegion stmFilled = new TextureRegion(barsFull, 4, 5, 1, 5);
        TextureRegion stmFilledEx = new TextureRegion(barsFull, 4, 10, 1, 5);
        //Draws the left side(Empty)
        batch.draw(barLeftEmpty, 50, uiHeight-150 - stmBarRight.getRegionHeight()*barScale*0.8f, barLeftEmpty.getRegionWidth()*barScale, barLeftEmpty.getRegionHeight()*barScale);
        //Empty bar
        segmentCount = Math.round(stmBarTotalWidth / (barScale/25f) - 4);
        for (int i = 0; i < segmentCount; i++) {
            batch.draw(barEmpty, 60 + i * barScale, uiHeight - 150 - stmBarRight.getRegionHeight()*barScale * 0.8f,
                barEmpty.getRegionWidth() * barScale, barEmpty.getRegionHeight() * barScale);
        }
        batch.draw(barRightEmpty, 60 + segmentCount*barScale, uiHeight - 150 - barRightEmpty.getRegionHeight()*barScale*0.8f,
            barRightEmpty.getRegionWidth() * barScale, barRightEmpty.getRegionHeight()*barScale); //The end

        //Filled bar
        int stmBarFilledSegments = (int) (Math.ceil(((segmentCount + 4) * (player.getStamina() / player.getMaxStamina())) - 4)); //The filled area
        //The filled area
        //The end of the bar
        if(!player.isExhausted()) {
            for (int i = 0; i < stmBarFilledSegments; i++) {
                batch.draw(stmFilled, 60 + i * barScale, uiHeight - 150 - stmBarRight.getRegionHeight() * barScale * 0.8f,
                    stmFilled.getRegionWidth() * barScale, stmFilled.getRegionHeight() * barScale);
            }
            stmBarRight = new TextureRegion(barsFull, barsFull.getWidth() - 4 - Math.min(stmBarFilledSegments, 0), 5, 4 + Math.min(stmBarFilledSegments, 0), 5);
        } else {
            for (int i = 0; i < stmBarFilledSegments; i++) {
                batch.draw(stmFilledEx, 60 + i * barScale, uiHeight - 150 - stmBarRight.getRegionHeight() * barScale * 0.8f,
                    stmFilledEx.getRegionWidth() * barScale, stmFilledEx.getRegionHeight() * barScale);
            }
            stmBarRight = new TextureRegion(barsFull, barsFull.getWidth() - 4 - Math.min(stmBarFilledSegments, 0), 10, 4 + Math.min(stmBarFilledSegments, 0), 5);
        }
        if (player.getStamina() > 0) {
            batch.draw(stmBarRight, 60 + Math.max(stmBarFilledSegments, 0) * barScale, uiHeight - 150 - stmBarRight.getRegionHeight() * barScale * 0.8f,
                stmBarRight.getRegionWidth() * barScale, stmBarRight.getRegionHeight() * barScale); //The end of the bar
        }

        if (levelUp) {
            // Scale background image
            float imageWidth = levelUpSelectionBG.getWidth() * uiWidth / 300f;
            float imageHeight = levelUpSelectionBG.getHeight() * uiWidth / 300f;

            float drawX = (uiWidth - imageWidth) / 2f;
            float drawY = (uiHeight - imageHeight) / 2f;

            batch.draw(levelUpSelectionBG, drawX, drawY, imageWidth, imageHeight);

            // Scale and position the 3 options
            float optionWidth = levelUpSelectionOption.getWidth() * uiWidth / 300f;
            float optionHeight = levelUpSelectionOption.getHeight() * uiHeight / 200f;

            // Space evenly within the background
            for (int i = 0; i < 3; i++) {
                float spacing = (imageWidth - 3 * optionWidth) / 4f;
                float optionX = drawX + spacing + i * (optionWidth + spacing);
                float optionY = drawY + (imageHeight - optionHeight) / 2f;

                float mouseX = Gdx.input.getX();
                float mouseY = uiHeight - Gdx.input.getY();

                boolean isMouseOver = mouseX >= optionX && mouseX <= optionX + optionWidth &&
                    mouseY >= optionY && mouseY <= optionY + optionHeight*0.75; // Checks if the mouse is hovering over the thing
                if(isMouseOver) {// Show the highlight on the one ur mouse is over
                    batch.draw(levelUpSelectionOptionHover, optionX, optionY, optionWidth, optionHeight);
                    if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                        levelUp = false;
                        isNotPaused = true;
                        canUnPause = true;
                        Runnable applyUpgrade = statOptions.get(i).applyUpgrade;
                        applyUpgrade.run();

                        hpBarTotalWidth = calculateMaxHPWidth();
                        stmBarTotalWidth = calculateMaxStmWidth();
                    }
                } else {
                    batch.draw(levelUpSelectionOption, optionX, optionY, optionWidth, optionHeight);
                }

                String upgradeName = statOptions.get(i).name;
                String upgradeDescription = statOptions.get(i).description;

                font.getData().setScale(1.25f); // Set scale before measuring

                // Measure name(Like get the centering and stuff)
                GlyphLayout nameLayout = new GlyphLayout(font, upgradeName);
                float nameX = optionX + (optionWidth - nameLayout.width) / 2f;
                float nameY = optionY + (optionHeight + nameLayout.height) / 1.65f;

                // Draw name
                font.draw(batch, nameLayout, nameX, nameY);

                // --- Draw Wrapped Description ---
                // Wrap width: use most of the option width, with a little margin
                float wrapWidth = optionWidth * 0.7f;
                float descX = optionX + (optionWidth - wrapWidth) / 2f;
                // Y position: below the name
                float descY = nameY - nameLayout.height - 15f;

                font.getData().setScale(1.1f);
                // Draw with wrapping and center alignment
                font.draw(batch, upgradeDescription, descX, descY, wrapWidth, Align.center, true);

            }
        }
        batch.end();

    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();

        batch.dispose();
        playerT.dispose();
        playerBullet.dispose();
        clearing.dispose();
        crosshairTexture.dispose();

        levelUpSelectionOptionHover.dispose();
        levelUpSelectionOption.dispose();
        levelUpSelectionBG.dispose();

        xpBarFull.dispose();
        xpBarEmpty.dispose();

        barsFull.dispose();
        font.dispose();
    }

    private void shoot() {
        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mousePos);

        // Gets the centered playerX and Y
        float playerX = playerS.getX() + playerS.getWidth() / 2f;
        float playerY = playerS.getY() + playerS.getHeight() / 2f;

        // Gets the bullet size
        float bulletSize = 0.1f * player.getBulletSize();

        // Gets the change in x and y from the mouseX/Y and playerX/Y
        float deltaX = mousePos.x - playerX;
        float deltaY = mousePos.y - playerY;

        // Makes change the same no matter how far your mouse is from the player (otherwise it would be faster the farther your mouse is)
        float length = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        deltaX /= length;
        deltaY /= length;

        // Convert to angle
        float baseAngle = (float) Math.atan2(deltaY, deltaX);
        float accuracyDegrees = player.getBulletAccuracy();
        float accuracyRadians = (float) Math.toRadians(accuracyDegrees);

        // Random spread for each bullet
        for (int i = 0; i < player.getForwardShots(); i++) {
            shootInDirection(baseAngle, accuracyRadians, playerX, playerY, bulletSize);
        }

        for (int i = 0; i < player.getBackwardsShots(); i++) {
            shootInDirection(baseAngle + (float) Math.PI, accuracyRadians, playerX, playerY, bulletSize); // Shoot in the opposite direction
        }

        for (int i = 0; i < player.getLeftShots(); i++) {
            shootInDirection(baseAngle + (float) Math.PI / 2f, accuracyRadians, playerX, playerY, bulletSize); // Shoot left
        }

        for (int i = 0; i < player.getRightShots(); i++) {
            shootInDirection(baseAngle - (float) Math.PI / 2f, accuracyRadians, playerX, playerY, bulletSize); // Shoot right
        }
    }

    // Helper method to shoot in a specific direction with spread
    private void shootInDirection(float baseAngle, float spreadRadians, float playerX, float playerY, float bulletSize) {
        // Add random spread
        float randomSpread = (random.nextFloat(0,1) - 0.5f) * spreadRadians;
        float finalAngle = baseAngle + randomSpread;

        // Get bullet speed and direction
        float bulletSpeed = player.getBulletSpeed();
        float bulletSpeedX = (float) Math.cos(finalAngle) * bulletSpeed;
        float bulletSpeedY = (float) Math.sin(finalAngle) * bulletSpeed;

        // Add player's current velocity to the bullet's velocity
        float playerSpeedBoost = (float) Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        if(playerSpeedBoost > 1f) { //The player speed boost would be noticable
            bulletSpeedX += velocityX;
            bulletSpeedY += velocityY;
        }

        //increase bullet distance based on player movement speed
        float adjustedDistance = getAdjustedDistance(bulletSpeedX, bulletSpeedY, playerSpeedBoost);

        //Make damage based on if they are exhausted or not(If they are exhausted, they have higher attack speed but lower damage)
        float damage;
        if(player.isExhausted()) {
            damage = player.getBulletDamage()/2;
        } else {
            damage = player.getBulletDamage();
        }

        float critMulti = getCritMulti();

        // Create new bullet
        Bullet newBullet = new Bullet(
            playerX, playerY,
            bulletSpeedX, bulletSpeedY,
            bulletSize,
            player.getBounce(), player.getPierce(), bulletAnimation,
            damage,
            adjustedDistance,
            critMulti
        );

        // Add bullet to the array
        bullets.add(newBullet);
    }

    private float getAdjustedDistance(float bulletSpeedX, float bulletSpeedY, float playerSpeedBoost) {
        float currentSpeed = (float) Math.sqrt(bulletSpeedX * bulletSpeedX + bulletSpeedY * bulletSpeedY);
        float adjustedDistance;
        if (playerSpeedBoost > 1f && currentSpeed > 0.01f) {
            //If the player is moving, adjust the thing(If the player speed boost is rlly low, it makes your range really high(intended)
            adjustedDistance = player.getBulletDistance() * (currentSpeed/playerSpeedBoost) * 0.5f;
        } else {
            //Otherwise use normal
            adjustedDistance = player.getBulletDistance();
        }
        return adjustedDistance;
    }


    private void updateBullets(float deltaTime) {
        for (int i = bullets.size - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update(deltaTime);
            if (bullet.shouldRemove()) {
                bullets.removeIndex(i);
            }
        }
    }

    private void spawnEnemy() {
        float buffer = 2f; // how far offscreen to spawn
        float spawnX = 0, spawnY = 0;
        float width = viewport.getWorldWidth();
        float height = viewport.getWorldHeight();
        float camX = viewport.getCamera().position.x;
        float camY = viewport.getCamera().position.y;

        int side = random.nextInt(0,4); // 0 = top, 1 = right, 2 = bottom, 3 = left

        switch (side) {
            case 0: // top
                spawnX = camX - width/2 + random.nextFloat() * width;
                spawnY = camY + height/2 + buffer;
                break;
            case 1: // right
                spawnX = camX + width/2 + buffer;
                spawnY = camY - height/2 + random.nextFloat() * height;
                break;
            case 2: // bottom
                spawnX = camX - width/2 + random.nextFloat() * width;
                spawnY = camY - height/2 - buffer;
                break;
            case 3: // left
                spawnX = camX - width/2 - buffer;
                spawnY = camY - height/2 + random.nextFloat() * height;
                break;
        }


        //Spawns enemy with the EnemyType stats
        EnemyType type = getRandomEnemyType(enemyTypes);
        enemies.add(new Enemy(spawnX, spawnY, type.health, type.damage, type.speed, type.texture, type.size, type.xp));
}
    private EnemyType getRandomEnemyType(Array<EnemyType> types) {//Gets a random enemy type based on weight
        float totalWeight = 0;
        for (EnemyType type : types) {
            totalWeight += type.weight;
        }

        float randomNum = random.nextFloat(0,1) * totalWeight;
        float current = 0;


        for (EnemyType type : types) {
            current += type.weight;
            if (randomNum <= current) {
                return type;
            }
        }

        return types.peek();
    }

    public StatOption getRandomStatOption (List<StatOption> options) {
        int totalWeight = 1;

        for (StatOption option: options) {
            totalWeight += (int) Math.max(option.weight + player.getLuck()*option.luckChange, 0); //Increases the total weight for everything in the list
        }

        int randNum = random.nextInt(totalWeight); //Makes a random number that is less than the weight
        int currentWeight = 0;
        for (StatOption option: options) { //Goes through everything in the list again and
            // if the random number becomes less than the current weight it means that it was selected
            currentWeight += (int) Math.max(option.weight + player.getLuck()*option.luckChange, 0);
            if(randNum <= currentWeight) {
                return option;
            }
        }
        return options.get(0); //If it didn't work for some reason

    }
    private float calculateMaxHPWidth() {
        int maxHPWidth = 40;
        float maxHPslope = 0.01f;
        return (float) ((maxHPWidth)/(1+Math.pow(3, -maxHPslope*(player.getMaxHP()-100))));
    }
    private float calculateMaxStmWidth() {
        int maxSTMWidth = 40;
        float maxSTMslope = 0.01f;
        return (float) ((maxSTMWidth)/(1+Math.pow(3, -maxSTMslope*(player.getMaxStamina()-100))));
    }

    private void levelUp() {
        player.gainXp(-player.getXpToLevelUp());
        player.levelUp();
        canUnPause = false;
        isNotPaused = false;
        levelUp = true;
        statOptions.clear();
        player.setMaxStamina(player.getMaxStamina()+10);
        player.setStaminaRegen(player.getStaminaRegen()+0.5f);
        stmBarTotalWidth = calculateMaxStmWidth();
        List<StatOption> statOptionsList = new StatOptions(player).statOptionList;
        while (statOptions.size < 5 ){
            StatOption randOption = getRandomStatOption(statOptionsList); //Gets a random option from the list
            if(!statOptions.contains(randOption,true)) { // If it doesnt already have the option
                statOptions.add(randOption); //Adds the option
            }
        }
    }

    public float getCritMulti() {
        float multiplier = 1;// Allows multiple crits based on crit chance (e.g., 2.5 means 2 full chances and 50% for a 3rd)

        int guaranteedCrits = (int) Math.floor(player.getCritChance()); //Guaranteed crits chance
        float partialCritChance = player.getCritChance() - guaranteedCrits;

        multiplier += player.getCritAmount()*guaranteedCrits;

        if (random.nextFloat() < partialCritChance) { //The chance one
            multiplier += player.getCritAmount();
        }

        return multiplier;
    }

}
