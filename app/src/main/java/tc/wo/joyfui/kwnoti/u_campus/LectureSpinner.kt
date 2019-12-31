package tc.wo.joyfui.kwnoti.u_campus

import android.os.AsyncTask
import android.util.SparseArray
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import tc.wo.joyfui.kwnoti.R
import java.nio.charset.Charset

class LectureSpinner : AsyncTask<Void, Void, Boolean>() {
	interface OnLectureSpinnerListener {
		fun onSuccess(map: SparseArray<List<MyItem>>)
		fun onFailure(message: String)
	}

	var onLectureSpinnerListener: OnLectureSpinnerListener? = null
	private val map = SparseArray<List<MyItem>>()
	private var message = ""

	override fun doInBackground(vararg p0: Void?): Boolean {
		val client = Requests.getInstance()
		var list: ArrayList<MyItem>
		var option: Elements

		val request = Request.Builder()
			.url("https://info.kw.ac.kr/webnote/lecture/h_lecture.php?layout_opt=N")
			.build()
		val response = client.newCall(request).execute()
		if (!response.isSuccessful) {	// 접속 실패
			message = response.message
			return false
		}
		val html = String(response.body!!.bytes(),	Charset.forName("euc-kr"))	// 어처구니 없게도 meta 태그엔 utf-8라고 적혀있는데 실제론 euc-kr...
		val doc = Jsoup.parse(html)

		list = ArrayList()	// 검색년도
		option = doc.select("body > form:nth-child(5) > table:nth-child(7) > tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(2) > select:nth-child(1)").select("option")
		for (i in option) {
			val name = i.text()
			val time = i.attr("value")
			list.add(MyItem(name, time))
		}
		map.put(R.id.this_year, list)

		list = ArrayList()	// 검색학기
		option = doc.select("body > form:nth-child(5) > table:nth-child(7) > tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(2) > select:nth-child(2)").select("option")
		for (i in option) {
			val name = i.text()
			val time = i.attr("value")
			list.add(MyItem(name, time))
		}
		map.put(R.id.hakgi, list)

		list = java.util.ArrayList()	// 공통과목
		option = doc.select("body > form:nth-child(5) > table:nth-child(7) > tbody:nth-child(2) > tr:nth-child(3) > td:nth-child(2) > select:nth-child(1)").select("option")
		for (i in option) {
			val name = i.text()
			val time = i.attr("value")
			list.add(MyItem(name, time))
		}
		map.put(R.id.fsel1, list)

		list = java.util.ArrayList()	// 학과/전공
		option = doc.select("body > form:nth-child(5) > table:nth-child(7) > tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(2) > select:nth-child(1)").select("option")
		for (i in option) {
			val name = i.text()
			val time = i.attr("value")
			list.add(MyItem(name, time))
		}
		map.put(R.id.fsel2, list)
		return true
	}

	override fun onPostExecute(result: Boolean?) {
		onLectureSpinnerListener?.let {
			if (result!!) {
				it.onSuccess(map)	// 강의계획서 조회 정보 가져오기 성공
			} else {
				it.onFailure(message)	// 강의계획서 조회 정보 가져오기 실패
			}
		}
	}
}
