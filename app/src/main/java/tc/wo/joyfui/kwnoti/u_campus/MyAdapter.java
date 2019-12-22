package tc.wo.joyfui.kwnoti.u_campus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tc.wo.joyfui.kwnoti.R;

public class MyAdapter extends BaseAdapter {
	private List<MyItem> data = null;
	private LayoutInflater layoutInflater = null;

	public MyAdapter(Context context, List<MyItem> data) {
		this.data = data;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public MyItem getItem(int i) {
		return data.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		View itemView = view;
		ViewHolder viewHolder = null;

		if (itemView == null) {
			itemView = layoutInflater.inflate(R.layout.list_item, null);

			viewHolder = new ViewHolder();
			viewHolder.title = itemView.findViewById(R.id.title);
			viewHolder.subtitle = itemView.findViewById(R.id.subtitle);

			itemView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)itemView.getTag();
		}

		viewHolder.title.setText(data.get(i).getName());
		viewHolder.subtitle.setText(data.get(i).getTime());

		return itemView;
	}

	private class ViewHolder {
		TextView title;
		TextView subtitle;
	}
}
