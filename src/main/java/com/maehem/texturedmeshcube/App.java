/**
 * Map a PNG image to a cube (triangle mesh) in JavaFX with FXGL
 * 
 * By Mark J Koch - 2021/10
 * 
 */
package com.maehem.texturedmeshcube;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Camera3D;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.TransformComponent;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 * App
 */
public class App extends GameApplication {

    private Camera3D camera;
    private boolean titleSet = false;
    private TriangleMesh mesh;
    private Entity cube;

    @Override
    protected void initSettings(GameSettings gs) {
        gs.setWidth(800);
        gs.setHeight(600);
        gs.set3D(true);
    }

    @Override
    protected void initGame() {
        // disable big mouse cursor
        getGameScene().getRoot().setCursor(Cursor.DEFAULT);
        // Set your custom cursor: (searched for in assets/ui/cursors/ )
        // getGameScene().setCursor(String imageName, Point2D hotspot);

        
        camera = getGameScene().getCamera3D();
        TransformComponent t = camera.getTransform();
        t.translateX(-28);
        t.translateY(-7);
        t.translateZ(-7);
        t.lookAt(new Point3D(0, 0, 0));
        
        cube = new EntityBuilder()
                .at(0, 0, 0)
                .view(initMeshView())
                .buildAndAttach();
    }

    @Override
    protected void onUpdate(double tpf) {
        // JavaFX and FXGL version information not available until after game inits.
        if (!titleSet) {
            var javaVersion = System.getProperty("java.version");
            var javafxVersion = System.getProperty("javafx.version");
            var fxglVersion = FXGL.getVersion();

            String label = "Basic FXGL App :: Java: " + javaVersion + " with JavaFX: " + javafxVersion + " and FXGL: " + fxglVersion;
            FXGL.getPrimaryStage().setTitle(label);
            titleSet = true;
        }
        
        // Make it spin
        cube.rotateBy(0.5);
    }

    @Override
    protected void initInput() {
        // Zoom in and out with mouse scroll wheel.
        getGameScene().getRoot().setOnScroll((t) -> {
            TransformComponent tr = camera.getTransform();
            tr.translateX(t.getDeltaY());
            tr.lookAt(new Point3D(0, 0, 0));
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private MeshView initMeshView() {
        mesh = new TriangleMesh();
        
        mesh.getPoints().addAll(
                 10.0f,  10.0f,  10.0f, // Vertex 0
                 10.0f,  10.0f, -10.0f, // Vertex 1
                 10.0f, -10.0f,  10.0f, // Vertex 2
                 10.0f, -10.0f, -10.0f, // Vertex 3
                -10.0f,  10.0f,  10.0f, // Vertex 4
                -10.0f,  10.0f, -10.0f, // Vertex 5
                -10.0f, -10.0f,  10.0f, // Vertex 6
                -10.0f, -10.0f, -10.0f  // Vertex 7
        );
        mesh.getTexCoords().addAll(
                0.25f, 0.00f,
                0.50f, 0.00f,
                0.00f, 0.333f,
                0.25f, 0.333f,
                0.50f, 0.333f,
                0.75f, 0.333f,
                1.00f, 0.333f,
                0.00f, 0.666f,
                0.25f, 0.666f,
                0.50f, 0.666f,
                0.75f, 0.666f,
                1.00f, 0.666f,
                0.25f, 1.00f,
                0.50f, 1.00f
        );
        
        mesh.getFaces().addAll(
            0,10,  2,5,   1,9,
            2,5,   3,4,   1,9,
            4,7,   5,8,   6,2,
            6,2,   5,8,   7,3,
            0,13,  1,9,  4,12,
            4,12,  1,9,   5,8,
            2,1,   6,0,   3,4,
            3,4,   6,0,   7,3,
            0,10, 4,11,   2,5,
            2,5,  4,11,   6,6,
            1,9,   3,4,   5,8,
            5,8,   3,4,   7,3            
        );
        
        Image im = new Image(getClass().getResourceAsStream("/assets/textures/cube-template.png"));
        Material mat = new PhongMaterial(Color.GRAY, im, null, null, im);
        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(mat);
        
        return meshView;
    }
}
