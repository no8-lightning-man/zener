package com.n8lm.zener.math;

import java.io.Serializable;

/**
 * Created on 2014/11/8.
 *
 * @author Alchemist
 */
public class CurveAnchor2f implements Serializable {
    private final Vector2f point;
    private final Vector2f control1;
    private final Vector2f control2;

    public CurveAnchor2f(Vector2f point) {
        this(point, new Vector2f(point.getX() - 1f, point.getY()), new Vector2f(point.getX() + 1f, point.getY()));
    }

    public CurveAnchor2f(Vector2f point, Vector2f control1, Vector2f control2) {
        this.point = point;
        this.control1 = control1;
        this.control2 = control2;
    }

    public Vector2f getPoint() {
        return point;
    }

    public Vector2f getControl1() {
        return control1;
    }

    public Vector2f getControl2() {
        return control2;
    }

    public void setPoint(Vector2f point) {
        this.point.set(point);
    }

    public void setControl1(Vector2f control1) {
        this.control1.set(control1);
    }

    public void setControl2(Vector2f control2) {
        this.control2.set(control2);
    }

    public Vector2f interpolate(CurveAnchor2f anchor, float changeAmt, Vector2f store) {
        return MathUtil.interpolateBezier(changeAmt, point, control2, anchor.control1, anchor.point, store);
    }

    public static Vector2f interpolate(CurveAnchor2f anchor1, CurveAnchor2f anchor2, float changeAmt, Vector2f store) {
        return MathUtil.interpolateBezier(changeAmt, anchor1.point, anchor1.control2, anchor2.control1, anchor2.point, store);
    }
}
