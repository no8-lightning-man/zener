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

import com.n8lm.zener.math.Vector3f;
import com.artemis.Entity;
import com.n8lm.zener.general.TransformComponent;
import com.n8lm.zener.map.Direction;
import com.n8lm.zener.map.Location;
import com.n8lm.zener.map.TiledMap;

/**
 * An animation controller for movement in tiled tiledMap
 * @author Alchemist
 *
 */
public class LocationAnimationController extends AnimationController<LocationKeyFrame> {

	TiledMap map;
	Entity mapObj;
	
	public LocationAnimationController(Animation<LocationKeyFrame> anim, TiledMap map, Entity mapObj) {
		super(anim);
		this.map = map;
		this.mapObj = mapObj;
	}

	@Override
	protected void process(int nowIndex, int nextIndex, Entity e) {
		
		LocationKeyFrame nowf = anim.getFrame(nowIndex);
		LocationKeyFrame nextf = anim.getFrame(nextIndex);
		
		Vector3f pos = e.getComponent(TransformComponent.class).getLocalTransform().getTranslation();
		pos.x = map.getTileWidth() * (nowf.loc.x);
		pos.y = map.getTileHeight() * (nowf.loc.y);
		pos.z = map.getTileAltitude() * (float) map.getAltitude(nowf.loc);
		
		Location trans = new Location(nextf.loc);
		trans.substract(nowf.loc);
		
		
		Direction dir = Direction.getDirection(trans);

		e.getComponent(TransformComponent.class).getLocalTransform().getRotation().fromAngleAxis((float)Math.toRadians(-(90 * dir.id - 180)), new Vector3f(0.0f, 0.0f, 1.0f));
		
		float totalTime = nextf.getTime() - nowf.getTime();
		float nowTime = time - nowf.getTime();
		

		if (totalTime == 0)
			totalTime = 1;
		
		float ratio = nowTime / totalTime;
		
		pos.x += ((float)map.getTileWidth() * trans.x) * ratio;
		pos.y += ((float)map.getTileHeight() * trans.y) * ratio;
		pos.z += (float) (map.getAltitude(nextf.loc) - map.getAltitude(nowf.loc)) * ratio;
	
	}
}
