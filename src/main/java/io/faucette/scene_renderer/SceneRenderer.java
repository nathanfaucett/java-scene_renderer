package io.faucette.scene_renderer;


import io.faucette.scene_graph.Scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Collections;
import java.util.Comparator;


public class SceneRenderer {
    private Scene scene;

    private boolean initted;

    private List<Renderer> renderers;
    private Map<Class<? extends Renderer>, Renderer> rendererHash;

    private List<RendererPlugin> rendererPlugins;
    private Map<Class<? extends RendererPlugin>, RendererPlugin> rendererPluginHash;


    private Comparator<Renderer> rendererComparator = new Comparator<Renderer>() {
        @Override
        public int compare(Renderer a, Renderer b) {
            return new Integer(a.getOrder()).compareTo(b.getOrder());
        }
    };
    private Comparator<RendererPlugin> rendererPluginComparator = new Comparator<RendererPlugin>() {
        @Override
        public int compare(RendererPlugin a, RendererPlugin b) {
            return new Integer(a.getOrder()).compareTo(b.getOrder());
        }
    };


    public SceneRenderer(Scene s) {
        scene = s;

        initted = false;

        renderers = new ArrayList<>();
        rendererHash = new HashMap<>();

        rendererPlugins = new ArrayList<>();
        rendererPluginHash = new HashMap<>();
    }

    public Scene getScene() {
        return scene;
    }

    public SceneRenderer init() {
        if (!initted) {
            initted = true;

            sortRendererPlugins();

            for (RendererPlugin rendererPlugin: rendererPlugins) {
                rendererPlugin.init();
            }

            sortRenderers();

            for (Renderer renderer: renderers) {
                renderer.init();
            }
        }
        return this;
    }

    public SceneRenderer render() {

        for (RendererPlugin rendererPlugin: rendererPlugins) {
            rendererPlugin.before();
        }

        for (Renderer renderer: renderers) {
            renderer.before();
        }
        for (Renderer renderer: renderers) {
            renderer.render();
        }
        for (Renderer renderer: renderers) {
            renderer.after();
        }

        for (RendererPlugin rendererPlugin: rendererPlugins) {
            rendererPlugin.after();
        }

        return this;
    }

    public SceneRenderer clear() {
        for (int i = rendererPlugins.size() - 1; i >= 0; i--) {
            rendererPlugins.get(i).destroy();
        }
        for (int i = renderers.size() - 1; i >= 0; i--) {
            renderers.get(i).destroy();
        }
        return this;
    }

    public SceneRenderer destroy() {
        return clear();
    }

    private void sortRenderers() {
        Collections.sort(renderers, rendererComparator);
    }
    private void sortRendererPlugins() {
        Collections.sort(rendererPlugins, rendererPluginComparator);
    }

    public <T extends RendererPlugin> boolean hasRendererPlugin(Class<T> rendererPluginClass) {
        return rendererPluginHash.containsKey(rendererPluginClass);
    }
    @SuppressWarnings("unchecked")
    public <T extends RendererPlugin> T getRendererPlugin(Class<T> rendererPluginClass) {
        return (T) rendererPluginHash.get(rendererPluginClass);
    }

    public <T extends RendererPlugin> SceneRenderer addRendererPlugin(T rendererPlugin) {
        if (!rendererPluginHash.containsKey(rendererPlugin.getClass())) {
            rendererPlugin.sceneRenderer = this;
            rendererPlugins.add(rendererPlugin);
            rendererPluginHash.put(rendererPlugin.getClass(), rendererPlugin);

            if (initted) {
                sortRendererPlugins();
                rendererPlugin.init();
            }
        }
        return this;
    }

    public <T extends RendererPlugin> SceneRenderer removeRendererPlugin(T rendererPlugin) {
        if (rendererPluginHash.containsKey(rendererPlugin.getClass())) {
            rendererPlugin.sceneRenderer = null;
            rendererPlugins.remove(rendererPlugin);
            rendererPluginHash.remove(rendererPlugin.getClass());
        }
        return this;
    }

    public <T extends Renderer> boolean hasRenderer(Class<T> rendererClass) {
        return rendererHash.containsKey(rendererClass);
    }
    @SuppressWarnings("unchecked")
    public <T extends Renderer> T getRenderer(Class<T> rendererClass) {
        return (T) rendererHash.get(rendererClass);
    }

    public <T extends Renderer> SceneRenderer addRenderer(T renderer) {
        if (!rendererHash.containsKey(renderer.getClass())) {
            renderer.sceneRenderer = this;
            renderers.add(renderer);
            rendererHash.put(renderer.getClass(), renderer);

            if (initted) {
                sortRenderers();
                renderer.init();
            }
        }
        return this;
    }

    public <T extends Renderer> SceneRenderer removeRenderer(T renderer) {
        if (rendererHash.containsKey(renderer.getClass())) {
            renderer.sceneRenderer = null;
            renderers.remove(renderer);
            rendererHash.remove(renderer.getClass());
        }
        return this;
    }
}
