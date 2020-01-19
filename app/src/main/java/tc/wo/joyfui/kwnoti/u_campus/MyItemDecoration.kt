package tc.wo.joyfui.kwnoti.u_campus

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MyItemDecoration : RecyclerView.ItemDecoration() {
	override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
		super.getItemOffsets(outRect, view, parent, state)

		val index = parent.getChildAdapterPosition(view)

		if (index == 0) {
			outRect.set(30, 30, 30, 30)
		} else {
			outRect.set(30, 0, 30, 30)
		}

//		ViewCompat.setElevation(view, 20.0f)
		view.elevation = 20.0f
	}
}
