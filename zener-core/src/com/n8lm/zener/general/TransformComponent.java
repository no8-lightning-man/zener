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

package com.n8lm.zener.general;

import java.util.List;

import com.n8lm.zener.animation.Joint;
import com.n8lm.zener.animation.SkeletonComponent;
import com.n8lm.zener.math.Quaternion;
import com.n8lm.zener.math.Transform;
import com.n8lm.zener.math.Vector3f;
import com.artemis.Component;
import com.artemis.Entity;
import com.n8lm.zener.utils.ZenerException;

public class TransformComponent extends Component {
	
	private Entity attached;
	private int joint = -1;
	final private Transform worldTransform;
	final private Transform localTransform;
	
	public TransformComponent(Vector3f trans, Quaternion rot, Vector3f scale) {
		this(null, trans, rot, scale);
	}

	public TransformComponent(Transform transform) {
		this(null, transform);
	}

	public TransformComponent(Entity attached, Transform transform) {
		this(attached, null, transform);
	}
	
	public TransformComponent(Entity attached, Vector3f trans, Quaternion rot, Vector3f scale) {
		this(attached, null, trans, rot, scale);
	}
	
	public TransformComponent(Entity attached, String bone, Transform transform) {
		joint = -1;
        try {
            setAttached(attached, bone);
        } catch (ZenerException e) {
            e.printStackTrace();
        }
        worldTransform = new Transform();
		localTransform = new Transform(transform);
	}
	
	public TransformComponent(Entity attached, String bone, Vector3f trans, Quaternion rot, Vector3f scale) {
		joint = -1;
        try {
            setAttached(attached, bone);
        } catch (ZenerException e) {
            e.printStackTrace();
        }
		worldTransform = new Transform();
		localTransform = new Transform(trans, rot, scale);
	}
	
	
	public Transform getWorldTransform() {
		return worldTransform;
	}

	public Transform getLocalTransform() {
		return localTransform;
	}

	public Entity getAttached() {
		return attached;
	}

	public void setAttached(Entity attached) {
		if (attached != null && attached.getComponent(TransformComponent.class) == null)
			throw new IllegalArgumentException("attached entity has no Tranform component");
		this.attached = attached;
	}
	

	public void setAttached(Entity attached, String bone) throws ZenerException {
		if (bone != null)
			if (attached != null && attached.getComponent(SkeletonComponent.class) == null)
				throw new ZenerException("attached entity has no Skeleton component");
			else {
				List<Joint> joints = attached.getComponent(SkeletonComponent.class).getBaseSkeleton().getJoints();
				int i;
				for (i = 0; i < joints.size(); i ++)
					if (joints.get(i).name.equals(bone)) {
						joint = i;
						break;
					}
				if (i == joints.size())	{
					joint = -1;
					throw new ZenerException("attached entity has no this bone in Skeleton component");
				}
			}
		setAttached(attached);
	}

	public int getJoint() {
		return joint;
	}
}