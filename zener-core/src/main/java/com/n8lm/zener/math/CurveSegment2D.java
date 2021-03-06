package com.n8lm.zener.math;

/**
 * Created on 2014/11/9.
 *
 * @author Alchemist
 */
public class CurveSegment2D {

    protected float ax,bx,cx,dx;
    protected float ay,by,cy,dy;

    public CurveSegment2D() {

    }

    public CurveSegment2D(CurveAnchor2f anchor1, CurveAnchor2f anchor2) {
        set(anchor1, anchor2);
    }

    public void set(CurveAnchor2f anchor1, CurveAnchor2f anchor2) {
        Vector2f p0, p1, p2, p3;

        p0 = anchor1.getPoint();
        p1 = anchor1.getControl2();
        p2 = anchor2.getControl1();
        p3 = anchor2.getPoint();
        //this.cx = 3.0 * p1x;
        //this.bx = 3.0 * (p2x - p1x) - this.cx;
        //this.ax = 1.0 - this.cx -this.bx;
        ax = -p0.x + 3*p1.x - 3*p2.x + p3.x;
        bx = 3*p0.x - 6*p1.x + 3*p2.x;
        cx = -3*p0.x + 3*p1.x;
        dx = p0.x;

        ay = -p0.y + 3*p1.y - 3*p2.y + p3.y;
        by = 3*p0.y - 6*p1.y + 3*p2.y;
        cy = -3*p0.y + 3*p1.y;
        dy = p0.y;
    }

    public float solveTfromX(float x, float epsilon) {
        float t0;
        float t1;
        float t2;
        float x2;
        float d2;
        int i;

        float tt = (x - dx) / (sampleX(1.0f) - dx);

        //System.out.println(tt);
        // First try a few iterations of Newton's method -- normally very fast.
        for (t2 = tt, i = 0; i < 8; i++) {
            x2 = this.sampleX(t2) - x;
            if (Math.abs (x2) < epsilon)
                return t2;
            d2 = this.sampleDerivativeX(t2);
            if (Math.abs(d2) < epsilon)
                break;
            t2 = t2 - x2 / d2;
            //System.out.println("t2 = " + t2);
        }

        // No solution found - use bi-section
        t0 = 0.0f;
        t1 = 1.0f;
        t2 = tt;
        //System.out.println(tt);

        if (t2 < t0) return t0;
        if (t2 > t1) return t1;

        while (t0 < t1) {
            x2 = this.sampleX(t2);
            if (Math.abs(x2 - x) < epsilon)
                return t2;
            if (x > x2) t0 = t2;
            else t1 = t2;

            t2 = (t1 - t0) * .5f + t0;
            //System.out.println("t2 = " + t2);
        }

        // Give up
        return t2;
    }

    private float sampleDerivativeX(float t) {
        return (3.0f * this.ax * t + 2.0f * this.bx) * t + this.cx;
    }

    private float sampleDerivativeY(float t) {
        return (3.0f * this.ay * t + 2.0f * this.by) * t + this.cy;
    }

    public Range getYBound(Range r) {
        r.l = Math.min(sampleY(0), sampleY(1));
        r.u = Math.max(sampleY(0), sampleY(1));

        float a, b, c, delta, t, s1, s2;
        a = 3.0f * this.ay;
        b = 2.0f * this.by;
        c = this.cy;
        delta = b * b - 4 * a * c;

        if (delta >= 0) {
            t = MathUtil.sqrt(delta);
            s1 = (b + t) / (-2 * a);
            s2 = (b - t) / (-2 * a);

            if (s1 > 0 && s1 < 1.0f) {
                r.l = Math.min(sampleY(s1), r.l);
                r.u = Math.max(sampleY(s1), r.u);
            }
            if (s2 > 0 && s2 < 1.0f) {
                r.l = Math.min(sampleY(s2), r.l);
                r.u = Math.max(sampleY(s2), r.u);
            }
        }
        return r;
    }

    public Range getXBound(Range r) {
        r.l = Math.min(sampleX(0), sampleX(1));
        r.u = Math.max(sampleX(0), sampleX(1));

        float a, b, c, delta, t, s1, s2;
        a = 3.0f * this.ax;
        b = 2.0f * this.bx;
        c = this.cx;
        delta = b * b - 4 * a * c;

        if (delta >= 0) {
            t = MathUtil.sqrt(delta);
            s1 = (b + t) / (-2 * a);
            s2 = (b - t) / (-2 * a);

            if (s1 > 0 && s1 < 1.0f) {
                r.l = Math.min(sampleX(s1), r.l);
                r.u = Math.max(sampleX(s1), r.u);
            }
            if (s2 > 0 && s2 < 1.0f) {
                r.l = Math.min(sampleX(s2), r.l);
                r.u = Math.max(sampleX(s2), r.u);
            }
        }
        return r;
    }

    public float sampleX(float t) {
        return ((this.ax * t + this.bx) * t + this.cx) * t + this.dx;
    }

    public float sampleY(float t) {
        return ((this.ay * t + this.by) * t + this.cy) * t + this.dy;
    }
}
