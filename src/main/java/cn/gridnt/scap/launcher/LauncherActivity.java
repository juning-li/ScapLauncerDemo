package cn.gridnt.scap.launcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.lee.tools.AppUtils;

/**
 * Scap Launcher
 *
 * @author juning.lee
 * @version 1.0
 * @since 2015-08-05
 */
public class LauncherActivity extends Activity implements AdapterView.OnItemClickListener, AppInstallWatcher.IAppInstallWatcher {


    private static LauncherActivity sMyself;
    /**
     * package manager
     */
    private PackageManager mPackageManager;
    /**
     * 应用grid
     */
    private GridView mAppGrid;
    /**
     * 应用适配器
     */
    private AppGridAdapter mAppAdapter;

    public static LauncherActivity getInstance() {
        return sMyself;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sMyself = this;
        // hide title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set content view
        setContentView(R.layout.activity_launcher);
        // get package manager
        mPackageManager = getPackageManager();
        // initial app grid
        setupAppGrid();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sMyself = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) {
            showLauncherVersion();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        // ignore it
    }

    @Override
    public void onAppInstalled(String packageName) {
        /**
         * 应用被安装：查询应用信息，将新应用显示在列表中
         */
        // 新建用于查询的app intent
        Intent queryIntent = new Intent(Intent.ACTION_MAIN, null);
        queryIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        queryIntent.setPackage(packageName);
        // 查得刚安装的应用信息
        List<ResolveInfo> resolveList = mPackageManager.queryIntentActivities(queryIntent, 0);
        if (resolveList != null && resolveList.size() > 0) {
            // list中应该只有一个应用
            AppInfo newInstalledApp = AppInfo.from(mPackageManager, resolveList.get(0));
            mAppAdapter.addInstalledApps(newInstalledApp);
        }
    }

    @Override
    public void onAppRemoved(String packageName) {
        /**
         * 应用被卸载：在列表中将其隐藏
         */
        mAppAdapter.removeInstalledApp(packageName);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AppInfo appInfo = mAppAdapter.getItem(position);
        Intent intent = new Intent();
        intent.setClassName(appInfo.getPackageName(), appInfo.getClassName());
        try {
            startActivity(intent);
        } catch (Exception e) {
            // 启动此应用失败
            String lauchErrorMessage = String.format(getString(R.string.cant_launch_app), appInfo.getLabel());
            Toast.makeText(this, lauchErrorMessage, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 初始化用于显示所有应用的grid
     */
    private void setupAppGrid() {
        mAppGrid = (GridView) findViewById(R.id.gv_apps);
        mAppGrid.setOnItemClickListener(this);
        mAppAdapter = new AppGridAdapter(this);
        mAppAdapter.setInstalledApps(getInstalledApps());
        mAppGrid.setAdapter(mAppAdapter);
    }


    /**
     * 取到所有已经安装的应用
     */
    private List<AppInfo> getInstalledApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<AppInfo> appList = new ArrayList<>();
        List<ResolveInfo> resolveList = mPackageManager.queryIntentActivities(intent, 0);
        if (resolveList != null && resolveList.size() > 0) {
            for (ResolveInfo resolveInfo : resolveList) {
                appList.add(AppInfo.from(mPackageManager, resolveInfo));
            }
        }
        return appList;
    }

    /**
     * 弹出dialog显示当前应用的版本
     */
    private void showLauncherVersion() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.setTitle(R.string.version_info);
        // 取得当前版本信息
        String nowVersion = AppUtils.getVersionName(this);
        String versionMessage = String.format(getString(R.string.now_version_is), nowVersion);
        b.setMessage(versionMessage);
        b.setCancelable(false);
        b.show();
    }
}
