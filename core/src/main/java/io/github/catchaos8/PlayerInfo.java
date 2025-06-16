package io.github.catchaos8;

import com.badlogic.gdx.math.Vector2;

public class PlayerInfo {
    private float survivedTime;

    private float x, y;
    private float size;

    private Vector2 playerKB;

    private float speed;

    private float attackSpeed;
    private float playerAttackStmCost;

    private int maxHP;
    private float hp;
    private float hpRegen;


    private int forwardShots;
    private int backwardsShots;
    private int leftShots;
    private int rightShots;

    private int pierce, bounce;
    private float bulletSize, bulletSpeed, bulletDamage, bulletAccuracy, bulletDistance, bulletKnockback;
    private float critChance, critAmount;

    private float lifeSteal;
    private float luck;

    private int maxStamina;
    private float stamina, staminaRegen;
    private boolean isExhausted;


    private int xp;
    private int lvl;

    private float iFrames;

    private boolean doCollision;

    boolean noStmRegenBoost;


    public PlayerInfo() {
        this.x = 0;
        this.y = 0;

        this.size = 0.45f;

        this.forwardShots = 1;
        this.backwardsShots = 0;
        this.leftShots = 0;
        this.rightShots = 0;
        this.speed = 2.5f;

        this.attackSpeed = 1;
        this.playerAttackStmCost = 5f;

        this.maxHP = 100;
        this.hp = maxHP;
        this.hpRegen = 0; //Per second
        this.lifeSteal = 0.0f;

        this.bounce = 0;
        this.pierce = 0;
        this.bulletSize = 2f;
        this.bulletSpeed = 2.5f;
        this.bulletDamage = 5f;
        this.bulletAccuracy = 0f;
        this.bulletKnockback = 1f;
        this.bulletDistance = 2.5f;

        this.critChance = 0.05f; //Chance to crit(Can be over 100, and crit twice)
        this.critAmount = 0.50f;//% Increase in damage on crits

        this.luck = 0f;

        this.xp = 0;
        this.lvl = 1;

        this.maxStamina = 100;
        this.stamina = maxStamina;
        this.staminaRegen = 15f; //per second

        this.iFrames = 0;
        this.doCollision = true;

        this.playerKB = new Vector2(0, 0);

        noStmRegenBoost = false;
    }

    // XP & Leveling
    public int getXp() {
        return xp;
    }

    public int getLvl() {
        return lvl;
    }

    public void gainXp(int amount) {
        xp += amount;
    }

    public void levelUp() {
        lvl++;
    }

    public int getXpToLevelUp() {
        return (int) (100*Math.pow(1.5, (this.lvl - 1)));
    }

    // Other Getters/Setters...
    public int getForwardShots() { return forwardShots; }
    public void setForwardShots(int forwardShots) { this.forwardShots = forwardShots; }

    public int getBackwardsShots() { return backwardsShots; }
    public void setBackwardsShots(int backwardsShots) { this.backwardsShots = backwardsShots; }

    public int getLeftShots() { return leftShots; }
    public void setLeftShots(int leftShots) { this.leftShots = leftShots; }

