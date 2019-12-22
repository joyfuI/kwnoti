package tc.wo.joyfui.kwnoti.u_campus.report;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tc.wo.joyfui.kwnoti.u_campus.ListStrategy;
import tc.wo.joyfui.kwnoti.u_campus.MyItem;

public class ReportList implements ListStrategy {
	@Override
	public List<MyItem> getList(Document doc) {
		List<MyItem> list = new ArrayList<>();
		Elements boardTable = doc.select(".mid2 > table:nth-child(3)").select("tr");
		String p_subj = doc.select("form > input[name=p_subj]").attr("value");
		String p_year = doc.select("form > input[name=p_year]").attr("value");
		String p_subjseq = doc.select("form > input[name=p_subjseq]").attr("value");
		String p_class = doc.select("form > input[name=p_class]").attr("value");
		for (Element i : boardTable) {
			if (i.is("tr:nth-child(2)") || i.is("tr:nth-child(3)") || i.childNodeSize() < 10 || i.text().isEmpty()) {	// 제목, 빈 행 제외
				continue;
			}
			String name = i.select("td:nth-child(2) > samp").text();
			String time = i.getElementsByIndexEquals(2).text().trim() + " | " + i.select("td:nth-child(4)").text();
			String onclick = i.select("td:nth-child(5) > table > tbody > tr > td:nth-child(2)").attr("onclick");
			Matcher m = Pattern.compile("'(.*?)'").matcher(onclick);
			m.find();
			String p_ordseq = m.group(1);
			if (onclick.contains("whenInsert")) {
				p_ordseq += ".";	// 약간의 꼼수;;
			}
			list.add(new MyItem(name, time, p_ordseq, p_subj, p_year, p_subjseq, p_class));
		}
		return list;
	}

	@Override
	public String getServlet() {
		return "ReportStuServlet";
	}
}
