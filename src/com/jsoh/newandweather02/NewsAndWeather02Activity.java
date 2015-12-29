package com.jsoh.newandweather02;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

public class NewsAndWeather02Activity extends Activity implements
		TabContentFactory {
	private static String HTTP_NEWS_ALL = "http://news.google.co.kr/news?pz=1&cf=all&ned=kr&hl=ko&output=rss"; // 주요뉴스
	private static String HTTP_NEWS_POLITICAL = "http://news.google.co.kr/news?pz=1&cf=all&ned=kr&hl=ko&topic=p&output=rss"; // 정치
	private static String HTTP_NEWS_ECONOMY = "http://news.google.co.kr/news?pz=1&cf=all&ned=kr&hl=ko&topic=b&output=rss"; // 경제
	private static String HTTP_NEWS_SOCIETY = "http://news.google.co.kr/news?pz=1&cf=all&ned=kr&hl=ko&topic=y&output=rss"; // 사회
	private static String HTTP_NEWS_CULTURE = "http://news.google.co.kr/news?pz=1&cf=all&ned=kr&hl=ko&topic=l&output=rss"; // 문화생활
	private static String HTTP_NEWS_NATION = "http://news.google.co.kr/news?pz=1&cf=all&ned=kr&hl=ko&topic=w&output=rss"; // 국제
	private static String HTTP_NEWS_SCIENCE = "http://news.google.co.kr/news?pz=1&cf=all&ned=kr&hl=ko&topic=t&output=rss"; // 과학기술
	private static String HTTP_NEWS_ENTERTAINMENT = "http://news.google.co.kr/news?pz=1&cf=all&ned=kr&hl=ko&topic=e&output=rss"; // 연예
	private static String HTTP_NEWS_SPORTS = "http://news.google.co.kr/news?pz=1&cf=all&ned=kr&hl=ko&topic=s&output=rss"; // 스포츠

	private static String[] SUBJECT_URL_LIST = { HTTP_NEWS_ALL,
			HTTP_NEWS_POLITICAL, HTTP_NEWS_ECONOMY, HTTP_NEWS_SOCIETY,
			HTTP_NEWS_CULTURE, HTTP_NEWS_NATION, HTTP_NEWS_SCIENCE,
			HTTP_NEWS_ENTERTAINMENT, HTTP_NEWS_SPORTS };

	private ArrayList<String> subject;

	private TabHost tabHost;
	private ViewPager mViewPager;
	private Context mContext;

	private ProgressDialog progressDialog;

	private ArrayList<ArrayList<News>> totalNewsList;

	// 프로그레스 닫기
	private Handler loadViewHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			progressDialog.dismiss();

			mViewPager.setAdapter(new MyPagerAdapter());
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 바 제거
		setContentView(R.layout.main);

		tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup(); // TabActivty 안 쓸 경우 필요
		mViewPager = (ViewPager) findViewById(R.id.pager);
		totalNewsList = new ArrayList<ArrayList<News>>();
		subject = new ArrayList<String>();

		mContext = this;

		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						// TODO Auto-generated method stub
						tabHost.setCurrentTab(position);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub

					}
				});

		loadSetting();
		loadNews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			loadSetting();
			loadNews();
			break;

		case R.id.menu_setting:
			Intent i = new Intent(this, SettingPreference.class);
			startActivity(i);
			break;
		}
		return false;
	}

	// 셋팅을 로드
	private void loadSetting() {
		subject.clear();
		tabHost.clearAllTabs();

		for (int i = 0; i < SettingPreference.NEWS_LIST.length; i++) {
			if (SharedPreference.getBooleanSharedPreference(this,
					SettingPreference.NEWS_LIST[i])) {
				subject.add(SUBJECT_URL_LIST[i]);
				switch (i) {
				case 0:
					tabHost.addTab(tabHost.newTabSpec("주요뉴스")
							.setIndicator("주요뉴스").setContent(this));
					break;
				case 1:
					tabHost.addTab(tabHost.newTabSpec("정치").setIndicator("정치")
							.setContent(this));
					break;
				case 2:
					tabHost.addTab(tabHost.newTabSpec("경제").setIndicator("경제")
							.setContent(this));
					break;
				case 3:
					tabHost.addTab(tabHost.newTabSpec("사회").setIndicator("사회")
							.setContent(this));
					break;
				case 4:
					tabHost.addTab(tabHost.newTabSpec("문화/생활")
							.setIndicator("문화/생활").setContent(this));
					break;
				case 5:
					tabHost.addTab(tabHost.newTabSpec("국제").setIndicator("국제")
							.setContent(this));
					break;
				case 6:
					tabHost.addTab(tabHost.newTabSpec("정보과학")
							.setIndicator("정보과학").setContent(this));
					break;
				case 7:
					tabHost.addTab(tabHost.newTabSpec("스포츠")
							.setIndicator("스포츠").setContent(this));
					break;
				case 8:
					tabHost.addTab(tabHost.newTabSpec("연예").setIndicator("연예")
							.setContent(this));
					break;
				}
			}
		}

//		tabHost.getCurrentTabView().setOnTouchListener(
//				new View.OnTouchListener() {
//					@Override
//					public boolean onTouch(View v, MotionEvent event) {
//						if (event.getAction() == MotionEvent.ACTION_DOWN) {
//							mViewPager.setCurrentItem(tabHost.getCurrentTab() + 1);
//						}
//						return false;
//					}
//				});
	}

	// 뉴스 내용 로드
	private void loadNews() {
		progressDialog = ProgressDialog.show(this, null, "뉴스를 읽어오는 중", true,
				true);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					for (String s : subject) {
						URL url = new URL(s);
						HttpURLConnection httpConnection = (HttpURLConnection) url
								.openConnection();

						if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
							SAXParserFactory parseModel = SAXParserFactory
									.newInstance();
							SAXParser parser = parseModel.newSAXParser();
							XMLReader myReader = parser.getXMLReader();

							// XMLReader myReader =
							// SAXParserFactory.newInstance().newSAXParser().getXMLReader();

							NewsXmlHandler handler = new NewsXmlHandler();
							myReader.setContentHandler(handler);

							InputSource is = new InputSource(url.openStream());
							myReader.parse(is);

							ArrayList<News> list = handler.getMessages();
							totalNewsList.add(list);
						}
					}
					loadViewHandler.sendEmptyMessage(0);

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}).start();
	}

	@Override
	public View createTabContent(String name) {
		TextView tv = new TextView(this);
		tv.setText(name);
		return tv;
	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return totalNewsList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ListView) object);
		}

		/**
		 * 페이지를 파기 position번째의 View를 제거하기 위한 처리
		 * 
		 * @param container
		 *            : 제거할 View의 컨테이너
		 * @param position
		 *            : 인스턴스 제거 위치
		 * @param object
		 *            : instantiateItem 메소드에서 반환된 object
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ListView) object);
		}

		/**
		 * 페이지를 생성
		 * 
		 * @param container
		 *            : 제거할 View의 컨테이너
		 * @param position
		 *            : 인스턴스 제거 위치
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			ListView lv = new ListView(mContext);
			NewsAdapter adapter = new NewsAdapter(mContext, R.layout.news_row,
					totalNewsList.get(position));
			lv.setAdapter(adapter);
			((ViewPager) container).addView(lv, 0);

			return lv;
		}

	}
}