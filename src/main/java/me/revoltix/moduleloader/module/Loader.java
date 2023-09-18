package me.revoltix.moduleloader.module;

import me.revoltix.moduleloader.exceptions.ErrorInLoadModuleClassException;
import me.revoltix.moduleloader.exceptions.InvalidModuleNameException;
import me.revoltix.moduleloader.exceptions.ModuleNameAlreadyInUseException;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Loader {

    // A private field that holds the folder that the modules are loaded from.
    private final File folder;
    // A reference to the `ModuleManager` object that is used to register and unregister modules.
    private final ModuleManager manager;

    public Loader(File folder, ModuleManager manager) {
        this.manager = manager;
        this.folder = folder;
        if (!this.folder.exists()) {
            this.folder.mkdirs();
        }
    }

    /**
     * It loads all the modules in the modules folder and registers them with the manager
     */
    @SuppressWarnings("deprecation")
    public void loadModules() {
        final HashMap<String, PluginModule> modules = new HashMap<>();
        final HashMap<PluginModule, List<Class<?>>> moduleClasses = new HashMap<>();
        final HashMap<PluginModule, List<Listener>> listeners = new HashMap<>();
        final HashMap<PluginModule, List<ModuleCommandExecutor>> commands = new HashMap<>();
        List<File> paths = Arrays.stream(Objects.requireNonNull(folder.listFiles())).toList();
        ClassPathImporter loader = new ClassPathImporter(new URL[] {}, getClass().getClassLoader());
        paths.forEach(file -> {
            JarFile jarFile = null;
            try {
                jarFile = new JarFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert jarFile != null;
            Enumeration<JarEntry> e = null;
            try {
                e = jarFile.entries();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            URL[] urls = new URL[0];
            try {
                urls = new URL[]{ new URL("jar:file:" + file.getAbsolutePath() +"!/") };
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
            try {
                if(!Arrays.stream(urls).toList().isEmpty()) {
                    for (URL url : urls) {
                        loader.addURL(url);
                    }
                }
                loader.addURL(file.toURI().toURL());
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
            List<Class<?>> classes = new ArrayList<>();
            AtomicReference<PluginModule> module = new AtomicReference<>(null);
            List<ModuleCommandExecutor> cmds = new ArrayList<>();
            List<Listener> ls = new ArrayList<>();
            while (true) {
                assert e != null;
                if (!e.hasMoreElements()) break;
                JarEntry je = e.nextElement();
                if(je.isDirectory() || !je.getName().endsWith(".class")){
                    continue;
                }
                // -6 because of .class
                String className = je.getName().substring(0,je.getName().length()-6);
                className = className.replace('/', '.');
                try {
                    Class<?> c = loader.loadClass(className);
                    List<Annotation> annotations = new ArrayList<>(Arrays.stream(c.getAnnotations()).toList());
                    annotations.forEach(annotation -> {
                        if(annotation instanceof ModuleCommand) {
                            try {
                                cmds.add((ModuleCommandExecutor) c.newInstance());
                            } catch (InstantiationException | IllegalAccessException ex) {
                                ex.printStackTrace();
                            }
                        } else if(annotation instanceof ModuleListener) {
                            try {
                                ls.add((Listener) c.newInstance());
                            } catch (InstantiationException | IllegalAccessException ex) {
                                ex.printStackTrace();
                            }
                        } else if(annotation instanceof Module) {
                            try {
                                module.set((PluginModule) c.newInstance());
                            } catch (InstantiationException | IllegalAccessException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    classes.add(c);
                } catch (Exception ex) {
                    try {
                        throw new ErrorInLoadModuleClassException("Error at loading class " + className, new Throwable("Class cannot be load because of", ex));
                    } catch (ErrorInLoadModuleClassException exc) {
                        exc.printStackTrace();
                    }
                }
            }
            if(module.get() != null) {
                if(manager.getModule(module.get().getClass().getAnnotation(Module.class).moduleName()) != null) {
                    try {
                        throw new ModuleNameAlreadyInUseException("Error at Module " + module.get().getClass().getAnnotation(Module.class).moduleName(), new Throwable("Name already in use"));
                    } catch (ModuleNameAlreadyInUseException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    if(module.get().getClass().getAnnotation(Module.class).moduleName().equals("")) {
                        try {
                            throw new InvalidModuleNameException("Error in Class " + module.get().getClass().getName(), new Throwable("Module name is not set"));
                        } catch (InvalidModuleNameException ex) {
                            ex.printStackTrace();
                        }
                    }
                    modules.put(module.get().getClass().getAnnotation(Module.class).moduleName(), module.get());
                    moduleClasses.put(module.get(), classes);
                    listeners.put(module.get(), ls);
                    commands.put(module.get(), cmds);
                }
            }
        });
        modules.forEach((mName, module) -> {
            manager.registerModule(module);
            for (Class<?> aClass : moduleClasses.get(module)) {
                manager.addModuleClass(module, aClass);
            }
            for (ModuleCommandExecutor command : commands.get(module)) {
                manager.registerCommand(module, command);
            }
            for (Listener moduleListener : listeners.get(module)) {
                manager.registerListener(module, moduleListener);
            }
            manager.enableModule(module);
        });
    }

    /**
     * This function is called when the server is shutting down
     */
    public void unloadModules() {
        System.gc();
    }

}
