package tc.wo.joyfui.kwnoti.u_campus.report;

import org.jsoup.nodes.Document;

import tc.wo.joyfui.kwnoti.u_campus.PostStrategy;

public class ReportPost implements PostStrategy {
	@Override
	public String getTitle(Document doc) {
		return doc.select(".mid2 > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(2) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(1)").text().trim();
	}

	@Override
	public String getContent(Document doc) {
		String content = "";

		content += "<html><head>";
		content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/KOREAN/ss_user.css\">";	// CSS
		content += "</head><body>";
		String regex = "javascript:profdownload\\('(.*?)',\\s?'(.*?)'\\);";
		String replacement = "/servlet/controller.library.DownloadServlet?p_savefile=$1&p_realfile=$2";
		content += doc.select(".mid2 > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(2)").html()	// 글 내용
				.replaceAll(regex, replacement);	// 첨부파일 (자바스크립트 변환)
//		String footer = doc.select(".mid2 > table:nth-child(6)").outerHtml();
//		if (!footer.contains("id=\"textfield\"")) {
//			content += footer;	// 제출 과제
//		}
		content += "</body></html>";
		return content;
	}

	@Override
	public String getServlet() {
		return "ReportStuServlet";
	}
}
