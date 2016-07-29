/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.fulicenter.adapter;

import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMGroup;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.UserUtils;

public class GroupAdapter extends ArrayAdapter<EMGroup> {

	private LayoutInflater inflater;
	private String newGroup;
	private String addPublicGroup;
	Context mContext;

	public GroupAdapter(Context context, int res, List<EMGroup> groups) {

		super(context, res, groups);
		mContext=context;
		this.inflater = LayoutInflater.from(context);
		newGroup = context.getResources().getString(cn.ucai.fulicenter.R.string.The_new_group_chat);
		addPublicGroup = context.getResources().getString(cn.ucai.fulicenter.R.string.add_public_group_chat);
	}

	@Override
	public int getViewTypeCount() {
		return 4;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return 0;
		} else if (position == 1) {
			return 1;
		} else if (position == 2) {
			return 2;
		} else {
			return 3;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getItemViewType(position) == 0) {
			if (convertView == null) {
				convertView = inflater.inflate(cn.ucai.fulicenter.R.layout.search_bar_with_padding, null);
			}
			final EditText query = (EditText) convertView.findViewById(cn.ucai.fulicenter.R.id.query);
			final ImageButton clearSearch = (ImageButton) convertView.findViewById(cn.ucai.fulicenter.R.id.search_clear);
			query.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					getFilter().filter(s);
					if (s.length() > 0) {
						clearSearch.setVisibility(View.VISIBLE);
					} else {
						clearSearch.setVisibility(View.INVISIBLE);
					}
				}

				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				public void afterTextChanged(Editable s) {
				}
			});
			clearSearch.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					query.getText().clear();
				}
			});
		} else if (getItemViewType(position) == 1) {
			if (convertView == null) {
				convertView = inflater.inflate(cn.ucai.fulicenter.R.layout.row_add_group, null);
			}
			((ImageView) convertView.findViewById(cn.ucai.fulicenter.R.id.avatar)).setImageResource(cn.ucai.fulicenter.R.drawable.create_group);
			((TextView) convertView.findViewById(cn.ucai.fulicenter.R.id.name)).setText(newGroup);
		} else if (getItemViewType(position) == 2) {
			if (convertView == null) {
				convertView = inflater.inflate(cn.ucai.fulicenter.R.layout.row_add_group, null);
			}
			((ImageView) convertView.findViewById(cn.ucai.fulicenter.R.id.avatar)).setImageResource(cn.ucai.fulicenter.R.drawable.add_public_group);
			((TextView) convertView.findViewById(cn.ucai.fulicenter.R.id.name)).setText(addPublicGroup);
			((TextView) convertView.findViewById(cn.ucai.fulicenter.R.id.header)).setVisibility(View.VISIBLE);

		} else {
			if (convertView == null) {

				convertView = inflater.inflate(cn.ucai.fulicenter.R.layout.row_group, null);
			}
			((TextView) convertView.findViewById(cn.ucai.fulicenter.R.id.name)).setText(getItem(position - 3).getGroupName());
			UserUtils.setAppGroupAvatar(mContext,getItem(position - 3).
					getGroupId(),((ImageView) convertView.findViewById(R.id.avatar)));
		}

		return convertView;
	}

	@Override
	public int getCount() {
		return super.getCount() + 3;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GroupAdapter that = (GroupAdapter) o;

		if (inflater != null ? !inflater.equals(that.inflater) : that.inflater != null)
			return false;
		if (newGroup != null ? !newGroup.equals(that.newGroup) : that.newGroup != null)
			return false;
		if (addPublicGroup != null ? !addPublicGroup.equals(that.addPublicGroup) : that.addPublicGroup != null)
			return false;
		return mContext != null ? mContext.equals(that.mContext) : that.mContext == null;

	}

	@Override
	public int hashCode() {
		int result = inflater != null ? inflater.hashCode() : 0;
		result = 31 * result + (newGroup != null ? newGroup.hashCode() : 0);
		result = 31 * result + (addPublicGroup != null ? addPublicGroup.hashCode() : 0);
		result = 31 * result + (mContext != null ? mContext.hashCode() : 0);
		return result;
	}
}