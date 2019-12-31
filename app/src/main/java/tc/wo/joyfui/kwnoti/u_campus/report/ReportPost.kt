package tc.wo.joyfui.kwnoti.u_campus.report

import org.jsoup.nodes.Document
import tc.wo.joyfui.kwnoti.u_campus.PostStrategy

class ReportPost : PostStrategy {
	override fun getTitle(doc: Document): String =
		doc.select(".mid2 > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(2) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(1)").text().trim()

	override fun getContent(doc: Document): String {
		var content = ""

		content += "<html><head>"
		content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/KOREAN/ss_user.css\">"	// CSS
		content += "</head><body>"
		val regex = "javascript:profdownload\\('(.*?)',\\s?'(.*?)'\\);"
		val replacement = "/servlet/controller.library.DownloadServlet?p_savefile=$1&p_realfile=$2"
		content += doc.select(".mid2 > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(2)").html()	// 글 내용
			.replace(regex.toRegex(), replacement)	// 첨부파일 (자바스크립트 변환)
//		val footer = doc.select(".mid2 > table:nth-child(6)").outerHtml()
//		if (!footer.contains("id=\"textfield\"")) {
//			content += footer	// 제출 과제
//		}
		content += "</body></html>"
		return content
	}

	override fun getServlet() = "ReportStuServlet"
}
