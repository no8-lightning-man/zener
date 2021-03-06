package com.n8lm.zener.ranger;

import com.artemis.Component;
import com.artemis.Entity;
import com.n8lm.zener.math.Vector3f;

/**
 * Created on 2014/7/11.
 *
 * @author Alchemist
 */
public class CharacterComponent extends Component {
    private int level;
    private int exp;
    private float maxhp;

    enum Action {
        Idle,
        Run,
        RunWithArrow,
        Bow,
        Shoot
    }

    private Action action;
    private int actionTime;
    private float actionPower;
    //private Weapon weapon;
    transient private Entity weaponEntity;
    private AbilityData abilityData;

    private final float[] headAngles;
    private final Vector3f movement;
    private final Vector3f accDir;
    private float health;


    public CharacterComponent(float maxhp, AbilityData abilityData) {
        this.level = 1;
        this.exp = 0;
        this.health = this.maxhp = maxhp;
        this.abilityData = abilityData;
        this.headAngles = new float[3];
        this.movement = new Vector3f();
        this.accDir = new Vector3f();

        this.action = Action.Idle;
        this.actionTime = 0;
        this.actionPower = 0.0f;
    }

    @Override
    public String toString() {
        return String
                .format("[Level = %d, EXP = %d, Weapon = %s, AbilityData = %s, HP = %f/%f]",
                        level, exp,
                        weaponEntity.toString(),
                        abilityData.toString(),
                        health, maxhp);
    }


    public Vector3f getAccDir() {
        return accDir;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public AbilityData getAbilityData() {
        return abilityData;
    }

    public int getExp() {
        return exp;
    }

    public void obtainExp(int exp) {
        this.exp += exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getLevelExp() {
        return level * 10 + 90;
    }

    public Entity getWeaponEntity() {
        return weaponEntity;
    }

    public void setWeaponEntity(Entity weaponEntity) {
        this.weaponEntity = weaponEntity;
    }

    public float[] getHeadAngles() {
        return headAngles;
    }

    public Vector3f getMovement() {
        return movement;
    }
    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getActionTime() {
        return actionTime;
    }

    public void setActionTime(int actionTime) {
        this.actionTime = actionTime;
    }

    public float getActionPower() {
        return actionPower;
    }

    public void setActionPower(float actionPower) {
        this.actionPower = actionPower;
    }
}
