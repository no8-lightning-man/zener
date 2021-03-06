package com.artemis.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ArrayBag;
import com.artemis.utils.ImmutableBag;

/**
 * A typical entity system. Use this when you need to process entities possessing the
 * provided component types.
 * 
 * @author Arni Arent
 *
 */
public abstract class EntityProcessingSystem extends EntitySystem {

    private ArrayBag<Entity> actives;

	public EntityProcessingSystem(Aspect aspect) {
		super(aspect);
        actives = new ArrayBag<>();
	}

	/**
	 * Process a entity this system is interested in.
	 * @param e the entity to process.
	 */
	protected abstract void process(Entity e);

	@Override
	protected final void processEntities() {
		for (int i = 0, s = actives.size(); s > i; i++) {
			process(actives.get(i));
		}
	}
	
	@Override
	protected boolean checkProcessing() {
		return true;
	}

    @Override
    protected void inserted(Entity e) {
        super.inserted(e);
        actives.add(e);
    }

    @Override
    protected void removed(Entity e) {
        super.removed(e);
        actives.remove(e);
    }

    public ImmutableBag<Entity> getActives() {
        return actives;
    }
}
