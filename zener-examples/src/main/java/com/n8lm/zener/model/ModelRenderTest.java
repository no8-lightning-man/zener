package com.n8lm.zener.model;

import com.artemis.Entity;
import com.artemis.World;
import com.n8lm.zener.ExampleBasicApp;
import com.n8lm.zener.app.AppContainer;
import com.n8lm.zener.app.AppStateManager;
import com.n8lm.zener.app.BasicApp;
import com.n8lm.zener.general.TransformComponent;
import com.n8lm.zener.general.TreeAttachSystem;
import com.n8lm.zener.general.ZenerException;
import com.n8lm.zener.graphics.*;
import com.n8lm.zener.graphics.geom.ModelGeometry;
import com.n8lm.zener.graphics.material.NormalMaterial;
import com.n8lm.zener.math.MathUtil;
import com.n8lm.zener.math.Transform;
import com.n8lm.zener.math.Vector3f;
import com.n8lm.zener.script.Event;
import com.n8lm.zener.script.GlobalScriptSystem;
import com.n8lm.zener.script.NativeScript;
import com.n8lm.zener.script.ScriptComponent;

/**
 * Created on 2014/7/4.
 * @author Alchemist
 */
public class ModelRenderTest extends ExampleBasicApp implements NativeScript {

    public ModelRenderTest() {
        super("modelrender", "Model Render");
    }

    private Entity model;
    private Entity model2;
    private Entity cam;
    private Entity light1, light2;


    @Override
    public void attached(AppStateManager appStateManager) {

    }

    @Override
    public void detached(AppStateManager appStateManager) {

    }

    @Override
    public void initialize(AppStateManager appStateManager, BasicApp app) {
        super.initialize(appStateManager, app);

        world.setSystem(new GlobalScriptSystem());
        world.setSystem(new TreeAttachSystem());
        world.setSystem(new GLRenderSystem(world));

        world.initialize();
        // add notorious suzanne model entity
        model = world.createEntity();
        model.addComponent(new GeometryComponent(new ModelGeometry("suzanne", resourceManager.getModel("suzanne").getMesh()), false));
        model.addComponent(new MaterialComponent(new NormalMaterial(resourceManager.getModel("suzanne").getMaterial()), false));
        model.addComponent(new TransformComponent(new Transform(2, 0, 0)));
        model.addComponent(new ScriptComponent(Event.WORLD_UPDATE, this));
        world.addEntity(model);

        // add cube with normal mapping
        model2 = world.createEntity();
        model2.addComponent(new GeometryComponent(new ModelGeometry("cube", resourceManager.getModel("cube").getMesh()), false));
        model2.addComponent(new MaterialComponent(new NormalMaterial(resourceManager.getModel("cube").getMaterial()), false));
        model2.addComponent(new TransformComponent(new Transform(-2, 0, 0)));
        world.addEntity(model2);

        // add camera entity
        cam = world.createEntity();
        cam.addComponent(new ViewComponent(new PerspectiveProjection()));
        Transform camTransform = new Transform(0, -3, 0);
        camTransform.getRotation().lookAt(new Vector3f(0, 3, 0), new Vector3f(0, 0, 3));
        cam.addComponent(new TransformComponent(camTransform));
        world.addEntity(cam);

        // add first light entity
        LightComponent lc1 = new LightComponent();
        lc1.setDiffuse(new Vector3f(0.8f, 0.8f, 0.8f));
        light1 = world.createEntity();
        light1.addComponent(lc1);
        light1.addComponent(new TransformComponent(new Transform()));
        world.addEntity(light1);

        // add first light entity
        LightComponent lc2 = new LightComponent();
        lc2.setDiffuse(new Vector3f(0.6f, 0.6f, 0.6f));
        light2 = world.createEntity();
        light2.addComponent(lc2);
        light2.addComponent(new TransformComponent(new Transform()));
        world.addEntity(light2);

        inputManager.addListener(new MaterialSwitchInputAdapter(model, light2));
    }

    @Override
    public void cleanup() {

    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private int timer = 0;

    @Override
    public void run(World world, Event event) {
        if (event.getType() == Event.WORLD_UPDATE) {
            timer++;
            float angle = timer / 10.0f / 180f * MathUtil.PI;
            // apply transform
            model.getComponent(TransformComponent.class).getLocalTransform().getRotation()
                    .lookAt(Vector3f.UNIT_Z, new Vector3f(MathUtil.cos(angle), MathUtil.sin(angle), 0));
            light1.getComponent(TransformComponent.class).getLocalTransform().getTranslation()
                    .set(0, MathUtil.cos(angle * 5) * 5, MathUtil.sin(angle * 5) * 5);
            light2.getComponent(TransformComponent.class).getLocalTransform().getTranslation()
                    .set(MathUtil.cos(angle * 10) * 5, 0, MathUtil.sin(angle * 10) * 5);
        }
    }

    public static void main(String[] args) throws ZenerException {
        ModelRenderTest game = new ModelRenderTest();
        AppContainer container = new AppContainer(game);
        container.setDisplayMode(800, 600, false);
        container.setAlwaysRender(true);
        //container.setVSync(true);
        container.setTargetFrameRate(60);
        container.start();
    }
}
