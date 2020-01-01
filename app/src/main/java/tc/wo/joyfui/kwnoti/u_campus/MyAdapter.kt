package tc.wo.joyfui.kwnoti.u_campus

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import tc.wo.joyfui.kwnoti.R

class MyAdapter(context: Context, private val data: List<MyItem>) : BaseAdapter() {
	private val layoutInflater = LayoutInflater.from(context)

	override fun getCount(): Int {
		return data.size
	}

	override fun getItem(p0: Int): MyItem {
		return data[p0]
	}

	override fun getItemId(p0: Int): Long {
		return p0.toLong()
	}

	override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
		var itemView = p1
		var viewHolder: ViewHolder

		viewHolder = itemView?.tag as? ViewHolder ?: {
			itemView = layoutInflater.inflate(R.layout.list_item, null)

			viewHolder = ViewHolder()
			viewHolder.title = itemView!!.findViewById(R.id.title)
			viewHolder.subtitle = itemView!!.findViewById(R.id.subtitle)

			itemView!!.tag = viewHolder

			viewHolder
		} ()

		viewHolder.title.text = data[p0].name
		viewHolder.subtitle.text = data[p0].time

		return itemView!!
	}

	private class ViewHolder {
		lateinit var title: TextView
		lateinit var subtitle: TextView
	}
}
