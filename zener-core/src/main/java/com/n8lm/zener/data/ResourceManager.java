/*
 * This file is part of Zener.
 *
 * Zener is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * Zener is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with Zener.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

/**
 *
 */
package com.n8lm.zener.data;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

import com.n8lm.zener.audio.openal.Audio;
import com.n8lm.zener.audio.openal.AudioLoader;

import com.n8lm.zener.assets.IQELoader;
import com.n8lm.zener.assets.Image;
import com.n8lm.zener.assets.Material;
import com.n8lm.zener.assets.Model;
import com.n8lm.zener.assets.OBJLoader;
import com.n8lm.zener.assets.PNGLoader;
import com.n8lm.zener.graphics.HardResourceManager;
import com.n8lm.zener.graphics.GLProgram;
import com.n8lm.zener.glsl.ShaderManager;
import com.n8lm.zener.graphics.Texture;
import com.n8lm.zener.utils.StringUtil;


/**
 * @author Alchemist
 */
public class ResourceManager {

    private final static Logger LOGGER = Logger.getLogger(ResourceManager.class
            .getName());


    private ArrayList<ResourceLocation> locations;

    private HardResourceManager geometryManager;
    private Map<String, Model> models;
    private Map<String, Material> materials;
    private Map<String, Texture> textures;
    private Map<String, Audio> sounds;
    private List<String> guiXmls;

    private ShaderManager shaderManager;

    private Map<Class<? extends GameDatabase>, GameDatabase> databases;

    private static final ResourceManager instance = new ResourceManager();

    public static ResourceManager getInstance() {
        return instance;
    }

    /**
     *
     */
    private ResourceManager() {
        models = new HashMap<>();
        materials = new HashMap<>();
        sounds = new HashMap<>();
        textures = new HashMap<>();
        guiXmls = new ArrayList<>();

        shaderManager = new ShaderManager(this);

        databases = new HashMap<>();

        geometryManager = new HardResourceManager();

        locations = new ArrayList<>();
        locations.add(new ClasspathLocation(""));
        locations.add(new FileSystemLocation(new File("")));
    }

    public void add(String name, Audio sound) {
        sounds.put(name, sound);
    }

    public void add(String name, Material mat) {
        materials.put(name, mat);
    }

    public void add(String name, Model model) {
        models.put(name, model);
    }

	/*public void add(String name, GLProgram shader) {
        shaders.put(name, shader);
	}*/

    public void add(String name, Texture texture) {
        textures.put(name, texture);
    }

    public <T extends GameDatabase> T setDatabase(T database) {
        databases.put(database.getClass(), database);
    	/*
    	try {
			database.load(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        return database;
    }


    private void loadAudio(String name, boolean isStreaming, String filename) {

        try {

            Audio audio = null;

            if (isStreaming)
                audio = AudioLoader.getStreamingAudio(filename.substring(filename.lastIndexOf('.') + 1).toUpperCase()
                        , filename, this);
            else
                audio = AudioLoader.getAudio(filename.substring(filename.lastIndexOf('.') + 1).toUpperCase()
                        , getResourceAsStream(filename));


            add(name, audio);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTexturedModel(String name, String filename) {

        try {

            String extension = "";
            int i = filename.lastIndexOf('.');
            if (i >= 0) {
                extension = filename.substring(i + 1);
            }
            Model model;
            if (extension.equals("obj")) {
                model = OBJLoader.loadTexturedModel(getResourceAsStream(filename));
                add(name, model);
            } else if (extension.equals("iqe")) {
                model = IQELoader.loadModel(getResourceAsStream(filename));
                add(name, model);
            }

            LOGGER.info("Loaded :" + filename);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadShaderSource(String name, String[] filenames) {
        shaderManager.loadShaderSource(name, filenames);
    }

    public String readText(String filename) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getResourceAsStream(filename)));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }


    public void loadImage(String name, String filename) {

        try {
            Image image = PNGLoader.loadPNGImage(getResourceAsStream(filename));
            //Texture texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(filename));
            //texture.setTextureFilter(GL_NEAREST);
            //GL_TEXTURE_2D_ARRAY
			/*texture.bind();
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE); 
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);*/

            add(name, new Texture(image));

            LOGGER.info("Loaded :" + filename);
            //LOGGER.info(texture.getFormat() + "");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public Audio getSound(String name) {
        return sounds.get(name);
    }

    public Material getMaterial(String name) {
        return materials.get(name);
    }

    public Model getModel(String name) {
        return models.get(name);
    }

    public GLProgram getProgram(String vertName, String fragName, List<String> options) {
        return shaderManager.getProgram(vertName, fragName, options);
    }

    public Texture getTexture(String name) {
        return textures.get(name);
    }

    public List<String> getGUIXmls() {
        return guiXmls;
    }

