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

package com.n8lm.zener.graphics;

import java.util.HashMap;
import java.util.Map;

import com.n8lm.zener.graphics.geom.Geometry;

public class HardResourceManager{

	private Map<String, Geometry> geometryMap;
	private Map<Geometry, Integer> invokeCount;
	
	public HardResourceManager() {
		invokeCount = new HashMap<Geometry, Integer>();
		geometryMap = new HashMap<String, Geometry>();
	}

	public boolean hasGeometry(String name) {
		return geometryMap.containsKey(name);
	}
	
	/*public boolean hasGeometry(Geometry geometry) {
		return invokeCount.containsKey(geometry);
	}*/
	
	public Geometry getGeometry(String name) {
		Geometry geometry = geometryMap.get(name);
		incrementInvokeCount(geometry);
		return geometry;
	}

	public void registerGeometry(String name, Geometry geometry) {
		geometryMap.put(name, geometry);
		incrementInvokeCount(geometry);
	}
	
	private void incrementInvokeCount(Geometry geometry) {
		if (invokeCount.containsKey(geometry))
			invokeCount.put(geometry, invokeCount.get(geometry) + 1);
		else
			invokeCount.put(geometry, 1);
	}
	
	public void reduceInvokeCount(Geometry geometry) {
		invokeCount.put(geometry, invokeCount.get(geometry) - 1);
		if (invokeCount.get(geometry) == 0) {
			invokeCount.remove(geometry);
			geometry.deleteObject();
		}
	}
}