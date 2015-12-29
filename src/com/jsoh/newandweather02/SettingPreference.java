package com.jsoh.newandweather02;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class SettingPreference extends PreferenceActivity {
	public static String NEWS_ALL = "news_all";
	public static String NEWS_POLITICAL = "news_political";
	public static String NEWS_ECONOMY = "news_economy";
	public static String NEWS_SOCIETY = "news_society";
	public static String NEWS_CULTURE = "news_culture";
	public static String NEWS_NATION = "news_nation";
	public static String NEWS_SCIENCE = "news_science";
	public static String NEWS_SPORTS = "news_sports";
	public static String NEWS_ENTERTAINMENT = "news_entertainment";
	
	public static String[] NEWS_LIST = { NEWS_ALL, NEWS_POLITICAL,
			NEWS_ECONOMY, NEWS_SOCIETY, NEWS_CULTURE, NEWS_NATION,
			NEWS_SCIENCE, NEWS_SPORTS, NEWS_ENTERTAINMENT}; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setTitle("날씨 및 뉴스 설정");

		PrefFragment prefs = new PrefFragment();
		getFragmentManager().beginTransaction().add(android.R.id.content, prefs).commit();
	}

	public static class PrefFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			addPreferencesFromResource(R.xml.pref_setting);
		}
		
	}
}
