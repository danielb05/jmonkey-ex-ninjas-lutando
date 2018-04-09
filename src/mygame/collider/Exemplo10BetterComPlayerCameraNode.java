package mygame.collider;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.Random;

//To use the example assets in a new jMonkeyPlatform project, right-click your project, select "Properties", go to "Libraries", press "Add Library" and add the "jme3-test-data" library.
/**
 * Aula 7
 *
 * Dois ninjas caminham ate se encontrarem, ai atacam e um deles morre, o
 * vencedor dança.
 */
public class Exemplo10BetterComPlayerCameraNode
        extends SimpleApplication
        implements ActionListener, PhysicsCollisionListener, AnimEventListener {

    public static void main(String[] args) {
        Exemplo10BetterComPlayerCameraNode app = new Exemplo10BetterComPlayerCameraNode();
        app.showSettings = false;
        app.start();
    }
    private AnimChannel channel1, channel2;
    private AnimControl control1, control2;
    private BulletAppState bulletAppState;
    private boolean walk = true, attack = false, win = false, lose = false;
    private Material boxMatColosion;
    private Node ninja1;
    private Node ninja2;
    public int count = 3;
    private Vector3f walk1 = new Vector3f(-1.5f, 0, 0);
    private Vector3f walk2 = new Vector3f(1.5f, 0, 0);
    private Vector3f walk3 = new Vector3f(0, 0, 0);
    private Vector3f vec1 = new Vector3f(FastMath.PI  - FastMath.PI / 2, 0 ,0);
    private Vector3f vec2 = new Vector3f(-FastMath.PI  - FastMath.PI / 2, 0 ,0);
    private BetterCharacterControl boxPhysicsNode1;
    private BetterCharacterControl boxPhysicsNode2;
    

    @Override
    public void simpleInitApp() {

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
      

        createLigth();
        createCity();

        boxPhysicsNode1 = new BetterCharacterControl(0.5f, 2.5f, 16f);
        createNinja1();
                
        boxPhysicsNode2 = new BetterCharacterControl(0.5f, 2.5f, 16f);
        createNinja2();
        
        boxMatColosion = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        boxMatColosion.setBoolean("UseMaterialColors", true);
        boxMatColosion.setColor("Ambient", ColorRGBA.Red);
        boxMatColosion.setColor("Diffuse", ColorRGBA.Red);


        bulletAppState.setDebugEnabled(true);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
        bulletAppState.getPhysicsSpace().add(boxPhysicsNode1);
        bulletAppState.getPhysicsSpace().add(boxPhysicsNode2);
    }

    @Override
    public void simpleUpdate(float ftp) {
        if (!walk && !attack) {
            boxPhysicsNode1.setWalkDirection(walk3);
            boxPhysicsNode2.setWalkDirection(walk3);
            channel1.setAnim("Attack1", 0.005f);
            channel1.setLoopMode(LoopMode.Loop);
            channel1.setSpeed(1f);
            channel2.setAnim("Attack2", 0.005f);
            channel2.setLoopMode(LoopMode.Loop);
            channel2.setSpeed(1f);
            attack = true;
        }

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {

    }

    
    private void createNinja1() {
        ninja1 = (Node) assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
        ninja1.setName("ninja1");
        ninja1.setLocalScale(0.015f);
        
        ninja1.setLocalTranslation(8, 2, 0);
        rootNode.attachChild(ninja1);
        control1 = ninja1.getControl(AnimControl.class);
        control1.addListener(this);
        

        for (String anim : control1.getAnimationNames()) {
            System.out.println(anim);
        }
        
        ninja1.addControl(boxPhysicsNode1);
        boxPhysicsNode1.setWalkDirection(walk1);
        boxPhysicsNode1.setViewDirection(vec1);

        channel1 = control1.createChannel();
        channel1.setAnim("Walk", 0.005f);
        channel1.setLoopMode(LoopMode.Loop);
    }
    
    private void createNinja2() {
        ninja2 = (Node) assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
        ninja1.setName("ninja2");
        ninja2.setLocalScale(0.015f);
        ninja2.setLocalTranslation(-8, 2, 0);
        rootNode.attachChild(ninja2);
        
        control2 = ninja2.getControl(AnimControl.class);
        control2.addListener(this);
        boxPhysicsNode2.setWalkDirection(walk2);
        boxPhysicsNode2.setViewDirection(vec2);
        ninja2.addControl(boxPhysicsNode2);

        channel2 = control2.createChannel();
        channel2.setAnim("Walk", 0.005f);
        channel2.setLoopMode(LoopMode.Loop);
    }
    
    
    private void createLigth() {

        DirectionalLight l1 = new DirectionalLight();
        l1.setDirection(new Vector3f(1, -0.7f, 0));
        rootNode.addLight(l1);

        DirectionalLight l2 = new DirectionalLight();
        l2.setDirection(new Vector3f(-1, 0, 0));
        rootNode.addLight(l2);

        DirectionalLight l3 = new DirectionalLight();
        l3.setDirection(new Vector3f(0, 0, -1.0f));
        rootNode.addLight(l3);

        DirectionalLight l4 = new DirectionalLight();
        l4.setDirection(new Vector3f(0, 0, 1.0f));
        rootNode.addLight(l4);

        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);

    }

    private void createCity() {
        assetManager.registerLocator("town.zip", ZipLocator.class);
        Spatial scene = assetManager.loadModel("main.scene");
        scene.setLocalTranslation(0, -5.2f, 0);
        rootNode.attachChild(scene);

        RigidBodyControl cityPhysicsNode = new RigidBodyControl(CollisionShapeFactory.createMeshShape(scene), 0);
        scene.addControl(cityPhysicsNode);
        bulletAppState.getPhysicsSpace().add(cityPhysicsNode);
    }

    
    @Override
    public void collision(PhysicsCollisionEvent event) {
        if(walk)
        {
            if (event.getNodeA().getName().equals("ninja2") || event.getNodeA().getName().equals("ninja1")) {
                if (event.getNodeB().getName().equals("Ninja-ogremesh")) {
                    walk = false;
                }
            }
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if (animName.equals("Attack1")||animName.equals("Attack2")) {
            if (count <= 0) {
                channel1.setAnim("Death1", 0.01f);
                channel1.setLoopMode(LoopMode.DontLoop);
                channel1.setSpeed(1f);
                channel2.setAnim("Climb", 0.01f);
                channel2.setLoopMode(LoopMode.Loop);
                channel2.setSpeed(1f);
                count = 3;
                walk = false;
            } else {
                count--;
            }

        }
    }
}
