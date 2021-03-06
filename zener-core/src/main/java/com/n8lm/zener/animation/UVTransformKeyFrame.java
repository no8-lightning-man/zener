package com.n8lm.zener.animation;

import com.n8lm.zener.math.Vector2f;

/**
 * It stores a vector2d that represents the UV Mapping transform.
 * @see com.n8lm.zener.animation.UVTransformAnimationController
 *
 * Created on 2014/9/8.
 * @author Alchemist
 */
public class UVTransformKeyFrame extends  KeyFrame {

    Vector2f uvTransform;

    public UVTransformKeyFrame(Vector2f uvTransform, float time) {
        super(time);
        this.uvTransform = uvTransform;
    }
}
