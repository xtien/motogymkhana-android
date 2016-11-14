package eu.motogymkhana.competition.robo;

import android.app.Application;

import org.junit.runners.model.InitializationError;
import org.robolectric.DefaultTestLifecycle;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.TestLifecycle;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.FileFsFile;
import org.robolectric.util.ReflectionHelpers;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.TestConstants;
import toothpick.Scope;
import toothpick.Toothpick;

public class RoboInjectedTestRunner extends RobolectricTestRunner {

    public RoboInjectedTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Class<? extends TestLifecycle> getTestLifecycleClass() {
        return TestLifeCycleWithInjection.class;
    }

    public static class TestLifeCycleWithInjection extends DefaultTestLifecycle {

        @Override
        public void prepareTest(Object test) {
            Application application = RuntimeEnvironment.application;
            Scope appScope = Toothpick.openScope(Constants.DEFAULT_SCOPE);
            appScope.installModules(new GymkhanaModule(application),new TestModule(application));
            Toothpick.inject(test, appScope);
        }
    }

    protected AndroidManifest getAppManifest(Config config) {

        if (config.constants() == Void.class) {
            throw new RuntimeException("No \'constants\' field in @Config annotation!");

        } else {
            String type = getType(config);
            String flavor = getFlavor(config);
            String packageName = getPackageName(config);

            FileFsFile res;
            if (FileFsFile.from(new String[]{TestConstants.ROBO_BASE_DIR, "res", "merged"}).exists()) {
                res = FileFsFile.from(new String[]{TestConstants.ROBO_BASE_DIR, "res", "merged", flavor, type});
            } else if (FileFsFile.from(new String[]{TestConstants.ROBO_BASE_DIR, "res"}).exists()) {
                res = FileFsFile.from(new String[]{TestConstants.ROBO_BASE_DIR, "res", flavor, type});
            } else {
                res = FileFsFile.from(new String[]{TestConstants.ROBO_BASE_DIR, "bundles", flavor, type, "res"});
            }

            FileFsFile assets;
            if (FileFsFile.from(new String[]{TestConstants.ROBO_BASE_DIR, "assets"}).exists()) {
                assets = FileFsFile.from(new String[]{TestConstants.ROBO_BASE_DIR, "assets", flavor, type});
            } else {
                assets = FileFsFile.from(new String[]{TestConstants.ROBO_BASE_DIR, "bundles", flavor, type,
                        "assets"});
            }

            FileFsFile manifest;
            if (FileFsFile.from(new String[]{"build/intermediates", "manifests"}).exists()) {
                manifest = FileFsFile.from(new String[]{"build/intermediates", "manifests", "full", flavor, type,
                        "AndroidManifest.xml"});
            } else {
                manifest = FileFsFile.from(new String[]{TestConstants.ROBO_BASE_DIR, "manifests", "full", flavor, type,
                        "AndroidManifest.xml"});
            }

            return new AndroidManifest(manifest, res, assets, packageName);
        }
    }

    private static String getType(Config config) {
        try {
            return (String) ReflectionHelpers.getStaticField(config.constants(), "BUILD_TYPE");
        } catch (Throwable var2) {
            return null;
        }
    }

    private static String getFlavor(Config config) {
        try {
            return (String) ReflectionHelpers.getStaticField(config.constants(), "FLAVOR");
        } catch (Throwable var2) {
            return null;
        }
    }

    private static String getPackageName(Config config) {
        try {
            String e = config.packageName();
            return e != null && !e.isEmpty() ? e : (String) ReflectionHelpers.getStaticField(config.constants(),
                    "APPLICATION_ID");
        } catch (Throwable var2) {
            return null;
        }
    }
}