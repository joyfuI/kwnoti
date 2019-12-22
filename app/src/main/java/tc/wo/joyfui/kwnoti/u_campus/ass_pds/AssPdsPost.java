package tc.wo.joyfui.kwnoti.u_campus.ass_pds;

import org.jsoup.nodes.Document;

import tc.wo.joyfui.kwnoti.u_campus.PostStrategy;

public class AssPdsPost implements PostStrategy {
	@Override
	public String getTitle(Document doc) {
		return doc.select(".tl_tit_fs9").text().trim();
	}

	@Override
	public String getContent(Document doc) {
		String content = "";
		String hr = "<div class=\"t_dot\"></div>";

		content += "<html><head>";
		content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/KOREAN/ss_user.css\">";	// CSS
		content += "</head><body>";
		content += "<div class=\"fs8\">";
		content += doc.select(".tl_r").html();	// 글 정보
		content += "</div>";
		content += hr;
		content += "<div class=\"tl_l2\">";
		content += doc.select(".tl_l2").html();	// 글 내용
		content += "</div>";
		content += hr;
		content += "<div class=\"tl_l3\">";
		String regex = "javascript:download\\('(.*?)',\\s?'(.*?)'\\);";
		String replacement = "/servlet/controller.library.DownloadServlet?p_savefile=$1&p_realfile=$2";
		content += doc.select(".tl_l3").html().replaceAll(regex, replacement);	// 첨부파일 (자바스크립트 변환)
		content += "</div>";
		content += "</body></html>";
		return content;
	}

	@Override
	public String getServlet() {
		return "AssPdsStuServlet";
	}
}
