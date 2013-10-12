package powercrystals.powerconverters;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.LanguageRegistry;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

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

        URL resource = this.getClass().getResource(langResourceBase);
        if (resource == null)
            return;

        try {
            File[] list = new File(resource.toURI()).listFiles(new LangFilter());

            if (list == null)
                return;
            for (File file : list) {
                InputStream is = this.getClass().getResourceAsStream(langResourceBase + file.getName());
                if (is == null)
                    continue;
                try {
                    OutputStream os = new FileOutputStream(new File(langFolder, file.getName()));
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
        } catch (URISyntaxException e) {
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

    private static class LangFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".lang");
        }
    }
}
