package tc.wo.joyfui.kwnoti.u_campus

import android.os.AsyncTask
import okhttp3.Request
import org.jsoup.Jsoup
import java.util.regex.Pattern

class Subject : AsyncTask<Void, Void, Boolean>() {
	interface OnSubjectListener {
		fun onSuccess(list: List<MyItem>)
		fun onFailure(message: String)
	}

	var onSubjectListener: OnSubjectListener? = null
	private val list = ArrayList<MyItem>()
	private var message = ""

	override fun doInBackground(vararg p0: Void?): Boolean {
		val client = Requests.getInstance()

		var request = Request.Builder()
			.url("https://info2.kw.ac.kr/servlet/controller.homepage.MainServlet?p_gate=univ&p_process=main&p_page=learning&p_kwLoginType=cookie&gubun_code=11")
			.build()
		client.newCall(request).execute()	// 메인페이지 한번 접속해야 프레임페이지 방문 가능

		request = Request.Builder()
			.url("https://info2.kw.ac.kr/servlet/controller.homepage.KwuMainServlet?p_process=openStu&p_grcode=N000003")
			.build()
		val response = client.newCall(request).execute()
		if (!response.isSuccessful) {	// 접속 실패
			message = response.message
			return false
		}
		val html = response.body!!.string()
		val doc = Jsoup.parse(html)

		val subjectTable = doc.select("table.main_box:nth-child(7) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(1) > table:nth-child(1)").select("tr")
		for (i in subjectTable) {
			var m = Pattern.compile("](.*) \\(").matcher(i.select("td:nth-child(2)").text())
			m.find()
			val name = m.group(1)
			val time = i.select("td:nth-child(3)").text()
			m = Pattern.compile("'(.*?)'").matcher(i.select("td:nth-child(4) > a").attr("href"))
			m.find()
			val p_subj = m.group(1)
			m.find()
			val p_year = m.group(1)
			m.find()
			val p_subjseq = m.group(1)
			m.find()
			val p_class = m.group(1)
			list.add(MyItem(name!!, time!!, p_subj!!, p_year!!, p_subjseq!!, p_class!!))
		}
		return true
	}

	override fun onPostExecute(result: Boolean?) {
		onSubjectListener?.let {
			if (result!!) {
				it.onSuccess(list)	// 과목 가져오기 성공
			} else {
				it.onFailure(message)	// 과목 가져오기 실패
			}
		}
	}
}
