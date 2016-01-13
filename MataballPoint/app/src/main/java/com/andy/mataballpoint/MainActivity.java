package com.andy.mataballpoint;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

	private MataballPointView mMataBall;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mMataBall = (MataballPointView) findViewById(R.id.mata_ball);
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mViewPager.setAdapter(new DemoPagerAdapter());
		mViewPager.addOnPageChangeListener(mPagerChangeListener);
		mMataBall.setCount(mViewPager.getAdapter().getCount());
	}

	private ViewPager.OnPageChangeListener mPagerChangeListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			mMataBall.setPositionOffset(position, positionOffset);
		}

		@Override
		public void onPageSelected(int position) {
			mMataBall.setPositionOffset(position, 0);
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	};

	@Override
	public void onClick(View v) {
	}

	class DemoPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View child = LayoutInflater.from(getBaseContext()).inflate(R.layout.pager_item, null);
			TextView text = (TextView) child.findViewById(R.id.text);
			ImageView image = (ImageView) child.findViewById(R.id.image);
			switch(position) {
				case 0:
					text.setText("“不屈服于鬼神的妨碍，一如既往地修炼自己的宝剑，这不是武道的极限吗?”\n" +
							"有这样的一些人。无论是心脏虚弱还是缺少一只腿，他们在任何条件下都不会屈服，勇往直前，向自己特定的目标前进。\n" +
							"同样也有这样的一些人，即使拿武器的手变得扭曲，也不会向命运屈服，而会更努力地对武器进行精研。其中一部分人在各自的武器领域里达到了极限，人们将这些鬼剑士称为“剑魂”。\n" +
							"20多岁成为德罗斯帝国精英才俊的巴恩，10年前在悲鸣洞穴失踪的太刀宗师西岚，北部班图族族长钝器专家布万加，挎着长剑到处流浪的巨剑达人阿甘佐等，他们的故事振奋着所有的战士们。");
					image.setImageResource(R.mipmap.jiansheng);
					break;
				case 1:
					text.setText("“为了得到更强的力量，不惜一切代价！即使出卖自己的灵魂！”\n" +
							"被鬼神卡赞所控制的鬼剑士。也称为卡赞综合病症。平时没什么异常，一旦有愤怒、激动的情绪，精神达到崩溃状态时会成为疯狂的鬼神，通过血的代价得到强大的力量。但会因失去了理智所以无法区分敌军与我军，但他的速度与攻击力非常高。当年摆平了异界生命体希洛克的狂战士卢克西是10年前的剑魂4人组合也无法相比的。狂战士在疯狂的瞬间，也许是所有职业里最强的。以其独特的攻击性和刷图流畅，简单给力的特点深受玩家喜爱。");
					image.setImageResource(R.mipmap.kuangzhan);
					break;
				case 2:
					text.setText("“能否将命运的铁链揭开，完全在于自身的意志。”\n" +
							"如果把缠绕在臂上的铁链松开，鬼神便会自由的行动，所以一部分鬼剑士会扔掉铁链成为鬼泣。巧妙的利用鬼神会对战斗有利。神官吉格将刀魂之卡赞、残影之凯贾、冰霜之萨亚、侵蚀之普戾蒙等鬼神的使用方法传播给了鬼泣。鬼泣最终也不乐观。吉格在与野蛮人战斗中多次被雷击，失去了对付鬼神的技能，最后被鬼神们埋在了地下。");
					image.setImageResource(R.mipmap.guiqi);
					break;
				case 3:
					text.setText("“眼睛虽已长眠，但只要心脏不停，我的身体就是我的眼睛。”\n" +
							"一些鬼剑士为了能感触到波动，而放弃了眼睛。\n" +
							"失去双目后，为了加强近距离战斗力，向武将打造板甲护甲提高自身的防御力。失去五感中的一感的他，向赫顿玛尔后院里的G.S.D习得了感知气流的功能，通过气流判断出敌人所在位置。\n" +
							"剩下的就是踏入战场的战神之路。");
					image.setImageResource(R.mipmap.axiulo);
					break;
			}
			container.addView(child);
			return child;
		}
	}
}
