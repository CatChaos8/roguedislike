package io.github.catchaos8;

public class PlayerInfo {
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

    private float lifeSteal;
    private float luck;

    private int maxStamina;
    private float stamina, staminaRegen;
    private boolean isExhausted;


    private int xp;
    private int lvl;

    private float iFrames;

    private boolean doCollision;


    public PlayerInfo() {
        this.forwardShots = 1;
        this.backwardsShots = 0;
        this.leftShots = 0;
        this.rightShots = 0;
        this.speed = 2.5f;

        this.attackSpeed = 1;
        this.playerAttackStmCost = 5f;

        this.maxHP = 100;
        this.hp = maxHP;
        this.hpRegen = 0.0f; //Per second
        this.lifeSteal = 0.0f;

        this.bounce = 0;
        this.pierce = 0;
        this.bulletSize = 2f;
        this.bulletSpeed = 2.5f;
        this.bulletDamage = 5f;
        this.bulletAccuracy = 0f;
        this.bulletKnockback = 1f;
        this.bulletDistance = 2.5f;

        this.luck = 0f;

        this.xp = 0;
        this.lvl = 1;

        this.maxStamina = 100;
        this.stamina = maxStamina;
        this.staminaRegen = 10f; //per second

        this.iFrames = 0;
        this.doCollision = true;
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
        return (int) (100 + (lvl - 1) * 25 + (10*Math.pow(lvl, 3.0)));
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
        if(!this.isExhausted) {
            this.stamina = Math.min(this.stamina + amount, this.maxStamina);
        } else {
            this.stamina = Math.min(this.stamina + amount*3, this.maxStamina);
        }
        if(this.stamina >= this.maxStamina) {
            this.isExhausted = false;
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
}
