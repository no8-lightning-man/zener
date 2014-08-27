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

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.DelayedEntityProcessingSystem;

public class ExpirationSystem extends DelayedEntityProcessingSystem {

	@Mapper ComponentMapper<ExpireComponent> em;
	
	public ExpirationSystem() {
		super(Aspect.getAspectForAll(ExpireComponent.class));
	}

	@Override
	protected float getRemainingDelay(Entity e) {
		return em.get(e).getRemain() * 1000;
	}

	@Override
	protected void processDelta(Entity e, float accumulatedDelta) {
		em.get(e).substract(accumulatedDelta / 1000f);
    }

	@Override
	protected void processExpired(Entity e) {
		if(em.get(e).getExpireListener() != null)
			em.get(e).getExpireListener().endEvent(e);
		world.deleteEntity(e);
	}

}
