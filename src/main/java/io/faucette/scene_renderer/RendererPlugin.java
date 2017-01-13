package io.faucette.scene_renderer;


public class RendererPlugin {
    protected SceneRenderer sceneRenderer;


    public RendererPlugin() {
        sceneRenderer = null;
    }

    public SceneRenderer getSceneRenderer() {
        return sceneRenderer;
    }

    public int getOrder() {
        return 0;
    }

    public RendererPlugin init() {
        return this;
    }
    public RendererPlugin clear() {
        return this;
    }
    public RendererPlugin before() {
        return this;
    }
    public RendererPlugin after() {
        return this;
    }

    public RendererPlugin destroy() {
        if (sceneRenderer != null) {
            sceneRenderer.removeRendererPlugin(this);
        }
        clear();
        return this;
    }
}
