package io.github.catchaos8;

import java.util.Arrays;
import java.util.List;

public class StatOptions {
    PlayerInfo player;

    public StatOptions(PlayerInfo player) {
        this.player = player;
    }

    List<StatOption> statOptionList = Arrays.asList(
        //Common
        new StatOption(250, "Attack Speed",
            "Slightly increases your attack speed, and decreases the attack stamina cost",
            () -> {
                player.setAttackSpeed(Math.max(player.getAttackSpeed() + 0.5f, player.getAttackSpeed()*1.1f));
                player.setPlayerAttackStmCost(player.getPlayerAttackStmCost()*0.9f);
            }, -1),
        new StatOption(250, "Bullet Damage",
            "Slightly increases your bullet damage",
            () -> player.setBulletDamage(Math.max(player.getBulletDamage() + 2.5f, player.getBulletDamage()*1.1f)), -1),
        new StatOption(250, "Max Health",
            "Slightly increases your max health",
            () -> {
                float oldHP = player.getMaxHP();
                player.setMaxHP((int) Math.max(player.getMaxHP() + 10f, player.getMaxHP()*1.05f));
                player.heal(player.getMaxHP() - oldHP);
            }, -1),
        new StatOption(250, "Speed",
            "Slightly increases your speed",
            () -> player.setSpeed(Math.max(player.getSpeed() + 0.5f, player.getSpeed()*1.1f)), -1),
        new StatOption(250, "Pierce",
            "Increases your max pierce by 1",
            () -> player.setPierce(player.getPierce() + 1), -1),
        new StatOption(250, "Bounce",
            "Increases your max bounce by 1",
            () -> player.setBounce(player.getBounce() + 1), -1),
//        new StatOption(250, "Lifesteal",
//            "Slightly increases your lifesteal, but decreases your speed",
//            () -> {
//                player.setLifeSteal(Math.max(player.getLifeSteal() + 0.0025f, player.getLifeSteal()*1.05f));
//                player.setSpeed((player.getSpeed()*0.9f));
//            }, -1),
        new StatOption(250, "Bullet Size",
            "Slightly increases your bullet size",
            () -> player.setBulletSize(Math.max(player.getBulletSize() + 1f, player.getBulletSize()*1.2f)), -1
        ),
        new StatOption(250, "Bullet Distance",
            "Slightly increases your bullet's max distance",
            () -> player.setBulletDistance(Math.max(player.getBulletDistance() + 0.5f, player.getBulletDistance()*1.1f)), -1
        ),
        new StatOption(250, "Bullet Knockback",
            "Slightly increases your bullet's knockback",
            () -> player.setBulletKnockback(Math.max(player.getBulletKnockback() + 2f, player.getBulletKnockback()*1.2f)), -1
        ),
        new StatOption(250, "Bullet Speed",
            "Slightly increases your bullet's speed",
            () -> player.setBulletSpeed(Math.max(player.getBulletSpeed() + 0.5f, player.getBulletSpeed()*1.2f)), -1
        ),
        new StatOption(250, "Health Regen",
            "Slightly increases your health regen",
            () -> player.setHpRegen(Math.max(player.getHpRegen() + 0.25f, player.getHpRegen()*1.2f)), -1),
        new StatOption(200, "Luck",
            "Slightly increases your luck",
            () -> player.setLuck(Math.max(player.getLuck() + 1f, player.getLuck()*1.05f)), -1),
        new StatOption(250, "Stamina Regen",
            "Slightly increases your stamina regeneration",
            () -> player.setStaminaRegen(Math.max(player.getStaminaRegen() + 1.5f, player.getStaminaRegen() * 1.1f)), -1
        ),

        new StatOption(250, "Max Stamina",
            "Slightly increases your maximum stamina",
            () -> {
                player.setMaxStamina((int)Math.max(player.getMaxStamina() + 10f, player.getMaxStamina() * 1.025f));
            }, -1),
        //================================Uncommon=========================================//
        new StatOption(50, "Attack Speed",
            "Increases your attack speed, and decreases the attack stamina cost",
            () -> {
                player.setAttackSpeed(Math.max(player.getAttackSpeed() + 1f, player.getAttackSpeed()*1.1f));
                player.setPlayerAttackStmCost(player.getPlayerAttackStmCost()*0.9f);
            }, 0.5f),
        new StatOption(50, "Bullet Damage",
            "Increases your bullet damage",
            () -> player.setBulletDamage(Math.max(player.getBulletDamage() + 7.5f, player.getBulletDamage()*1.1f)), 0.5f),
        new StatOption(50, "Max Health",
            "Increases your max health",
            () -> {
                float oldHP = player.getMaxHP();
                player.setMaxHP((int) Math.max(player.getMaxHP() + 25f, player.getMaxHP()*1.2f));
                player.heal(player.getMaxHP() - oldHP);
            }, -1),
        new StatOption(50, "Speed",
            "Increases your speed",
            () -> player.setSpeed(Math.max(player.getSpeed() + 1f, player.getSpeed()*1.2f)), 0.5f),
        new StatOption(50, "Pierce",
            "Increases your max pierce by 2",
            () -> player.setPierce(player.getPierce() + 2), 0.5f),
        new StatOption(50, "Bounce",
            "Increases your max bounce by 2",
            () -> player.setBounce(player.getBounce() + 2), 0.5f),
//        new StatOption(50, "Lifesteal",
//                           "Increases your lifesteal, but decreases your speed",
//                           () ->  {
//                                player.setLifeSteal(Math.max(player.getLifeSteal() + 0.005f, player.getLifeSteal()*1.1f));
//                                player.setSpeed(player.getSpeed()*0.9f);
//                               hpBarTotalWidth = calculateMaxHPWidth();
//                            }, 0.5f),
        new StatOption(50, "Bullet Size",
            "Increases your bullet size",
            () -> player.setBulletSize(Math.max(player.getBulletSize() + 2.5f, player.getBulletSize()*1.1f)), 0.5f),
        new StatOption(50, "Bullet Distance",
            "Increases your bullet's max distance",
            () -> player.setBulletDistance(Math.max(player.getBulletDistance() + 1f, player.getBulletDistance()*1.1f)), 0.5f),
        new StatOption(50, "Bullet Knockback",
            "Increases your bullet's knockback",
            () -> player.setBulletKnockback(Math.max(player.getBulletKnockback() + 4f, player.getBulletKnockback()*1.1f)), 0.5f),
        new StatOption(50, "Bullet Speed",
            "Increases your bullet's speed",
            () -> player.setBulletSpeed(Math.max(player.getBulletSpeed() + 1f, player.getBulletSpeed()*1.1f)), 0.5f),
        new StatOption(50, "Health Regen",
            "Increases your health regen",
            () -> player.setHpRegen(Math.max(player.getHpRegen() + 0.75f, player.getHpRegen()*1.1f)), 0.5f),
        new StatOption(50, "Luck",
            "Increases your luck",
            () -> player.setLuck(Math.max(player.getLuck() + 2.5f, player.getLuck()*1.1f)), 0.5f),
        new StatOption(50, "Stamina Regen",
            "Increases your stamina regeneration",
            () -> player.setStaminaRegen(Math.max(player.getStaminaRegen() + 3f, player.getStaminaRegen() * 1.1f)), 0.5f),

        new StatOption(50, "Max Stamina",
            "Increases your maximum stamina",
            () -> {
                player.setMaxStamina((int)Math.max(player.getMaxStamina() + 25f, player.getMaxStamina() * 1.1f));
            }, 0.5f),
        // ================================ Rare Upgrades ================================ //
        new StatOption(25, "Attack Speed",
            "Greatly increases your attack speed, and decreases the attack stamina cost",
            () -> {
                player.setAttackSpeed(Math.max(player.getAttackSpeed() + 2f, player.getAttackSpeed() * 1.15f));
                player.setPlayerAttackStmCost(player.getPlayerAttackStmCost()*0.9f);
            }, 1.0f),
        new StatOption(25, "Bullet Damage",
            "Greatly increases your bullet damage",
            () -> player.setBulletDamage(Math.max(player.getBulletDamage() + 15f, player.getBulletDamage() * 1.15f)), 1.0f),
        new StatOption(25, "Max Health",
            "Greatly increases your max health",
            () -> {
                float oldHP = player.getMaxHP();
                player.setMaxHP((int)Math.max(player.getMaxHP() + 50f, player.getMaxHP() * 1.15f));
                player.heal(player.getMaxHP() - oldHP);
            }, 1.0f),
        new StatOption(25, "Speed",
            "Greatly increases your movement speed",
            () -> player.setSpeed(Math.max(player.getSpeed() + 2f, player.getSpeed() * 1.15f)), 1.0f),
        new StatOption(25, "Pierce",
            "Increases your max pierce by 3",
            () -> player.setPierce(player.getPierce() + 3), 1.0f),
        new StatOption(25, "Bounce",
            "Increases your max bounce by 3",
            () -> player.setBounce(player.getBounce() + 3), 1.0f),
//        new StatOption(25, "Lifesteal",
//            "Greatly increases your lifesteal, but decreases your speed",
//            () -> {
//            player.setLifeSteal(Math.max(player.getLifeSteal() + 0.0075f, player.getLifeSteal() * 1.15f));
//            player.setSpeed(player.getSpeed()*0.9f);
//            }, 1.0f),
        new StatOption(25, "Bullet Size",
            "Greatly increases your bullet size",
            () -> player.setBulletSize(Math.max(player.getBulletSize() + 4f, player.getBulletSize() * 1.15f)), 1.0f),
        new StatOption(25, "Bullet Distance",
            "Greatly increases your bullet max distance",
            () -> player.setBulletDistance(Math.max(player.getBulletDistance() + 2f, player.getBulletDistance() * 1.15f)), 1.0f),
        new StatOption(25, "Bullet Knockback",
            "Greatly increases your bullet knockback",
            () -> player.setBulletKnockback(Math.max(player.getBulletKnockback() + 8f, player.getBulletKnockback() * 1.15f)), 1.0f),
        new StatOption(25, "Bullet Speed",
            "Greatly increases your bullet speed",
            () -> player.setBulletSpeed(Math.max(player.getBulletSpeed() + 2f, player.getBulletSpeed() * 1.15f)), 1.0f),
        new StatOption(15, "Health Regen",
            "Greatly increases your health regeneration",
            () -> player.setHpRegen(Math.max(player.getHpRegen() + 1.5f, player.getHpRegen() * 1.15f)), 1.0f),
        new StatOption(25, "Luck",
            "Greatly increases your luck",
            () -> player.setLuck(Math.max(player.getLuck() + 5f, player.getLuck() * 1.15f)), 1.0f),
        new StatOption(15, "Front Bullet",
            "Increases your front bullet amount by 1, but decreases your accuracy",
            () -> {
                player.setForwardShots(player.getForwardShots() + 1);
                player.setBulletAccuracy(player.getBulletAccuracy() + 5);
            }, 1f),
        new StatOption(15, "Side Bullets",
            "Increases your side bullets amount by 1, but decreases your accuracy",
            () -> {
                player.setLeftShots(player.getLeftShots() + 1);
                player.setRightShots(player.getRightShots() + 1);
                player.setBulletAccuracy(player.getBulletAccuracy() + 10);
            }, 1f),
        new StatOption(15, "Back bullet",
            "Increases your back bullets amount by 1, but decreases your accuracy",
            () -> {
                player.setBackwardsShots(player.getBackwardsShots() + 1);
                player.setBulletAccuracy(player.getBulletAccuracy() + 5);
            }, 1f),
        new StatOption(25, "Stamina Regen",
            "Greatly increases your stamina regeneration",
            () -> player.setStaminaRegen(Math.max(player.getStaminaRegen() + 5f, player.getStaminaRegen() * 1.15f)), 1.0f),

        new StatOption(25, "Max Stamina",
            "Greatly increases your maximum stamina",
            () -> {
                player.setMaxStamina((int)Math.max(player.getMaxStamina() + 50f, player.getMaxStamina() * 1.15f));
            }, 1.0f),
        // ================================ Epic Upgrades ================================ //
        new StatOption(10, "Attack Speed",
            "Massively increases your attack speed, and decreases the attack stamina cost",
            () -> {
                player.setAttackSpeed(Math.max(player.getAttackSpeed() + 4f, player.getAttackSpeed() * 1.2f));
                player.setPlayerAttackStmCost(player.getPlayerAttackStmCost()*0.9f);
            }, 2.0f),
        new StatOption(10, "Bullet Damage",
            "Massively increases your bullet damage",
            () -> player.setBulletDamage(Math.max(player.getBulletDamage() + 30f, player.getBulletDamage() * 1.2f)), 2.0f),
        new StatOption(10, "Max Health",
            "Massively increases your max health",
            () -> {
                float oldHP = player.getMaxHP();
                player.setMaxHP((int)Math.max(player.getMaxHP() + 100f, player.getMaxHP() * 1.2f));
                player.heal(player.getMaxHP() - oldHP);
            }, 2.0f),
        new StatOption(10, "Speed",
            "Massively increases your movement speed",
            () -> player.setSpeed(Math.max(player.getSpeed() + 4f, player.getSpeed() * 1.2f)), 2.0f),
        new StatOption(10, "Pierce",
            "Increases your max pierce by 5",
            () -> player.setPierce(player.getPierce() + 5), 2.0f),
        new StatOption(10, "Bounce",
            "Increases your max bounce by 5",
            () -> player.setBounce(player.getBounce() + 5), 2.0f),
//        new StatOption(10, "Lifesteal",
//            "Massively increases your lifesteal, but decreases your speed",
//            () -> {
//                player.setLifeSteal(Math.max(player.getLifeSteal() + 0.01f, player.getLifeSteal() * 1.2f));
//                player.setSpeed(player.getSpeed()*0.f);
//            }, 2.0f),
        new StatOption(10, "Bullet Size",
            "Massively increases your bullet size",
            () -> player.setBulletSize(Math.max(player.getBulletSize() + 6f, player.getBulletSize() * 1.2f)), 2.0f),
        new StatOption(10, "Bullet Distance",
            "Massively increases your bullet's max distance",
            () -> player.setBulletDistance(Math.max(player.getBulletDistance() + 4f, player.getBulletDistance() * 1.2f)), 2.0f),
        new StatOption(10, "Bullet Knockback",
            "Massively increases your bullet's knockback",
            () -> player.setBulletKnockback(Math.max(player.getBulletKnockback() + 16f, player.getBulletKnockback() * 1.2f)), 2.0f),
        new StatOption(10, "Bullet Speed",
            "Massively increases your bullet's speed",
            () -> player.setBulletSpeed(Math.max(player.getBulletSpeed() + 4f, player.getBulletSpeed() * 1.2f)), 2.0f),
        new StatOption(5, "Health Regen",
            "Massively increases your health regeneration",
            () -> player.setHpRegen(Math.max(player.getHpRegen() + 3f, player.getHpRegen() * 1.2f)), 2.0f),
        new StatOption(10, "Luck",
            "Massively increases your luck",
            () -> player.setLuck(Math.max(player.getLuck() + 10f, player.getLuck() * 1.2f)), 2.0f),
        new StatOption(5, "Front Bullet",
            "Increases your front bullet amount by 3, but decreases your accuracy",
            () -> {
                player.setForwardShots(player.getForwardShots() + 3);
                player.setBulletAccuracy(player.getBulletAccuracy() + 7);
            }, 2f),
        new StatOption(7, "Side Bullets",
            "Increases your side bullets amount by 3, but decreases your accuracy",
            () -> {
                player.setLeftShots(player.getLeftShots() + 3);
                player.setRightShots(player.getRightShots() + 3);
                player.setBulletAccuracy(player.getBulletAccuracy() + 15);
            }, 2f),
        new StatOption(10, "Back bullet",
            "Increases your back bullets amount by 3, but decreases your accuracy",
            () -> {
                player.setBackwardsShots(player.getBackwardsShots() + 3);
                player.setBulletAccuracy(player.getBulletAccuracy() + 7);
            }, 2f),
        new StatOption(15, "Stamina Regen",
            "Massively increases your stamina regeneration",
            () -> player.setStaminaRegen(Math.max(player.getStaminaRegen() + 10f, player.getStaminaRegen() * 1.2f)), 2f),

        new StatOption(15, "Max Stamina",
            "Massively increases your maximum stamina",
            () -> {
                player.setMaxStamina((int)Math.max(player.getMaxStamina() + 100f, player.getMaxStamina() * 1.2f));
            }, 2f)
    ) ;
}
