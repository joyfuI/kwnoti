package tc.wo.joyfui.kwnoti.u_campus;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.*;

public class LectureListView extends AsyncTask<String, Void, Boolean> {
	public interface OnLectureListViewListener {
		void onSuccess(List<MyItem> list);
		void onFailure(String message);
	}

	private OnLectureListViewListener callback = null;
	private List<MyItem> list = new ArrayList<>();
	private String message = "";

	public void setOnLectureListViewListener(OnLectureListViewListener callback) {
		this.callback = callback;
	}

	@Override
	protected Boolean doInBackground(String... strings) {
		OkHttpClient client = Requests.getInstance();
		Request request;
		RequestBody formBody;
		Response response;

		try {
			formBody = new FormBody.Builder()
					.add("this_year", strings[0])
					.add("hakgi", strings[1])
					.add("sugang_opt", "all")
					.addEncoded("hh", URLEncoder.encode(strings[2], "euc-kr"))
					.addEncoded("prof_name", URLEncoder.encode(strings[3], "euc-kr"))
					.addEncoded("fsel1", URLEncoder.encode(strings[4], "euc-kr"))
					.addEncoded("fsel2", URLEncoder.encode(strings[5], "euc-kr"))
					.add("fsel4", "00_00")
					.build();
			request = new Request.Builder()
					.url("https://info.kw.ac.kr/webnote/lecture/h_lecture.php?mode=view&layout_opt=N")
					.post(formBody)
					.build();
			response = client.newCall(request).execute();
			if (!response.isSuccessful()) {	// 접속 실패
				message = response.message();
				return false;
			}
			String html = new String(response.body().bytes(), "euc-kr");
			Document doc = Jsoup.parse(html);

			Elements lectureTable = doc.select("body > form:nth-child(8) > table:nth-child(7)").select("tr");
			if (lectureTable.get(1).child(0).attr("colspan").isEmpty()) {	// 검색결과가 있을 때만
				for (Element i : lectureTable) {
					if (i.is(".bgtable1")) {	// 제목 행 제외
						continue;
					}
					String name = i.select("td:nth-child(1)").text() + " " + i.select("td:nth-child(2)").text() + " | " + i.select("td:nth-child(6)").text();
					String time = i.select("td:nth-child(2) > a").attr("href");
					list.add(new MyItem(name, time));
				}
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
		if (callback == null) {
			return;
		}
		if (aBoolean) {
			callback.onSuccess(list);	// 강의계획서 목록 가져오기 성공
		} else {
			callback.onFailure(message);	// 강의계획서 목록 가져오기 실패
		}
	}
}
