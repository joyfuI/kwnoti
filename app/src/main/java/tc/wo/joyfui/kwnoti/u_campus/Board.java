package tc.wo.joyfui.kwnoti.u_campus;

import android.os.AsyncTask;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.*;

public class Board extends AsyncTask<String, Void, Boolean> {
	public interface OnBoardListener {
		void onSuccess(List<MyItem> list);
		void onFailure(String message);
	}

	private OnBoardListener callback = null;
	private ListStrategy listStrategy = null;
	private List<MyItem> list = new ArrayList<>();
	private String message = "";

	public void setOnBoardListener(OnBoardListener callback) {
		this.callback = callback;
	}

	public void setListStrategy(ListStrategy listStrategy) {
		this.listStrategy = listStrategy;
	}

	@Override
	protected Boolean doInBackground(String... strings) {
		OkHttpClient client = Requests.getInstance();
		Request request;
		RequestBody formBody;
		Response response;
		List<MyItem> listTmp;

		try {
			for (int i = 1; ; i++) {	// 모든 페이지 탐색
				formBody = new FormBody.Builder()
						.add("p_grcode", "N000003")
						.add("p_subj", strings[0])
						.add("p_year", strings[1])
						.add("p_subjseq", strings[2])
						.add("p_class", strings[3])
						.add("p_pageno", Integer.toString(i))
						.build();
				request = new Request.Builder()
						.url("https://info2.kw.ac.kr/servlet/controller.learn." + listStrategy.getServlet() + "?p_process=listPage")
						.post(formBody)
						.build();
				response = client.newCall(request).execute();
				if (!response.isSuccessful()) {	// 접속 실패
					message = response.message();
					return false;
				}
				String html = response.body().string();

				listTmp = listStrategy.getList(Jsoup.parse(html));	// 리스트 생성
				if (listTmp.size() == 0 || (listTmp.get(0).getP_bdseq().isEmpty() && i != 1)) {	// 글이 없으면(없는 페이지면), 과제게시판은 첫 페이지만
					break;
				}
				list.addAll(listTmp);	// 리스트 연결
			}
		} catch (IOException e) {
			e.printStackTrace();
			message = e.getMessage();
			return false;
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean aBoolean) {
		if (callback == null || listStrategy == null) {
			return;
		}
		if (aBoolean) {
			callback.onSuccess(list);	// 글목록 가져오기 성공
		} else {
			callback.onFailure(message);	// 글목록 가져오기 실패
		}
	}
}
