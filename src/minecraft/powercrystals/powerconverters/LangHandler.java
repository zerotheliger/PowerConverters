package powercrystals.powerconverters;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.LanguageRegistry;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author samrg472
 */
public class LangHandler {

    private final File langFolder;
    private final String modID;

    public LangHandler(String modID, File langFolder) {
        this.modID = modID.toLowerCase();
        this.langFolder = langFolder;
    }

    public void init() {
        extractLang();
        loadLang();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void extractLang() {
        if (!langFolder.isDirectory())
            langFolder.mkdir();
        String langResourceBase = "/assets/" + modID + "/lang/";

        try {
            String[] list = getResources(langResourceBase, new LangFilter());

            if (list == null)
                return;
            for (String file : list) {
                InputStream is = this.getClass().getResourceAsStream(langResourceBase + file);
                if (is == null)
                    continue;
                try {
                    OutputStream os = new FileOutputStream(new File(langFolder, file));
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = is.read(buffer)) != -1) {
                        os.write(buffer, 0, read);
                    }
                    is.close();
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private void loadLang() {
        for (File langFile : langFolder.listFiles(new LangFilter())) {
            try {
                Properties langPack = new Properties();
                langPack.load(new InputStreamReader(new FileInputStream(langFile), "UTF-8"));

                String lang = langFile.getName().replace(".lang", "");
                LanguageRegistry.instance().addStringLocalization(langPack, lang);
            } catch (Exception x) {
                FMLLog.severe("There was a problem loading language file: " + langFile.getName());
                x.printStackTrace(System.err);
            }
        }
    }

    private String[] getResources(String path, ResourceFilter filter) throws URISyntaxException, IOException {
        URL url = this.getClass().getResource(path);
        if (url != null && url.getProtocol().equals("file"))
            return new File(url.toURI()).list(filter);

        if (url == null) {
            // Set the URL to the path of this class
            final String me = this.getClass().getName().replace(".", "/") + ".class";
            url = this.getClass().getResource(me);
        }

        if (url != null && url.getProtocol().equals("jar")) {
            final String jarPath = url.getPath().substring(5, url.getPath().indexOf("!")); // Only want the jar path
            final Set<String> result = new HashSet<String>();
            final Enumeration<JarEntry> entries = new JarFile(URLDecoder.decode(jarPath, "UTF-8")).entries();
            while (entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                String customPath = null;
                {
                    if (name.startsWith(path))
                        customPath = path;
                    else if (name.startsWith(path.substring(1)))
                        customPath = path.substring(1);
                }
                if (customPath != null) {
                    final String entry = name.substring(customPath.length());
                    if (!filter.accept(entry))
                        continue;
                    result.add(entry);
                }
            }
            return result.toArray(new String[result.size()]);
        }

        return new String[0];
    }

    private static class LangFilter implements ResourceFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".lang");
        }

        @Override
        public boolean accept(String name) {
            return name.endsWith(".lang");
        }
    }

    private static interface ResourceFilter extends FilenameFilter {
        public boolean accept(String name);
    }
}
