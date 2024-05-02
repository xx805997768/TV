package com.fongmi.android.tv.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.viewbinding.ViewBinding;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.R;
import com.fongmi.android.tv.Setting;
import com.fongmi.android.tv.databinding.ActivitySettingCustomBinding;
import com.fongmi.android.tv.event.RefreshEvent;
import com.fongmi.android.tv.ui.base.BaseActivity;
import com.fongmi.android.tv.ui.dialog.ButtonsDialog;
import com.fongmi.android.tv.ui.dialog.DisplayDialog;
import com.fongmi.android.tv.ui.dialog.LanguageDialog;
import com.fongmi.android.tv.ui.dialog.MenuKeyDialog;
import com.fongmi.android.tv.ui.dialog.X5WebViewDialog;
import com.fongmi.android.tv.utils.ResUtil;
import com.tencent.smtt.sdk.QbSdk;
import java.util.Locale;

public class SettingCustomActivity extends BaseActivity {

    private ActivitySettingCustomBinding mBinding;
    private String[] quality;
    private String[] size;
    private String[] episode;
    private String[] fullscreenMenuKey;
    private String[] smallWindowBackKey;
    private String[] homeUI;
    private String[] parseWebview;

    @Override
    protected ViewBinding getBinding() {
        return mBinding = ActivitySettingCustomBinding.inflate(getLayoutInflater());
    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, SettingCustomActivity.class));
    }

    private String getSwitch(boolean value) {
        return getString(value ? R.string.setting_on : R.string.setting_off);
    }

    @Override
    protected void initView() {
        mBinding.quality.requestFocus();
        mBinding.qualityText.setText((quality = ResUtil.getStringArray(R.array.select_quality))[Setting.getQuality()]);
        mBinding.sizeText.setText((size = ResUtil.getStringArray(R.array.select_size))[Setting.getSize()]);
        mBinding.episodeText.setText((episode = ResUtil.getStringArray(R.array.select_episode))[Setting.getEpisode()]);
        mBinding.speedText.setText(getSpeedText());
        mBinding.fullscreenMenuKeyText.setText((fullscreenMenuKey = ResUtil.getStringArray(R.array.select_fullscreen_menu_key))[Setting.getFullscreenMenuKey()]);
        mBinding.homeSiteLockText.setText(getSwitch(Setting.isHomeSiteLock()));
        mBinding.incognitoText.setText(getSwitch(Setting.isIncognito()));
        mBinding.smallWindowBackKeyText.setText((smallWindowBackKey = ResUtil.getStringArray(R.array.select_small_window_back_key))[Setting.getSmallWindowBackKey()]);
        mBinding.homeMenuKeyText.setText((ResUtil.getStringArray(R.array.select_home_menu_key))[Setting.getHomeMenuKey()]);
        mBinding.aggregatedSearchText.setText(getSwitch(Setting.isAggregatedSearch()));
        mBinding.homeUIText.setText((homeUI = ResUtil.getStringArray(R.array.select_home_ui))[Setting.getHomeUI()]);
        mBinding.homeHistoryText.setText(getSwitch(Setting.isHomeHistory()));
        mBinding.languageText.setText((ResUtil.getStringArray(R.array.select_language))[Setting.getLanguage()]);
        mBinding.parseWebviewText.setText((parseWebview = ResUtil.getStringArray(R.array.select_parse_webview))[Setting.getParseWebView()]);
    }

    @Override
    protected void initEvent() {
        mBinding.quality.setOnClickListener(this::setQuality);
        mBinding.size.setOnClickListener(this::setSize);
        mBinding.episode.setOnClickListener(this::setEpisode);
        mBinding.display.setOnClickListener(this::onDisplay);
        mBinding.speed.setOnClickListener(this::setSpeed);
        mBinding.speed.setOnLongClickListener(this::resetSpeed);
        mBinding.fullscreenMenuKey.setOnClickListener(this::setFullscreenMenuKey);
        mBinding.homeSiteLock.setOnClickListener(this::setHomeSiteLock);
        mBinding.incognito.setOnClickListener(this::setIncognito);
        mBinding.smallWindowBackKey.setOnClickListener(this::setSmallWindowBackKey);
        mBinding.homeMenuKey.setOnClickListener(this::onHomeMenuKey);
        mBinding.aggregatedSearch.setOnClickListener(this::setAggregatedSearch);
        mBinding.homeUI.setOnClickListener(this::setHomeUI);
        mBinding.homeButtons.setOnClickListener(this::onHomeButtons);
        mBinding.homeHistory.setOnClickListener(this::setHomeHistory);
        mBinding.setLanguage.setOnClickListener(this::setLanguage);
        mBinding.parseWebview.setOnClickListener(this::setParseWebview);

    }

    private void setQuality(View view) {
        int index = Setting.getQuality();
        Setting.putQuality(index = index == quality.length - 1 ? 0 : ++index);
        mBinding.qualityText.setText(quality[index]);
        RefreshEvent.image();
    }

    private void setSize(View view) {
        int index = Setting.getSize();
        Setting.putSize(index = index == size.length - 1 ? 0 : ++index);
        mBinding.sizeText.setText(size[index]);
        RefreshEvent.size();
    }

    private void setEpisode(View view) {
        int index = Setting.getEpisode();
        Setting.putEpisode(index = index == episode.length - 1 ? 0 : ++index);
        mBinding.episodeText.setText(episode[index]);
    }

    private void onDisplay(View view) {
        DisplayDialog.create(this).show();
    }

    private String getSpeedText() {
        return String.format(Locale.getDefault(), "%.2f", Setting.getPlaySpeed());
    }

    private void setSpeed(View view) {
        float speed = Setting.getPlaySpeed();
        float addon = speed >= 2 ? 1.0f : 0.1f;
        speed = speed >= 5 ? 0.2f : Math.min(speed + addon, 5.0f);
        Setting.putPlaySpeed(speed);
        mBinding.speedText.setText(getSpeedText());
    }

    private boolean resetSpeed(View view) {
        Setting.putPlaySpeed(1.0f);
        mBinding.speedText.setText(getSpeedText());
        return true;
    }

    private void setFullscreenMenuKey(View view) {
        int index = Setting.getFullscreenMenuKey();
        Setting.putFullscreenMenuKey(index = index == fullscreenMenuKey.length - 1 ? 0 : ++index);
        mBinding.fullscreenMenuKeyText.setText(fullscreenMenuKey[index]);
    }

    private void setHomeSiteLock(View view) {
        Setting.putHomeSiteLock(!Setting.isHomeSiteLock());
        mBinding.homeSiteLockText.setText(getSwitch(Setting.isHomeSiteLock()));
    }

    private void setIncognito(View view) {
        Setting.putIncognito(!Setting.isIncognito());
        mBinding.incognitoText.setText(getSwitch(Setting.isIncognito()));
    }

    private void setSmallWindowBackKey(View view) {
        int index = Setting.getSmallWindowBackKey();
        Setting.putSmallWindowBackKey(index = index == smallWindowBackKey.length - 1 ? 0 : ++index);
        mBinding.smallWindowBackKeyText.setText(smallWindowBackKey[index]);
    }

    private void onHomeMenuKey(View view) {
        MenuKeyDialog.create(this).show();
    }

    public void setHomeMenuText() {
        mBinding.homeMenuKeyText.setText((ResUtil.getStringArray(R.array.select_home_menu_key))[Setting.getHomeMenuKey()]);
    }

    private void setAggregatedSearch(View view) {
        Setting.putAggregatedSearch(!Setting.isAggregatedSearch());
        mBinding.aggregatedSearchText.setText(getSwitch(Setting.isAggregatedSearch()));
    }

    private void setHomeUI(View view) {
        int index = Setting.getHomeUI();
        Setting.putHomeUI(index = index == homeUI.length - 1 ? 0 : ++index);
        mBinding.homeUIText.setText(homeUI[index]);
    }

    private void onHomeButtons(View view) {
        ButtonsDialog.create(this).show();
    }

    private void setHomeHistory(View view) {
        Setting.putHomeHistory(!Setting.isHomeHistory());
        mBinding.homeHistoryText.setText(getSwitch(Setting.isHomeHistory()));
    }

    private void setLanguage(View view) {
        LanguageDialog.create(this).show();
    }

    public void setLanguageText() {
        mBinding.languageText.setText((ResUtil.getStringArray(R.array.select_language))[Setting.getLanguage()]);
    }


    private void setParseWebview(View view) {
        int index = Setting.getParseWebView();
        Setting.putParseWebView(index = index == parseWebview.length - 1 ? 0 : ++index);
        mBinding.parseWebviewText.setText(parseWebview[index]);
        if (index == 1 && QbSdk.getTbsVersion(App.get()) <= 0) X5WebViewDialog.create(this).show();
    }

}
