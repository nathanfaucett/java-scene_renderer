package io.faucette.scene_renderer;


import io.faucette.scene_graph.Scene;

import static org.junit.Assert.*;
import org.junit.*;


public class SceneRendererTest {
    @Test
    public void test() {
        Scene scene = new Scene();
        SceneRenderer sceneRenderer = new SceneRenderer(scene);

        scene.init();

        sceneRenderer.addRendererPlugin(new RendererPlugin());
        sceneRenderer.addRenderer(new Renderer());

        sceneRenderer.init();
        sceneRenderer.render();

        assertTrue(sceneRenderer.hasRenderer(Renderer.class));
        assertTrue(sceneRenderer.hasRendererPlugin(RendererPlugin.class));
    }
}
