package tc.wo.joyfui.kwnoti.u_campus

import android.os.AsyncTask
import okhttp3.FormBody
import okhttp3.Request
import org.jsoup.Jsoup
import java.net.URLEncoder
import java.nio.charset.Charset

class LectureListView : AsyncTask<String, Void, Boolean>() {
	interface OnLectureListViewListener {
		fun onSuccess(list: List<MyItem>)
		fun onFailure(message: String)
	}

	var onLectureListViewListener: OnLectureListViewListener? = null
	private val list = ArrayList<MyItem>()
	private var message = ""

	override fun doInBackground(vararg p0: String?): Boolean {
		val client = Requests.getInstance()

		val formBody = FormBody.Builder()
			.add("this_year", p0[0]!!)
			.add("hakgi", p0[1]!!)
			.add("sugang_opt", "all")
			.addEncoded("hh", URLEncoder.encode(p0[2], "euc-kr"))
			.addEncoded("prof_name", URLEncoder.encode(p0[3], "euc-kr"))
			.addEncoded("fsel1", URLEncoder.encode(p0[4], "euc-kr"))
			.addEncoded("fsel2", URLEncoder.encode(p0[5], "euc-kr"))
			.add("fsel4", "00_00")
			.build()
		val request = Request.Builder()
			.url("https://info.kw.ac.kr/webnote/lecture/h_lecture.php?mode=view&layout_opt=N")
			.post(formBody)
			.build()
		val response = client.newCall(request).execute()
		if (!response.isSuccessful) {	// 접속 실패
			message = response.message
			return false
		}
		val html = String(response.body!!.bytes(), Charset.forName("euc-kr"))
		val doc = Jsoup.parse(html)

		val lectureTable = doc.select("body > form:nth-child(8) > table:nth-child(7)").select("tr")
		if (lectureTable[1].child(0).attr("colspan").isEmpty()) {	// 검색결과가 있을 때만
			for (i in lectureTable) {
				if (i.`is`(".bgtable1")) {	// 제목 행 제외
					continue
				}
				val name = i.select("td:nth-child(1)").text() + " " + i.select("td:nth-child(2)").text() + " | " + i.select("td:nth-child(6)").text()
				val time = i.select("td:nth-child(2) > a").attr("href")
				list.add(MyItem(name, time))
			}
		}
		return true
	}

	override fun onPostExecute(result: Boolean?) {
		onLectureListViewListener?.let {
			if (result!!) {
				it.onSuccess(list)	// 강의계획서 목록 가져오기 성공
			} else {
				it.onFailure(message)	// 강의계획서 목록 가져오기 실패
			}
		}
	}
}
