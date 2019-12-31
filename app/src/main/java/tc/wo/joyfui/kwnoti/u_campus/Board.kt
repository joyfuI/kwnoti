package tc.wo.joyfui.kwnoti.u_campus

import android.os.AsyncTask
import okhttp3.FormBody
import okhttp3.Request
import org.jsoup.Jsoup

class Board : AsyncTask<String, Void, Boolean>() {
	interface OnBoardListener {
		fun onSuccess(list: List<MyItem>)
		fun onFailure(message: String)
	}

	var onBoardListener: OnBoardListener? = null
	lateinit var listStrategy: ListStrategy
	private val list = ArrayList<MyItem>()
	private var message = ""

	override fun doInBackground(vararg p0: String?): Boolean {
		val client = Requests.getInstance()

		var i = 1
		while (true) {	// 모든 페이지 탐색
			val formBody = FormBody.Builder()
				.add("p_grcode", "N000003")
				.add("p_subj", p0[0]!!)
				.add("p_year", p0[1]!!)
				.add("p_subjseq", p0[2]!!)
				.add("p_class", p0[3]!!)
				.add("p_pageno", i.toString())
				.build()
			val request = Request.Builder()
				.url("https://info2.kw.ac.kr/servlet/controller.learn." + listStrategy.getServlet() + "?p_process=listPage")
				.post(formBody)
				.build()
			val response = client.newCall(request).execute()
			if (!response.isSuccessful) {	// 접속 실패
				message = response.message
				return false
			}
			val html = response.body!!.string()

			val listTmp = listStrategy.getList(Jsoup.parse(html))	// 리스트 생성
			if (listTmp.isEmpty() || (listTmp[0].p_bdseq.isEmpty() && i != 1)) {	// 글이 없으면(없는 페이지면), 과제게시판은 첫 페이지만
				break
			}
			list.addAll(listTmp)	// 리스트 연결
			i++
		}
		return true
	}

	override fun onPostExecute(result: Boolean?) {
		onBoardListener?.let {
			if (result!!) {
				it.onSuccess(list)	// 글목록 가져오기 성공
			} else {
				it.onFailure(message)	// 글목록 가져오기 실패
			}
		}
	}
}
