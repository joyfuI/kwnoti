package tc.wo.joyfui.kwnoti.u_campus.notice;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tc.wo.joyfui.kwnoti.u_campus.ListStrategy;
import tc.wo.joyfui.kwnoti.u_campus.MyItem;

public class NoticeList implements ListStrategy {
	@Override
	public List<MyItem> getList(Document doc) {
		List<MyItem> list = new ArrayList<>();
		Elements boardTable = doc.select(".mid2 > table:nth-child(2)").select("tr");
		for (Element i : boardTable) {
			if (i.is("tr:nth-child(2)") || i.text().isEmpty()) {	// 제목, 빈 행 제외
				continue;
			}
			String name = i.getElementsByIndexEquals(2).text();
			String time = i.getElementsByIndexEquals(4).text();
			Matcher m = Pattern.compile("'(.*?)'").matcher(i.select("td:nth-child(3) > samp > a").attr("href"));
			m.find();
			String p_bdseq = m.group(1);
			list.add(new MyItem(name, time, p_bdseq));
		}
		return list;
	}

	@Override
	public String getServlet() {
		return "NoticeStuServlet";
	}
}
