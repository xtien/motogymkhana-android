package eu.motogymkhana.competition.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class MyViewPagerAdapter extends FragmentStatePagerAdapter {

	private List<Fragment> fragments;

	public MyViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragments) {
		super(fragmentManager);
		this.fragments = fragments;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	public void push(Fragment fragment) {
		fragments.add(fragment);
	}

	public void pop() {

		fragments.remove(fragments.size() - 1);
		notifyDataSetChanged();
	}
}
