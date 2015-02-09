package com.linangran.tgfcapp.fragments;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.linangran.tgfcapp.R;
import com.linangran.tgfcapp.utils.PreferenceUtils;

/**
 * Created by linangran on 5/2/15.
 */
public class SettingFragment extends PreferenceFragment
{
	public static final String TAG = "setting_fragment_tag";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.fragment_settings);
		this.getActivity().setTitle("设置");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = super.onCreateView(inflater, container, savedInstanceState);
		Preference aboutPreference = findPreference(PreferenceUtils.KEY_ABOUT_VERSION);
		try
		{
			aboutPreference.setSummary(aboutPreference.getSummary() + getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
		}
		catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return view;
	}


}