    public <T extends GameDatabase> T getDatabase(Class<T> databaseType) {
        return databaseType.cast(databases.get(databaseType));
    }

    public void loadDataConfig(String configFileName) {
        loadDataConfig(getResourceAsStream(configFileName));
    }

    private void loadDataConfig(InputStream input) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;

        try {
            String[] strs = new String[10];
            int n;
            while ((line = reader.readLine()) != null) {
                n = StringUtil.splitOnWhitespace(line, strs);
                if (n == 0)
                    continue;
                if (strs[0].compareTo("addClassLoaderPath") == 0) {
                    addResourceLocation(new ClasspathLocation(strs[1]));
                } else if (strs[0].compareTo("addFileSystemPath") == 0) {
                    addResourceLocation(new FileSystemLocation(new File(strs[1])));
                } else if (strs[0].compareTo("audio") == 0) {
                    boolean isStream = false;
                    if (n > 4 && strs[3].equals("-stream"))
                        isStream = true;
                    loadAudio(strs[1], isStream, strs[2]);
                }
                if (strs[0].compareTo("model") == 0) {
                    loadTexturedModel(strs[1], strs[2]);
                    if (n > 4 && strs[3].equals("-sk"))
                        getModel(strs[1]).combineBoneToSkeleton(getModel(strs[4]).getSkeleton());
                } else if (strs[0].compareTo("shader") == 0) {
                    String[] filenames = new String[n - 2];
                    for (int i = 0; i < filenames.length; i++) {
                        filenames[i] = strs[i + 2];
                    }
                    loadShaderSource(strs[1], filenames);
                } else if (strs[0].compareTo("texture") == 0) {
                    loadImage(strs[1], strs[2]);
                } else if (strs[0].compareTo("gui") == 0) {
                    guiXmls.add(strs[1]);
                } else if (strs[0].compareTo("database") == 0) {

                    try {
                        Class<?> cls;
                        cls = Class.forName(strs[1]);
                        if (GameDatabase.class.isAssignableFrom(cls)) {
                            getDatabase((Class<? extends GameDatabase>) cls).load(ResourceManager.getInstance().getResourceAsStream(strs[2]));
                        } else
                            LOGGER.severe("Database Class " + strs[1] + " not found");
                    } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
					/*
					if(strs[1].compareTo("TiledMapDatabase") == 0)
					else if(strs[1].compareTo("ElementDatabase") == 0)
					else if(strs[1].compareTo("WeaponDatabase") == 0)
					else if(strs[1].compareTo("CharacterClassDatabase") == 0)
						getDatabase(CharacterClassDatabase.class).load(ResourceLoader.getResourceAsStream(strs[2]));*/
                }
            }
        } catch (IOException e) {
            LOGGER.severe("Config file error: io error");
        }
    }

    /**
     * Add a location that will be searched for resources
     *
     * @param location The location that will be searched for resoruces
     */
    public void addResourceLocation(ResourceLocation location) {
        locations.add(location);
    }

    /**
     * Remove a location that will be no longer be searched for resources
     *
     * @param location The location that will be removed from the search list
     */
    public void removeResourceLocation(ResourceLocation location) {
        locations.remove(location);
    }

    /**
     * Remove all the locations, no resources will be found until
     * new locations have been added
     */
    public void removeAllResourceLocations() {
        locations.clear();
    }

    /**
     * Get a resource
     *
     * @param ref The reference to the resource to retrieve
     * @return A stream from which the resource can be read
     */
    public InputStream getResourceAsStream(String ref) {
        InputStream in = null;

        for (ResourceLocation location : locations) {
            in = location.getResourceAsStream(ref);
            if (in != null) {
                break;
            }
        }

        if (in == null) {
            throw new RuntimeException("Resource not found: " + ref);
        }

        return new BufferedInputStream(in);
    }

    /**
     * Check if a resource is available from any given resource loader
     *
     * @param ref A reference to the resource that should be checked
     * @return True if the resource can be located
     */
    public boolean resourceExists(String ref) {
        URL url = null;

        for (ResourceLocation location1 : locations) {
            ResourceLocation location = location1;
            url = location.getResource(ref);
            if (url != null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get a resource as a URL
     *
     * @param ref The reference to the resource to retrieve
     * @return A URL from which the resource can be read
     */
    public URL getResource(String ref) {

        URL url = null;

        for (ResourceLocation location1 : locations) {
            ResourceLocation location = location1;
            url = location.getResource(ref);
            if (url != null) {
                break;
            }
        }

        if (url == null) {
            throw new RuntimeException("Resource not found: " + ref);
        }

        return url;
    }

    public HardResourceManager getGeometryManager() {
        return geometryManager;
    }

    public void setGeometryManager(HardResourceManager geometryManager) {
        this.geometryManager = geometryManager;
    }
}
