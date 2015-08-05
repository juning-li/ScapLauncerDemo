package cn.gridnt.scap.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * 应用安装/卸载的监听器
 *
 * @author juning.lee
 * @version 1.0
 * @since 2015-08-05
 */
public class AppInstallWatcher extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        // ssp is the package name
        String packageName = intent.getData().getSchemeSpecificPart();
        Log.w("APP", action + "   |   " + packageName);
        if (Intent.ACTION_PACKAGE_ADDED.equals(action) || Intent.ACTION_PACKAGE_REPLACED.equals(action)) {
            // 应用被安装或被替换
            IAppInstallWatcher appWatcher = LauncherActivity.getInstance();
            if (appWatcher != null) {
                appWatcher.onAppInstalled(packageName);
            }
        } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action) || Intent.ACTION_PACKAGE_FULLY_REMOVED.equals(action)) {
            // 应用被卸载
            IAppInstallWatcher appWatcher = LauncherActivity.getInstance();
            if (appWatcher != null) {
                appWatcher.onAppRemoved(packageName);
            }
        }
    }

    public interface IAppInstallWatcher {

        void onAppInstalled(String packageName);

        void onAppRemoved(String packageName);
    }
}
