package io.github.catchaos8;

public class PlayerInfo {
    private int forwardShots;
    private int backwardsShots;
    private int leftShots;
    private int rightShots;
    private float speed;
    private float attackSpeed;
    private int maxHP;
    private float hp;
    private int bounce;
    private int pierce;
    private float bulletSize;
    private float bulletSpeed;
    private float hpRegen;
    private float bulletDamage;
    private float bulletSpread; // in degrees
    private float bulletDistance;
    private float bulletKnockback;
    private float lifeSteal;

    private int xp;
    private int lvl;

    private float iFrames;

    public PlayerInfo() {
        this.forwardShots = 1;
        this.backwardsShots = 0;
        this.leftShots = 0;
        this.rightShots = 0;
        this.speed = 2.5f;
        this.attackSpeed = 1;

        this.maxHP = 100;
        this.hp = maxHP;
        this.hpRegen = 0.0f;
        this.lifeSteal = 0.00f;

        this.bounce = 0;
        this.pierce = 0;
        this.bulletSize = 2f;
        this.bulletSpeed = 2.5f;
        this.bulletDamage = 5f;
        this.bulletSpread = 0f;
        this.bulletKnockback = 1f;
        this.bulletDistance = 2.5f;

        this.xp = 0;
        this.lvl = 1;
        this.iFrames = 0;
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
        hp +=10;
        maxHP +=10;
        attackSpeed +=1;
        lifeSteal += 0.01f;
        System.out.println("Levelup" + lvl);
    }

    public int getXpToLevelUp() {
        return 100 + (lvl - 1) * 25;
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

    public float getBulletSpread() {return bulletSpread;}
    public void setBulletSpread(float bulletSpread) {this.bulletSpread = bulletSpread;}

    public float getBulletDistance() {return bulletDistance;}
    public void setBulletDistance(float amount) {this.bulletDistance = amount;}

    public float getBulletKnockback() {return bulletKnockback;}
    public void setBulletKnockback(float amount) {this.bulletKnockback=amount;}

    public float getHpRegen() { return hpRegen; }
    public void setHpRegen(float hpRegen) { this.hpRegen = hpRegen; }

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
            ", bounce=" + bounce +
            ", pierce=" + pierce +
            ", bulletSize=" + bulletSize +
            ", bulletSpeed=" + bulletSpeed +
            ", bulletSpread=" + bulletSpread +
            ", xp=" + xp +
            ", lvl=" + lvl +
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

}