    public int getRightShots() { return rightShots; }
    public void setRightShots(int rightShots) { this.rightShots = rightShots; }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }

    public float getAttackSpeed() { return attackSpeed; }
    public void setAttackSpeed(float attackSpeed) { this.attackSpeed = attackSpeed; }

    public int getMaxHP() { return maxHP; }
    public void setMaxHP(int maxHP) { this.maxHP = maxHP; }

    public float getHp() { return hp; }
    public void setHp(float hp) {
        this.hp = Math.max(0, Math.min(hp, maxHP));
    }
    public void dealDamage(float amount) {
        this.hp = this.hp - amount;
    }
    public void heal(float amount) {
        this.hp = Math.min(this.hp + amount, this.maxHP);
    }

    public float getLifeSteal() {
        return lifeSteal;
    }
    public void setLifeSteal(float amount) {
        this.lifeSteal = amount;
    }

    public int getBounce() { return bounce; }
    public void setBounce(int bounce) { this.bounce = bounce; }

    public int getPierce() { return pierce; }
    public void setPierce(int pierce) { this.pierce = pierce; }

    public float getBulletSize() { return bulletSize; }
    public void setBulletSize(float bulletSize) { this.bulletSize = bulletSize; }

    public float getBulletSpeed() { return bulletSpeed; }
    public void setBulletSpeed(float bulletSpeed) { this.bulletSpeed = bulletSpeed; }

    public float getBulletDamage() {return bulletDamage;}
    public void setBulletDamage(float amount) {this.bulletDamage = amount;}

    public float getBulletAccuracy() {return bulletAccuracy;}
    public void setBulletAccuracy(float bulletAccuracy) {this.bulletAccuracy = bulletAccuracy;}

    public float getBulletDistance() {return bulletDistance;}
    public void setBulletDistance(float amount) {this.bulletDistance = amount;}

    public float getBulletKnockback() {return bulletKnockback;}
    public void setBulletKnockback(float amount) {this.bulletKnockback=amount;}

    public float getHpRegen() { return hpRegen; }
    public void setHpRegen(float hpRegen) { this.hpRegen = hpRegen; }

    public float getLuck() {return luck;}
    public void setLuck(float luck) {this.luck = luck;}

    public float getSize() {
        return size;
    }
    public void setSize(float size) {
        this.size = size;
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "PlayerInfo{" +
            "forwardShots=" + forwardShots +
            ", backwardsShots=" + backwardsShots +
            ", leftShots=" + leftShots +
            ", rightShots=" + rightShots +
            ", speed=" + speed +
            ", attackSpeed=" + attackSpeed +
            ", maxHP=" + maxHP +
            ", hp=" + hp +
            ", hpRegen=" + hpRegen +
            ", stamina=" + stamina +
            ", maxStamina=" + maxStamina +
            ", staminaRegen=" + staminaRegen +
            ", isExhausted=" + isExhausted +
            ", bounce=" + bounce +
            ", pierce=" + pierce +
            ", bulletSize=" + bulletSize +
            ", bulletSpeed=" + bulletSpeed +
            ", bulletDamage=" + bulletDamage +
            ", bulletAccuracy=" + bulletAccuracy +
            ", bulletDistance=" + bulletDistance +
            ", bulletKnockback=" + bulletKnockback +
            ", lifeSteal=" + lifeSteal +
            ", xp=" + xp +
            ", lvl=" + lvl +
            ", luck=" + luck +
            ", iFrames=" + iFrames +
            ", doCollision=" + doCollision +
            '}';
    }


    public void addIFrames(float amount) {
        this.iFrames += amount;
    }

    public float getiFrames(){
        return this.iFrames;
    }
    public void setiFrames(float amount) {
        this.iFrames = amount;
    }

    public boolean isDoCollision() {
        return this.doCollision;
    }
    public void setDoCollision(boolean doCollision) {
        this.doCollision = doCollision;
    }

    public float getStamina() {
        return stamina;
    }

    public float getStaminaRegen() {
        return staminaRegen;
    }

    public int getMaxStamina() {
        return maxStamina;
    }

    public void setStaminaRegen(float staminaRegen) {
        this.staminaRegen = staminaRegen;
    }

    public void setStamina(float stamina) {
        this.stamina = stamina;
    }

    public void setMaxStamina(int maxStamina) {
        this.maxStamina = maxStamina;
    }

    public boolean spendStamina(float amount) {
        if (this.stamina >= amount && !this.isExhausted) {
            this.stamina -= amount;
            return true;
        } else if(!this.isExhausted){
            this.stamina = 0;
            this.isExhausted = true;
            return true;
        }
        return false;
    }

    public void regenStamina(float amount) {
        if(this.doCollision) {
            if (!this.isExhausted || !this.noStmRegenBoost) {
                this.stamina = Math.min(this.stamina + amount, this.maxStamina);
            } else {
                this.stamina = Math.min(this.stamina + amount * 3, this.maxStamina);
            }
            if (this.stamina >= this.maxStamina) {
                this.isExhausted = false;
            }
        }
    }

    public boolean isExhausted() {
        return isExhausted;
    }

    public void setExhausted(boolean exhausted) {
        isExhausted = exhausted;
    }

    public float getPlayerAttackStmCost() {
        return playerAttackStmCost;
    }

    public void setPlayerAttackStmCost(float playerAttackStmCost) {
        this.playerAttackStmCost = playerAttackStmCost;
    }

    public float getCritAmount() {
        return critAmount;
    }

    public float getCritChance() {
        return critChance;
    }

    public void setCritAmount(float critAmount) {
        this.critAmount = critAmount;
    }

    public void setCritChance(float critChance) {
        this.critChance = critChance;
    }

    public Vector2 getPlayerKB() {
        return playerKB;
    }

    public void setPlayerKB(Vector2 playerKB) {
        this.playerKB = playerKB;
    }

    public void applyKB(float kbStrength, Vector2 source) {
        float deltaX = (x + size/2f) - source.x;
        float deltaY = (y + size/2f) - source.y;
        float distFromAttack = (float) Math.sqrt(deltaX*deltaX + deltaY*deltaY);

        if(distFromAttack != 0) {
            deltaX /= distFromAttack;
            deltaY /= distFromAttack;

            playerKB.x = deltaX*kbStrength;
            playerKB.y = deltaY*kbStrength;
        }
    }

    public boolean isNoStmRegenBoost() {
        return noStmRegenBoost;
    }

    public void setNoStmRegenBoost(boolean noStmRegenBoost) {
        this.noStmRegenBoost = noStmRegenBoost;
    }

    public float getSurvivedTime() {
        return survivedTime;
    }

    public void setSurvivedTime(float survivedTime) {
        this.survivedTime = survivedTime;
    }

    public String getSurvivedTimeString() {
        return
            String.format("%02d:%02d", (int) Math.floor(survivedTime / 60), (int)(survivedTime % 60)); //Displays time in minutes/seconds;
    }


    public String getString() {


        return "Player Stats:\n" +
            "Level: " + lvl + "\n" +
            "XP: " + xp + " / " + getXpToLevelUp() + "\n" +
            "Survived Time: " + String.format("%02d:%02d", (int) Math.floor(survivedTime / 60), (int)(survivedTime % 60)) + "\n\n" + //Displays time in minutes/seconds

        "Max HP: " + maxHP + " (Regen: " + hpRegen + "/s)\n" +
            "Stamina: " + (int) stamina + " / " + maxStamina +
            " (Regen: " + staminaRegen + "/s" + ")\n\n" +

            "Movement Speed: " + speed + "\n" +
            "Attack Speed: " + attackSpeed + "x\n" +
            "Attack Stamina Cost: " + playerAttackStmCost + "\n" +
//            "Lifesteal: " + (int)(lifeSteal * 100) + "%\n\n" +

            "Crit Chance: " + (int)(critChance * 100) + "%\n" +
            "Crit Damage Bonus: " + (int)(critAmount * 100) + "%\n" +
            "Luck: " + (int) luck + "\n\n" +

            "Bullet Damage: " + bulletDamage*10 + "\n" +
            "Bullet Speed: " + bulletSpeed + "\n" +
            "Bullet Size: " + bulletSize + "\n" +
            "Bullet Accuracy: " + bulletAccuracy + "\n" +
            "Bullet Distance: " + bulletDistance + "\n" +
            "Bullet Knockback: " + bulletKnockback + "\n" +
            "Pierce: " + pierce + ", Bounce: " + bounce + "\n\n" +

            "Shots - Forward: " + forwardShots + ", Backward: " + backwardsShots +
            ", Left: " + leftShots + ", Right: " + rightShots;
    }

}

