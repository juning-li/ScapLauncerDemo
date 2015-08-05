package cn.gridnt.scap.launcher;


import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

/**
 * 应用信息
 *
 * @author juning.lee
 * @version 1.0
 * @since 2015-08-05
 */
public class AppInfo {

    private Drawable icon;
    private CharSequence label;
    private String packageName;
    private String className;

    /**
     * @param icon        应用图标
     * @param label       应用名称
     * @param packageName 应用包名
     * @param className   应用启动类名
     */
    public AppInfo(Drawable icon, CharSequence label, String packageName, String className) {
        setIcon(icon);
        setLabel(label);
        setPackageName(packageName);
        setClassName(className);
    }

    /**
     * 将ResolveInfo转换成AppInfo
     *
     * @param packageManager
     * @param resolveInfo
     * @return
     */
    public static AppInfo from(PackageManager packageManager, ResolveInfo resolveInfo) {
        if (packageManager == null || resolveInfo == null) {
            return null;
        }
        Drawable iconDrawable = resolveInfo.activityInfo.loadIcon(packageManager);
        CharSequence label = resolveInfo.activityInfo.loadLabel(packageManager);
        String packageName = resolveInfo.activityInfo.packageName;
        String className = resolveInfo.activityInfo.name;
        return new AppInfo(iconDrawable, label, packageName, className);
    }

    public Drawable getIcon() {
        return icon;
    }

    public CharSequence getLabel() {
        return label;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setLabel(CharSequence label) {
        this.label = label;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
