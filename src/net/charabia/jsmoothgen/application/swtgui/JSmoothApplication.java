/*
 * Created on May 14, 2004
 */
package net.charabia.jsmoothgen.application.swtgui;

import java.io.File;
import java.io.IOException;

import net.charabia.jsmoothgen.application.ExeCompiler;
import net.charabia.jsmoothgen.application.JSmoothModelBean;
import net.charabia.jsmoothgen.application.JSmoothModelPersistency;
import net.charabia.jsmoothgen.application.JavaPropertyPair;
import net.charabia.jsmoothgen.skeleton.SkeletonBean;
import net.charabia.jsmoothgen.skeleton.SkeletonList;
import net.charabia.jsmoothgen.skeleton.SkeletonProperty;

public class JSmoothApplication {

    private boolean dirty = false;

    private JSmoothModelBean modelBean;

    private File projFile;

    private SkeletonModel skeletonModel;
    
    private ExeCompiler compiler;
    
    public JSmoothApplication() {
        skeletonModel = new SkeletonModel(this);
        compiler = new ExeCompiler();
    }

    public void compile() throws CompilationException {
        modelBean.normalizePaths(projFile.getParentFile());
        String skeletonName = modelBean.getSkeletonName();
        SkeletonList skeletonList = skeletonModel.getSkeletonList();
        SkeletonBean skeletonBean = skeletonList.getSkeleton(skeletonName);
        File skeletonRoot = skeletonList.getDirectory(skeletonBean);
        File baseDir = projFile.getParentFile();
        File exeDir = baseDir;
        
        // NOTE: We assume the exe name is always relative
        File out = new File(exeDir, modelBean.getExecutableName());
        
        try {
            compiler.compile(skeletonRoot, skeletonBean, baseDir, modelBean, out);
        } catch (Exception e) {
            String[] messages = (String[]) compiler.getErrors().toArray(new String[0]);
            throw new CompilationException(messages);
        }
    }
    
    public String getCurrentDirectory() {
        String dir = modelBean.getCurrentDirectory();
        return dir == null ? "" : dir;
    }

    private String getDefaultSkeletonName() {
        // We want to return the "Window Wrapper" skeleton.
        // Due to the fact that we cannot identify the skeletons :(,
        // we'll have to make a hard guess.
        // It seems like the "Window Wrapper" is the second in the
        // list. In case it is not , we catch the exception,
        // and return the first skeleton in the list.
        String[] skelNames = skeletonModel.getSkeletonNames();
        try {
            return skelNames[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            return skelNames[0];
        }
    }

    public String getExecutable() {
        String exec = modelBean.getExecutableName();
        return exec == null ? "" : exec;
    }

    public String getIcon() {
        return modelBean.getIconLocation();

        // The default "eclipse" icon:
        // JSmoothResources.class.getResource("eclipse.gif").getPath();
    }

    public String getProjectName() {
        if (projFile == null) {
            return "[NO PROJECT]";
        }
        return projFile.getName();
    }

    public String getPropertyValue(String id) {
        JSmoothModelBean.Property[] props = modelBean.getSkeletonProperties();
        for (int i = 0; i < props.length; i++) {
            if (id.equals(props[i].Key)) {
                return props[i].getValue();
            }
        }
        return null;
    }

    public SkeletonModel getSkeletonModel() {
        return skeletonModel;
    }

    public String getSkeletonName() {
        if (modelBean == null) {
            return "";
        }
        return modelBean.getSkeletonName();
    }

    public boolean hasProjectFile() {
        return projFile != null;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void loadProject(String path) {
        if (path == null) {
            dirty = false;
            projFile = null;
            modelBean = null;
            return;
        }
    }

    public void newProject(String path) {
        setProject(path);

        System.out.println("[DEBUG] New project file: "
                + projFile.getAbsolutePath());

        // Create a new default JSmoothModelBean
        modelBean = new JSmoothModelBean();
        modelBean.setSkeletonName(getDefaultSkeletonName());
        setProperties(modelBean, skeletonModel.getProperties());
        modelBean.setExecutableName("[none]");
        modelBean.setArguments("");
        modelBean.setBundledJVMPath("");
        modelBean.setClassPath(new String[0]);
        modelBean.setCurrentDirectory("");
        modelBean.setIconLocation("");
        modelBean.setJarLocation("");
        modelBean.setJavaProperties(new JavaPropertyPair[0]);
        modelBean.setJVMSearchPath(new String[0]);
        modelBean.setMainClassName("");
        modelBean.setMaximumVersion("");
        modelBean.setMinimumVersion("");
        modelBean.setNoJvmMessage("");
        modelBean.setNoJvmURL("");
        //

        // Set the dirty flag
        dirty = true;
    }

    public boolean saveProject() {
        System.out.println("[DEBUG] Saving project to file: "
                + projFile.getAbsolutePath());
        try {
            JSmoothModelPersistency.save(projFile, modelBean);
        } catch (IOException e) {
            System.out
                    .println("[ERROR] Project save failed: " + e.getMessage());
            return false;
        }

        // Reset the dirty flag
        dirty = false;
        
        return true;
    }

    public void setCurrentDirectory(String dir) {
        System.out.println("[DEBUG] Setting current dir: " + dir);
        modelBean.setCurrentDirectory(dir);
    }

    public void setExecutable(String file) {
        System.out.println("[DEBUG] Setting executable file: " + file);
        modelBean.setExecutableName(file);
    }

    public void setIcon(String file) {
        System.out.println("[DEBUG] Setting icon file: " + file);
        modelBean.setIconLocation(file);
    }

    public void setProject(String path) {
        projFile = new File(path);
    }

    private void setProperties(JSmoothModelBean modelBean, SkeletonProperty[] props) {
        // Transfer the SkeletonProperty data to JSmoothModelBean.Property
        JSmoothModelBean.Property[] jsBeanProps = new JSmoothModelBean.Property[props.length];
        String sysoutProps = "";
        for (int i = 0; i < props.length; i++) {
            jsBeanProps[i] = new JSmoothModelBean.Property();
            jsBeanProps[i].setKey(props[i].getIdName());
            jsBeanProps[i].setValue(props[i].getValue());

            sysoutProps += "(" + props[i].getIdName() + ", \""
                    + props[i].getValue() + "\") ";
        }
        //

        System.out.println("[DEBUG] Setting skeleton props: " + sysoutProps);
        modelBean.setSkeletonProperties(jsBeanProps);
    }

    public void setPropertyValue(String id, String value) {
        JSmoothModelBean.Property[] props = modelBean.getSkeletonProperties();
        for (int i = 0; i < props.length; i++) {
            if (id.equals(props[i].Key)) {
                props[i].setValue(value);
            }
        }
    }

    public void setSkeletonName(String name) {
        System.out.println("[DEBUG] Setting skeleton name: " + name);
        modelBean.setSkeletonName(name);
        setProperties(modelBean, skeletonModel.getProperties());
    }
    
    public void setJarLocation(String jarLocation) throws InvalidPathException {
        modelBean.setJarLocation(jarLocation);
    }
    
}