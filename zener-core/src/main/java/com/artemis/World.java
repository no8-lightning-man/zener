package com.artemis;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import com.artemis.annotations.Mapper;
import com.artemis.utils.ArrayBag;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

/**
 * The primary instance for the framework. It contains all the managers.
 * 
 * You must use this to create, delete and retrieve entities.
 * 
 * It is also important to set the delta each game loop iteration, and initialize before game loop.
 * 
 * @author Arni Arent
 * 
 */
public class World {
	private EntityManager em;
	private ComponentManager cm;

    private String name;
    private boolean isEnable;

	public float delta;
	private Bag<Entity> added;
	private Bag<Entity> changed;
	private Bag<Entity> deleted;
	private Bag<Entity> enable;
	private Bag<Entity> disable;

	private Map<Class<? extends Manager>, Manager> managers;
	private Bag<Manager> managersBag;
	
	private Map<Class<?>, EntitySystem> systems;
	private Bag<EntitySystem> systemsBag;

	public World(String name) {
        this.name = name;
        this.isEnable = true;

		managers = new HashMap<>();
		managersBag = new ArrayBag<>();
		
		systems = new HashMap<>();
		systemsBag = new ArrayBag<>();

		added = new ArrayBag<>();
		changed = new ArrayBag<>();
		deleted = new ArrayBag<>();
		enable = new ArrayBag<>();
		disable = new ArrayBag<>();

		cm = new ComponentManager();
		setManager(cm);
		
		em = new EntityManager();
		setManager(em);
	}

	
	/**
	 * Makes sure all managers systems are initialized in the order they were added.
	 */
	public void initialize() {
		for (int i = 0; i < managersBag.size(); i++) {
			managersBag.get(i).initialize();
		}
		
		for (int i = 0; i < systemsBag.size(); i++) {
			ComponentMapperInitHelper.config(systemsBag.get(i), this);
			systemsBag.get(i).initialize();
		}
	}

	/**
	 * Destroy the World by deleting all the entities in it
	 */
	public void destroy() {
		Bag<Entity> entities = em.getActiveEntities();
		for (int i = 0; i < entities.size(); i++)
			if (entities != null)
				deleteEntity(entities.get(i));
	}
	
	
	/**
	 * Returns a manager that takes care of all the entities in the world.
	 * entities of this world.
	 * 
	 * @return entity manager.
	 */
	public EntityManager getEntityManager() {
		return em;
	}
	
	/**
	 * Returns a manager that takes care of all the components in the world.
	 * 
	 * @return component manager.
	 */
	public ComponentManager getComponentManager() {
		return cm;
	}
	
	
	

	/**
	 * Add a manager into this world. It can be retrieved later.
	 * World will notify this manager of changes to entity.
	 * 
	 * @param manager to be added
	 */
	public <T extends Manager> T setManager(T manager) {
		managers.put(manager.getClass(), manager);
		managersBag.add(manager);
		manager.setWorld(this);
		return manager;
	}

	/**
	 * Returns a manager of the specified type.
	 * 
	 * @param <T>
	 * @param managerType
	 *            class type of the manager
	 * @return the manager
	 */
	public <T extends Manager> T getManager(Class<T> managerType) {
		return managerType.cast(managers.get(managerType));
	}
	
	/**
	 * Deletes the manager from this world.
	 * @param manager to delete.
	 */
	public void deleteManager(Manager manager) {
		managers.remove(manager);
		managersBag.remove(manager);
	}

	
	
	
	/**
	 * Time since last game loop.
	 * 
	 * @return delta time since last game loop.
	 */
	public float getDelta() {
		return delta;
	}

	/**
	 * You must specify the delta for the game here.
	 * 
	 * @param delta time since last game loop.
	 */
	public void setDelta(float delta) {
		this.delta = delta;
	}
	


	/**
	 * Adds a entity to this world.
	 * 
	 * @param e entity
	 */
	public void addEntity(Entity e) {
		added.add(e);
	}
	
	/**
	 * Ensure all systems are notified of changes to this entity.
	 * If you're adding a component to an entity after it's been
	 * added to the world, then you need to invoke this method.
	 * 
	 * @param e entity
	 */
	public void changedEntity(Entity e) {
		changed.add(e);
	}
	
	/**
	 * Delete the entity from the world.
	 * 
	 * @param e entity
	 */
	public void deleteEntity(Entity e) {
		if (!deleted.contains(e)) {
			deleted.add(e);
		}
	}

	/**
	 * (Re)enable the entity in the world, after it having being disabled.
	 * Won't do anything unless it was already disabled.
	 */
	public void enable(Entity e) {
		enable.add(e);
	}

	/**
	 * Disable the entity from being processed. Won't delete it, it will
	 * continue to exist but won't get processed.
	 */
	public void disable(Entity e) {
		disable.add(e);
	}


