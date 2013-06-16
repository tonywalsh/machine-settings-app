package com.freefoam.productionreports;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.widget.Toast;

public class ReportTabsListener implements TabListener {
	private Fragment fragment;
	
	public ReportTabsListener(Fragment fragment) {
		this.fragment = fragment;
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		Toast.makeText(fragment.getView().getContext(), "Reselected!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft.replace(R.id.fragment_container, fragment);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(fragment);
	}
}
