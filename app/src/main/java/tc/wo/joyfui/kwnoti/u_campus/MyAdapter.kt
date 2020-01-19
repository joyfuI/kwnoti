package tc.wo.joyfui.kwnoti.u_campus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tc.wo.joyfui.kwnoti.R

class MyAdapter(private val data: List<MyItem>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
	interface OnClickListener {
		fun onClick(view: View, position: Int)
	}

	var onClickListener: OnClickListener? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
		return MyViewHolder(view)
	}

	override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
		holder.title.text = data[position].name
		holder.subtitle.text = data[position].time
	}

	override fun getItemCount(): Int {
		return data.size
	}

	inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
		init {
			itemView.setOnClickListener(this)
		}

		val title: TextView = itemView.findViewById(R.id.title)
		val subtitle: TextView = itemView.findViewById(R.id.subtitle)

		override fun onClick(p0: View?) {	// 클릭 리스너
			onClickListener?.onClick(p0!!, adapterPosition)
		}
	}
}
