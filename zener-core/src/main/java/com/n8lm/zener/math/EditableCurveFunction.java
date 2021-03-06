package com.n8lm.zener.math;

/**
 * Created on 2014/11/8.
 *
 * @author Alchemist
 */
public class EditableCurveFunction extends EditableCurve2f implements CurveFunction {

    public EditableCurveFunction() {
        super();
    }

    public void verticalLineCheck(int from, int to) {
        if (from < 0)
            from = 0;
        if (to >= anchors.size())
            to = anchors.size() - 1;
        for (int i = from; i <= to; i++) {
            CurveAnchor2f anchor = anchors.get(i);
            if (anchor.getControl1().x > anchor.getPoint().x)
                anchor.getControl1().x = anchor.getPoint().x;
            if (anchor.getControl2().x < anchor.getPoint().x)
                anchor.getControl2().x = anchor.getPoint().x;
        }

        for (int i = from; i <= to - 1; i++) {
            if (anchors.get(i).getControl2().x > anchors.get(i + 1).getPoint().x) {
                anchors.get(i).getControl2().x = anchors.get(i + 1).getPoint().x;
                //anchors.get(i).balanceControl1();
            }
            if (anchors.get(i + 1).getControl1().x < anchors.get(i).getPoint().x) {
                anchors.get(i + 1).getControl1().x = anchors.get(i).getPoint().x;
                //anchors.get(i + 1).balanceControl2();
            }
        }
    }

    public void setAnchorPoint(int i, Vector2f p) {
        if (0 <= i && i < anchors.size()) {
            p.subtractLocal(anchors.get(i).getPoint());
            anchors.get(i).addLocal(p);

            if (0 <= i - 1 && anchors.get(i).getPoint().x < anchors.get(i - 1).getPoint().x + 0.01f) {
                p.set(anchors.get(i - 1).getPoint().x + 0.01f, anchors.get(i).getPoint().y);
                p.subtractLocal(anchors.get(i).getPoint());
                anchors.get(i).addLocal(p);
                //anchors.get(i).getPoint().x = anchors.get(i - 1).getPoint().x + 0.01f;
            }
            if (i + 1 < anchors.size() && anchors.get(i).getPoint().x > anchors.get(i + 1).getPoint().x - 0.01f) {
                p.set(anchors.get(i + 1).getPoint().x - 0.01f, anchors.get(i).getPoint().y);
                p.subtractLocal(anchors.get(i).getPoint());
                anchors.get(i).addLocal(p);
            }
            calculate(i - 1, i + 1);
            verticalLineCheck(i - 1, i + 1);
        }
    }

    public void setAnchorControl1(int i, Vector2f cp) {
        if (0 <= i && i < anchors.size()) {
            anchors.get(i).getControl1().set(cp);
            //anchors.get(i).balanceControl2();
            calculate(i - 1, i + 1);
            verticalLineCheck(i - 1, i + 1);
        }
    }

    public void setAnchorControl2(int i, Vector2f cp) {
        if (0 <= i && i < anchors.size()) {
            anchors.get(i).getControl2().set(cp);
            //anchors.get(i).balanceControl1();
            calculate(i - 1, i + 1);
            verticalLineCheck(i - 1, i + 1);
        }
    }

    @Override
    public void addAnchor(CurveAnchor2f anchor ,EditableAnchorData data) {
        if (anchor.getControl1().x > anchor.getPoint().x)
            anchor.getControl1().x = anchor.getPoint().x * 2 - anchor.getControl1().x;
        if (anchor.getControl2().x < anchor.getPoint().x)
            anchor.getControl2().x = anchor.getPoint().x * 2 - anchor.getControl2().x;

        int i;
        int segment = getSegment(anchor.getPoint().x);
        if (segment != -1)
            i = segment + 1;
        else if (!anchors.isEmpty() && anchor.getPoint().x < anchors.get(0).getPoint().x)
            i = 0;
        else
            i = anchors.size();

        anchors.add(i, anchor);
        editableData.add(i, data);
        calculate(i - 1, i + 1);
        verticalLineCheck(i - 1, i + 1);
    }

    public float getTfromX(float x) {
        CurveSegment2D curve = getCurvefromX(x);
        return curve.solveTfromX(x, 1e-6f);
    }

    public ReadonlyCurveFunction generateReadonly() {
        return new ReadonlyCurveFunction(this.anchors);
    }

/*
while (Math.Abs(x - x0) > 0.0001)
{
    if (itr++ > maxIteration)
    {
        return NoSolution;
    }
    x0 = x;
    func = 0; dFunc = 0;
    for (int i = 0; i < coefficient.Count; i++)
    {
        func += coefficient[i] * Math.Pow(x, coefficient.Count-1 - i);
    }
    for (int i = 0; i < dCoeff.Count; i++)
        dFunc += dCoeff[i] * Math.Pow(x, dCoeff.Count-1 - i);

    if (dFunc != 0)
        x = x - func / dFunc;
    else if (func < 0.0001)
        return x;
    else
        x += 1;
}*/
}
