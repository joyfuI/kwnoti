package tc.wo.joyfui.kwnoti.u_campus;

import android.os.AsyncTask;
import android.util.SparseArray;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.*;
import tc.wo.joyfui.kwnoti.R;

public class LectureSpinner extends AsyncTask<Void, Void, Boolean> {
	public interface OnLectureSpinnerListener {
		void onSuccess(SparseArray<List<MyItem>> map);
		void onFailure(String message);
	}

	private OnLectureSpinnerListener callback = null;
	private SparseArray<List<MyItem>> map = new SparseArray<>();
	private String message = "";

	public void setOnLectureSpinnerListener(OnLectureSpinnerListener callback) {
		this.callback = callback;
	}

	@Override
	protected Boolean doInBackground(Void... voids) {
		OkHttpClient client = Requests.getInstance();
		Request request;
		Response response;
		List<MyItem> list;
		Elements option;

		try {
			request = new Request.Builder()
					.url("https://info.kw.ac.kr/webnote/lecture/h_lecture.php?layout_opt=N")
					.build();
			response = client.newCall(request).execute();
			if (!response.isSuccessful()) {	// 접속 실패
				message = response.message();
				return false;
			}
			String html = new String(response.body().bytes(), "euc-kr");	// 어처구니 없게도 meta 태그엔 utf-8라고 적혀있는데 실제론 euc-kr...
			Document doc = Jsoup.parse(html);

			list = new ArrayList<>();	// 검색년도
			option = doc.select("body > form:nth-child(5) > table:nth-child(7) > tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(2) > select:nth-child(1)").select("option");
			for(Element i : option) {
				String name = i.text();
				String time = i.attr("value");
				list.add(new MyItem(name, time));
			}
			map.put(R.id.this_year, list);

			list = new ArrayList<>();	// 검색학기
			option = doc.select("body > form:nth-child(5) > table:nth-child(7) > tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(2) > select:nth-child(2)").select("option");
			for(Element i : option) {
				String name = i.text();
				String time = i.attr("value");
				list.add(new MyItem(name, time));
			}
			map.put(R.id.hakgi, list);

			list = new ArrayList<>();	// 공통과목
			option = doc.select("body > form:nth-child(5) > table:nth-child(7) > tbody:nth-child(2) > tr:nth-child(3) > td:nth-child(2) > select:nth-child(1)").select("option");
			for(Element i : option) {
				String name = i.text();
				String time = i.attr("value");
				list.add(new MyItem(name, time));
			}
			map.put(R.id.fsel1, list);

			list = new ArrayList<>();	// 학과/전공
			option = doc.select("body > form:nth-child(5) > table:nth-child(7) > tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(2) > select:nth-child(1)").select("option");
			for(Element i : option) {
				String name = i.text();
				String time = i.attr("value");
				list.add(new MyItem(name, time));
			}
			map.put(R.id.fsel2, list);
		} catch (IOException e) {
			e.printStackTrace();
			message = e.getMessage();
			return false;
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean aBoolean) {
		if (callback == null) {
			return;
		}
		if (aBoolean) {
			callback.onSuccess(map);	// 강의계획서 조회 정보 가져오기 성공
		} else {
			callback.onFailure(message);	// 강의계획서 조회 정보 가져오기 실패
		}
	}
}
