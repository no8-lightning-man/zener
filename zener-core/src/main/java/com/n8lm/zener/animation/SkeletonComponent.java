/*
 * This file is part of Zener.
 *
 * Zener is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * Zener is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with Zener.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.n8lm.zener.animation;

import java.util.ArrayList;
import com.n8lm.zener.math.Matrix4f;
import com.n8lm.zener.math.Transform;
import com.artemis.Component;

/**
 * A component store a skeleton and current poses of bones in the skeleton
 * @author Alchemist
 *
 */
public class SkeletonComponent extends Component {

	private final Matrix4f[] poseMatrices;
	private Skeleton baseSkeleton;
	
	public SkeletonComponent(Skeleton baseSkeleton) {
		this.baseSkeleton = baseSkeleton;
		
		ArrayList<Transform> poses = new ArrayList<Transform>();
		for (int i = 0; i < baseSkeleton.totalJoints(); i ++)
			poses.add(new Transform(baseSkeleton.getJoints().get(i).pose));

        poseMatrices = new Matrix4f[baseSkeleton.totalJoints()];
	    SkeletonHelper.calcTransformMatrix(poses, baseSkeleton, poseMatrices);
		
	}

    public SkeletonComponent(SkeletonComponent clone) {
        this.baseSkeleton = clone.baseSkeleton;
        this.poseMatrices = clone.poseMatrices;
    }
	
	public void setToBasePoses() {
		
		ArrayList<Transform> poses = new ArrayList<Transform>();
		for (int i = 0; i < baseSkeleton.totalJoints(); i ++)
			poses.add(new Transform(baseSkeleton.getJoints().get(i).pose));
		
		/*poseMatrices = */SkeletonHelper.calcTransformMatrix(poses, baseSkeleton, poseMatrices);
	}

	public Matrix4f[] getCurrentPosesMatrices() {
		return poseMatrices;
	}
	
	public Skeleton getBaseSkeleton() {
		return baseSkeleton;
	}

    public void setCurrentPosesMatrices(Matrix4f[] poseMatrices) {
        for (int i = 0; i < poseMatrices.length; i ++) {
            this.poseMatrices[i] = poseMatrices[i];
        }
    }

    public void setCurrentPosesMatrices(Matrix4f[] poseMatrices1, Matrix4f[] poseMatrices2, float delta) {
        for (int i = 0; i < poseMatrices.length; i ++) {
            this.poseMatrices[i] = poseMatrices1[i].interpolate(poseMatrices2[i], delta);
        }
    }

	/*public void setCurrentPosesMatrices(Matrix4f[] poseMatrices) {
		this.poseMatrices = poseMatrices;
	}*/
}
