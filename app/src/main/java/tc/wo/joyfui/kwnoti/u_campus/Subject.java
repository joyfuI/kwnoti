package tc.wo.joyfui.kwnoti.u_campus;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.*;

public class Subject extends AsyncTask<Void, Void, Boolean> {
	public interface OnSubjectListener {
		void onSuccess(List<MyItem> list);
		void onFailure(String message);
	}

	private OnSubjectListener callback = null;
	private List<MyItem> list = new ArrayList<>();
	private String message = "";

	public void setOnSubjectListener(OnSubjectListener callback) {
		this.callback = callback;
	}

	@Override
	protected Boolean doInBackground(Void... voids) {
		OkHttpClient client = Requests.getInstance();
		Request request;
		Response response;

		try {
			request = new Request.Builder()
					.url("https://info2.kw.ac.kr/servlet/controller.homepage.MainServlet?p_gate=univ&p_process=main&p_page=learning&p_kwLoginType=cookie&gubun_code=11")
					.build();
			client.newCall(request).execute();	// 메인페이지 한번 접속해야 프레임페이지 방문 가능

			request = new Request.Builder()
					.url("https://info2.kw.ac.kr/servlet/controller.homepage.KwuMainServlet?p_process=openStu&p_grcode=N000003")
					.build();
			response = client.newCall(request).execute();
			if (!response.isSuccessful()) {	// 접속 실패
				message = response.message();
				return false;
			}
			String html = response.body().string();
			Document doc = Jsoup.parse(html);

			Elements subjectTable = doc.select("table.main_box:nth-child(7) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(1) > table:nth-child(1)").select("tr");
			for(Element i : subjectTable) {
				Matcher m = Pattern.compile("\\](.*) \\(").matcher(i.select("td:nth-child(2)").text());
				m.find();
				String name = m.group(1);
				String time = i.select("td:nth-child(3)").text();
				m = Pattern.compile("'(.*?)'").matcher(i.select("td:nth-child(4) > a").attr("href"));
				m.find();
				String p_subj = m.group(1);
				m.find();
				String p_year = m.group(1);
				m.find();
				String p_subjseq = m.group(1);
				m.find();
				String p_class = m.group(1);
				list.add(new MyItem(name, time, p_subj, p_year, p_subjseq, p_class));
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
			callback.onSuccess(list);	// 과목 가져오기 성공
		} else {
			callback.onFailure(message);	// 과목 가져오기 실패
		}
	}
}
