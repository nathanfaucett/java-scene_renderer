package io.faucette.scene_renderer;


public class Renderer {
    protected SceneRenderer sceneRenderer;


    public Renderer() {
        sceneRenderer = null;
    }

    public SceneRenderer getSceneRenderer() {
        return sceneRenderer;
    }

    public int getOrder() {
        return 0;
    }

    public Renderer init() {
        return this;
    }
    public Renderer clear() {
        return this;
    }

    public Renderer before() {
        return this;
    }
    public Renderer after() {
        return this;
    }
    public Renderer render() {
        return this;
    }

    public Renderer destroy() {
        if (sceneRenderer != null) {
            sceneRenderer.removeRenderer(this);
        }
        clear();
        return this;
    }
}
