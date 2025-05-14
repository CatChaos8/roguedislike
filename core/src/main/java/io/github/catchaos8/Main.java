package io.github.catchaos8;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    SpriteBatch batch;
    FitViewport viewport;
    float velocityX;
    float velocityY;
    float acceleration = 10f;       // How quickly you speed up
    float friction = 6f;           // How quickly you slow down

    boolean isNotPaused = true;
    private boolean canUnPause = true;

    Texture clearing;
    TextureRegion tiledRegion;

    PlayerInfo player;
    Texture playerT;
    Sprite playerS;
    Texture healthBarFullTextureCenter;
    Texture healthBarFullTextureLeftCorner;
    Texture healthBarFullTextureRightCorner;
    Texture healthBarEmptyTextureCenter;
    Texture healthBarEmptyTextureLeftCorner;
    Texture healthBarEmptyTextureRightCorner;


    Array<Bullet> bullets;
    Animation<TextureRegion> bulletAnimation;
    Texture playerBullet;

    Array<Enemy> enemies;
    Texture enemyTexture;
    Array<EnemyType> enemyTypes = new Array<>();

    float shotCD = 0;

    Texture crosshairTexture;
    Sprite crosshairSprite;

    OrthographicCamera uiCamera;

    boolean levelUp = false;
    Texture levelUpSelectionBG;



    @Override
    public void create() {
        Gdx.input.setCursorCatched(true);
        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(displayMode);

        clearing = new Texture("grassBackground.png");
        clearing.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat); //Makes it tile
        tiledRegion = new TextureRegion(clearing);

        playerT = new Texture("player.png");
        playerS = new Sprite(playerT);
        playerS.setSize(0.45f, 0.45f);

        healthBarFullTextureLeftCorner = new Texture("healthbars/healthBarFullLeftCorner.png");
        healthBarFullTextureRightCorner = new Texture("healthbars/healthBarFullRightCorner.png");
        healthBarFullTextureCenter = new Texture("healthbars/healthBarFullCenter.png");
        healthBarEmptyTextureCenter = new Texture("healthbars/healthBarEmptyCenter.png");
        healthBarEmptyTextureRightCorner = new Texture("healthbars/healthBarEmptyRightCorner.png");
        healthBarEmptyTextureLeftCorner = new Texture("healthbars/healthBarEmptyLeftCorner.png");


        batch = new SpriteBatch();
        viewport = new FitViewport(10, 6);

        player = new PlayerInfo();

        playerBullet = new Texture("playerProjectile.png");

        // SET UP ANIMATION
        TextureRegion[][] bulletFrames = TextureRegion.split(playerBullet, 15, 15);
        bulletAnimation = new Animation<>(
            0.04f,  // Frame duration
            bulletFrames[0]
        );

        enemyTexture = new Texture("enemy.png"); // Add your enemy texture
        enemies = new Array<>();
        enemyTypes.add(new EnemyType(100, 10, 1, 0.25f, 25, 20, enemyTexture)); //Normal
        enemyTypes.add(new EnemyType(70, 5, 1.3f, 0.15f, 40, 15, enemyTexture)); //Fast
        enemyTypes.add(new EnemyType(25, 25, 0.65f, 0.5f, 50, 20, enemyTexture)); //Tank
        enemyTypes.add(new EnemyType(1, 250, 0.75f, 0.65f, 500, 50, enemyTexture)); //Miniboss


        bullets = new Array<>();

        //Crosshair
        crosshairTexture = new Texture("crosshair.png");
        crosshairSprite = new Sprite(crosshairTexture);
        crosshairSprite.setSize(0.2f, 0.2f);

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        levelUpSelectionBG = new Texture("levelUpBackground.png");

    }


    @Override
    public void render() {
        logic();
        inputs();
        draw();
    }


    private void inputs() {
        if(isNotPaused) {
            Gdx.input.setCursorCatched(true);
            float maxSpeed = player.getSpeed() / 2f;
            float delta = Gdx.graphics.getDeltaTime();

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

            //For arrow keys/wasd for moving
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                velocityX += acceleration * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                velocityX -= acceleration * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                velocityY += acceleration * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                velocityY -= acceleration * delta;
            }

            // Normalize the movement vector to prevent faster diagonal movement
            float movementLength = (float) Math.sqrt(velocityX * velocityX + velocityY * velocityY);
            if (movementLength > maxSpeed) {
                velocityX = (velocityX / movementLength) * maxSpeed;
                velocityY = (velocityY / movementLength) * maxSpeed;
            }

            // Apply velocity
            playerS.translate(velocityX * delta, velocityY * delta);

            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && shotCD >= 1f / player.getAttackSpeed()) {
                shotCD = 0;
                shoot();
            }
        } else {
            Gdx.input.setCursorCatched(false);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && canUnPause) {
            isNotPaused = !isNotPaused;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1920, 1080);
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }
    }


    private void logic() {
        if (isNotPaused) {
            shotCD += Gdx.graphics.getDeltaTime();
            player.addIFrames(Gdx.graphics.getDeltaTime());

            player.heal(Gdx.graphics.getDeltaTime() * player.getHpRegen());

            // Spawn 1 enemy every 2 seconds
            float enemySpawnTime = 2f;
            if (Math.random() < Gdx.graphics.getDeltaTime() * 1 / enemySpawnTime) {
                for (int i = 0; i < player.getLvl(); i++) {
                    if(enemies.size < 150) {
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

                if((enemy.x-playerX)*(enemy.x-playerX) + (enemy.y-playerY) + (enemy.y-playerY) > enemy.maxDist) {
                    enemies.removeIndex(i); //Despawns enemy if the player gets too far from it
                    spawnEnemy(); //Respawns a new enemy so that u cant just liek run aaway forever
                }
                enemy.update(Gdx.graphics.getDeltaTime(), playerX, playerY, playerBounds, player);
                enemy.doCollision(enemies);
                if (enemy.isDead()) { // Remove enemy if dead
                    enemies.removeIndex(i);
                    player.gainXp((int) enemy.xp);
                    while (player.getXp() >= player.getXpToLevelUp()) {
                        player.gainXp(-player.getXpToLevelUp());
                        player.levelUp();
                        canUnPause = false;
                        isNotPaused = false;
                        levelUp = true;
                    }
                }

            }

            // Handle bullet collisions with enemies
            handleBulletCollisions();

            if (player.getHp() <= 0) {
                Gdx.app.exit();
            }
            updateBullets(Gdx.graphics.getDeltaTime());
        }
    }

    // Handle bullet collision logic
    // In your Main class, in the handlaeBulletCollisions method:
    private void handleBulletCollisions() {
        for (int i = bullets.size - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            boolean bulletHit = false;

            // Iterate through the enemies to check for collisions
            for (Enemy enemy : enemies) {
                // Only check collision if the bullet hasn't hit this enemy yet this frame
                if (bullet.getBounds().overlaps(enemy.getBounds()) && !bullet.hitEnemies.contains(enemy, true)) { //If it hasnt hit the enemy
                    bullet.hitEnemies.add(enemy);  // Mark this enemy as hit so that it doesnt hit it again next frame

                    enemy.takeDamage(bullet.damage);  // Apply damage to enemy
                    player.heal(bullet.damage * player.getLifeSteal());

                    // If the bullet has no pierce, remove it
                    if (bullet.pierce <= 0) {
                        bullets.removeIndex(i);
                        break;  // Exit the loop after removing the bullet
                    } else {
                        // Reduce the pierce and continue
                        bullet.pierce = bullet.pierce - 1;
                    }

                    bulletHit = true;
                    break;  // Stop after the first enemy hit
                }
            }

            // If the bullet did not hit any enemy or has finished piercing, check if it should be removed
            if (!bulletHit && bullet.shouldRemove()) {
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

        // Scale camera offset by how far the mouse is from the player
        float offsetAmount = distance * 0.0f;

        float cameraX = playerX + dirX * offsetAmount;
        float cameraY = playerY + dirY * offsetAmount;

        // Set camera position
        if(isNotPaused) {
            viewport.getCamera().position.set(cameraX, cameraY, 0);
            viewport.getCamera().update();

            viewport.getCamera().update();
        }
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

        crosshairSprite.draw(batch);

        batch.end();

        float uiHeight = uiCamera.viewportHeight;
        float uiWidth = uiCamera.viewportWidth;
        uiCamera.update();
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        //Health Bar
        float hpBarFullSize = Math.min(player.getMaxHP()*4f, 1500);
        float hpBarFilledSize = (player.getHp()/player.getMaxHP()*hpBarFullSize); //gets the percent of hp and stuff
        batch.draw(healthBarEmptyTextureLeftCorner, 50, uiHeight-150, 50, 100);
        batch.draw(healthBarEmptyTextureCenter, 100, uiHeight-150, hpBarFullSize, 100);
        batch.draw(healthBarEmptyTextureRightCorner, 100+hpBarFullSize, uiHeight-150, 50, 100);
        if(player.getHp() > 0) {
            batch.draw(healthBarFullTextureLeftCorner, 50, uiHeight-150, 50, 100);
            batch.draw(healthBarFullTextureCenter, 100, uiHeight-150, hpBarFilledSize, 100);
            if (player.getHp() >= player.getMaxHP()) {
                batch.draw(healthBarFullTextureRightCorner, 100+hpBarFilledSize, uiHeight-150, 50, 100);
            }
        }

        if (levelUp) {
            float imageWidth = 280f*uiWidth/300;
            float imageHeight = 120f*uiWidth/300;

            float drawX = (uiWidth - imageWidth) / 2f;
            float drawY = (uiHeight - imageHeight) / 2f;

            batch.draw(levelUpSelectionBG, drawX, drawY, imageWidth, imageHeight);

        }

        batch.end();

    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerT.dispose();
        playerBullet.dispose();
        clearing.dispose();
        crosshairTexture.dispose();

        healthBarFullTextureCenter.dispose();
        healthBarFullTextureRightCorner.dispose();
        healthBarFullTextureLeftCorner.dispose();
        healthBarEmptyTextureCenter.dispose();
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
        float spreadDegrees = player.getBulletSpread();
        float spreadRadians = (float) Math.toRadians(spreadDegrees);

        // Random spread for each bullet
        for (int i = 0; i < player.getForwardShots(); i++) {
            shootInDirection(baseAngle, spreadRadians, playerX, playerY, bulletSize);
        }

        for (int i = 0; i < player.getBackwardsShots(); i++) {
            shootInDirection(baseAngle + (float) Math.PI, spreadRadians, playerX, playerY, bulletSize); // Shoot in the opposite direction
        }

        for (int i = 0; i < player.getLeftShots(); i++) {
            shootInDirection(baseAngle + (float) Math.PI / 2f, spreadRadians, playerX, playerY, bulletSize); // Shoot left
        }

        for (int i = 0; i < player.getRightShots(); i++) {
            shootInDirection(baseAngle - (float) Math.PI / 2f, spreadRadians, playerX, playerY, bulletSize); // Shoot right
        }
    }

    // Helper method to shoot in a specific direction with spread
    private void shootInDirection(float baseAngle, float spreadRadians, float playerX, float playerY, float bulletSize) {
        // Add random spread
        float randomSpread = (float) ((Math.random() - 0.5f) * spreadRadians);
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
        float currentSpeed = (float) Math.sqrt(bulletSpeedX*bulletSpeedX + bulletSpeedY * bulletSpeedY);
        float adjustedDistance;
        if (playerSpeedBoost > 1f && currentSpeed > 0.01f) {
            //If the player is moving, adjust the thing(If the player speed boost is rlly low, it makes your range really high(intended)
            adjustedDistance = player.getBulletDistance() * (currentSpeed / playerSpeedBoost) * 0.5f;
        } else {
            //Otherwise use normal
            adjustedDistance = player.getBulletDistance();
        }
        // Create new bullet
        Bullet newBullet = new Bullet(
            playerX, playerY,
            bulletSpeedX, bulletSpeedY,
            bulletSize,
            player.getBounce(), player.getPierce() + 1, bulletAnimation,
            player.getBulletDamage(),
            adjustedDistance
        );

        // Add bullet to the list
        bullets.add(newBullet);
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
        float buffer = 1f; // how far offscreen to spawn
        float spawnX = 0, spawnY = 0;
        float width = viewport.getWorldWidth();
        float height = viewport.getWorldHeight();
        float camX = viewport.getCamera().position.x;
        float camY = viewport.getCamera().position.y;

        int side = (int)(Math.random() * 4); // 0 = top, 1 = right, 2 = bottom, 3 = left

        switch (side) {
            case 0: // top
                spawnX = camX - width/2 + (float)Math.random() * width;
                spawnY = camY + height/2 + buffer;
                break;
            case 1: // right
                spawnX = camX + width/2 + buffer;
                spawnY = camY - height/2 + (float)Math.random() * height;
                break;
            case 2: // bottom
                spawnX = camX - width/2 + (float)Math.random() * width;
                spawnY = camY - height/2 - buffer;
                break;
            case 3: // left
                spawnX = camX - width/2 - buffer;
                spawnY = camY - height/2 + (float)Math.random() * height;
                break;
        }
        EnemyType type = getRandomEnemyType(enemyTypes);

        //Spawns enemy with the EnemyType stats
        enemies.add(new Enemy(spawnX, spawnY, type.health, type.damage, type.speed, type.texture, type.size, type.xp));
}
    private EnemyType getRandomEnemyType(Array<EnemyType> types) {//Gets a random enemy type based on weight
        float totalWeight = 0;
        for (EnemyType type : types) {
            totalWeight += type.weight;
        }

        float randomNum = (float) Math.random() * totalWeight;
        float current = 0;

        for (EnemyType type : types) {
            current += type.weight;
            if (randomNum <= current) {
                return type;
            }
        }

        return types.peek();
    }

    public void setCanUnPause(boolean canUnPause) {
        this.canUnPause = canUnPause;
    }



}
