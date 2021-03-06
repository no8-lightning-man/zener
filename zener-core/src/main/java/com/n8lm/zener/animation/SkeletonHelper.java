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

import java.util.List;

import com.n8lm.zener.math.Matrix4f;
import com.n8lm.zener.math.Transform;

/**
 * A Helper class for Skeleton Calculation
 * @author Alchemist
 *
 */
public class SkeletonHelper {


	public static void calcTransformMatrix(List<Transform> poses, Skeleton skeleton, final Matrix4f[] m4a) {

		List<Joint> joints = skeleton.getJoints();
		for (int i = 0; i < skeleton.totalJoints(); i ++) {

			m4a[i] = new Matrix4f();
			m4a[i].setTransform(poses.get(i));
			
			if (joints.get(i).parent >= 0)
				joints.get(joints.get(i).parent).base.mult(m4a[i], m4a[i]);
			
			m4a[i].mult(joints.get(i).inverseBase, m4a[i]);
		}
		
		for (int i = 0; i < m4a.length; i ++) {
			if (joints.get(i).parent >= 0)
				m4a[joints.get(i).parent].mult(m4a[i], m4a[i]);
		}
	}
}