	/**
	 * Create and return a new or reused entity instance.
	 * Will NOT add the entity to the world, use World.addEntity(Entity) for that.
	 * 
	 * @return entity
	 */
	public Entity createEntity() {
		return em.createEntityInstance();
	}

	/**
	 * Get a entity having the specified id.
	 * 
	 * @param entityId
	 * @return entity
	 */
	public Entity getEntity(int entityId) {
		return em.getEntity(entityId);
	}

	


	/**
	 * Gives you all the systems in this world for possible iteration.
	 * 
	 * @return all entity systems in world.
	 */
	public ImmutableBag<EntitySystem> getSystems() {
		return systemsBag;
	}

	/**
	 * Adds a system to this world that will be processed by World.process()
	 * 
	 * @param system the system to add.
	 * @return the added system.
	 */
	public <T extends EntitySystem> T setSystem(T system) {
        system.setWorld(this);

        systems.put(system.getClass(), system);
		systemsBag.add(system);

        return system;
	}

	/**
	 * Will add a system to this world.
	 *  
	 * @param system the system to add.
	 * @param passive wether or not this system will be processed by World.process()
	 * @return the added system.
	 */
	public <T extends EntitySystem> T setSystem(T system, boolean passive) {
		system.setPassive(passive);

		return setSystem(system);
	}
	
	/**
	 * Removed the specified system from the world.
	 * @param system to be deleted from world.
	 */
	public void deleteSystem(EntitySystem system) {
		systems.remove(system.getClass());
		systemsBag.remove(system);
	}
	
	private void notifySystems(Performer performer, Entity e) {
		for(int i = 0, s=systemsBag.size(); s > i; i++) {
			performer.perform(systemsBag.get(i), e);
		}
	}

	private void notifyManagers(Performer performer, Entity e) {
		for(int a = 0; managersBag.size() > a; a++) {
			performer.perform(managersBag.get(a), e);
		}
	}
	
	/**
	 * Retrieve a system for specified system type.
	 * 
	 * @param type type of system.
	 * @return instance of the system in this world.
	 */
	public <T extends EntitySystem> T getSystem(Class<T> type) {
		return type.cast(systems.get(type));
	}

	
	/**
	 * Performs an action on each entity.
	 * @param entities
	 * @param performer
	 */
	private void check(Bag<Entity> entities, Performer performer) {
		if (!entities.isEmpty()) {
			for (int i = 0; entities.size() > i; i++) {
				Entity e = entities.get(i);
				notifyManagers(performer, e);
				notifySystems(performer, e);
			}
			entities.clear();
		}
	}

	
	/**
	 * Process all non-passive systems.
	 */
	public void process() {
		check(added, new Performer() {
			@Override
			public void perform(EntityObserver observer, Entity e) {
				observer.added(e);
			}
		});
		
		check(changed, new Performer() {
			@Override
			public void perform(EntityObserver observer, Entity e) {
				observer.changed(e);
			}
		});
		
		check(disable, new Performer() {
			@Override
			public void perform(EntityObserver observer, Entity e) {
				observer.disabled(e);
			}
		});
		
		check(enable, new Performer() {
			@Override
			public void perform(EntityObserver observer, Entity e) {
				observer.enabled(e);
			}
		});
		
		check(deleted, new Performer() {
			@Override
			public void perform(EntityObserver observer, Entity e) {
				observer.deleted(e);
			}
		});
		
		cm.clean();
		
		for(int i = 0; systemsBag.size() > i; i++) {
			EntitySystem system = systemsBag.get(i);
			if(!system.isPassive()) {
				system.process();
			}
		}
	}
	

	/**
	 * Retrieves a ComponentMapper instance for fast retrieval of components from entities.
	 * 
	 * @param type of component to get mapper for.
	 * @return mapper for specified component type.
	 */
	public <T extends Component> ComponentMapper<T> getMapper(Class<T> type) {
		return ComponentMapper.getFor(type, this);
	}

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	/*
     * Only used internally to maintain clean code.
     */
	private interface Performer {
		void perform(EntityObserver observer, Entity e);
	}

	
	
	private static class ComponentMapperInitHelper {

		public static void config(Object target, World world) {
			try {
				Class<?> clazz = target.getClass();
				for (Field field : clazz.getDeclaredFields()) {
					Mapper annotation = field.getAnnotation(Mapper.class);
					if (annotation != null && Mapper.class.isAssignableFrom(Mapper.class)) {
						ParameterizedType genericType = (ParameterizedType) field.getGenericType();
						Class componentType = (Class) genericType.getActualTypeArguments()[0];

						field.setAccessible(true);
						field.set(target, world.getMapper(componentType));
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("Error while setting component mappers", e);
			}
		}

	}

}
