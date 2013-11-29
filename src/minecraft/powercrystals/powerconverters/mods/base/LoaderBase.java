package powercrystals.powerconverters.mods.base;

import cpw.mods.fml.common.Loader;

/**
 * @author samrg472
 */
public abstract class LoaderBase {

    public final boolean isLoaded;

    public LoaderBase(String modID) {
        isLoaded = Loader.isModLoaded(modID);
    }

    public final void load(Stage stage) {
        if (!isLoaded)
            return;
        switch (stage) {
            case PREINIT:
                preInit();
                break;
            case INIT:
                init();
                break;
            case POSTINIT:
                postInit();
                break;
        }
    }

    protected abstract void preInit();

    protected abstract void init();

    protected abstract void postInit();

    public static enum Stage {
        PREINIT,
        INIT,
        POSTINIT
    }
}
