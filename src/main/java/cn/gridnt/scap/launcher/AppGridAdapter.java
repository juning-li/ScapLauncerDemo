package cn.gridnt.scap.launcher;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Adapter for launcher to show installed apps
 *
 * @author juning.lee
 * @version 1.0
 * @since 2015-08-05
 */
public class AppGridAdapter extends BaseAdapter {

    public AppGridAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    private LayoutInflater mLayoutInflater;
    private List<AppInfo> mInstalledApps;

    /**
     * 设置已经安装的应用信息
     *
     * @param apps 应用信息集合
     */
    public void setInstalledApps(List<AppInfo> apps) {
        mInstalledApps = apps;
        notifyDataSetChanged();
    }

    /**
     * 添加某些已经安装的应用信息
     *
     * @param app 应用信息集合
     */
    public void addInstalledApps(AppInfo app) {
        if (mInstalledApps == null) {
            mInstalledApps = new ArrayList<>();
        }
        if (app != null) {
            mInstalledApps.add(app);
            notifyDataSetChanged();
        }
    }

    /**
     * 移除某个已安装的应用信息
     *
     * @param packageName 应用包名
     */
    public void removeInstalledApp(String packageName) {
        if (!TextUtils.isEmpty(packageName) && mInstalledApps != null) {
            Iterator<AppInfo> iterator = mInstalledApps.iterator();
            while (iterator.hasNext()) {
                AppInfo appInfo = iterator.next();
                if (packageName.equals(appInfo.getPackageName())) {
                    iterator.remove();
                    notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    public int getCount() {
        return mInstalledApps == null ? 0 : mInstalledApps.size();
    }

    @Override
    public AppInfo getItem(int position) {
        return mInstalledApps.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        H h = null;
        if (convertView == null) {
            h = new H();
            View appItemView = mLayoutInflater.inflate(R.layout.griditem_app, null);
            h.appIcon = (ImageView) appItemView.findViewById(R.id.app_icon);
            h.appName = (TextView) appItemView.findViewById(R.id.app_name);
            convertView = appItemView;
            convertView.setTag(h);
        } else {
            h = (H) convertView.getTag();
        }
        // bind data to item view
        AppInfo appInfo = getItem(position);
        h.appName.setText(appInfo.getLabel());
        h.appIcon.setImageDrawable(appInfo.getIcon());
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * view holder
     */
    private class H {
        private ImageView appIcon;
        private TextView appName;
    }
}
