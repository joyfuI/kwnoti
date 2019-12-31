package tc.wo.joyfui.kwnoti.u_campus.ass_pds

import org.jsoup.nodes.Document
import tc.wo.joyfui.kwnoti.u_campus.PostStrategy

class AssPdsPost : PostStrategy {
	override fun getTitle(doc: Document): String = doc.select(".tl_tit_fs9").text().trim()

	override fun getContent(doc: Document): String {
		var content = ""
		val hr = "<div class=\"t_dot\"></div>"

		content += "<html><head>"
		content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/KOREAN/ss_user.css\">"	// CSS
		content += "</head><body>"
		content += "<div class=\"fs8\">"
		content += doc.select(".tl_r").html()	// 글 정보
		content += "</div>"
		content += hr
		content += "<div class=\"tl_l2\">"
		content += doc.select(".tl_l2").html()	// 글 내용
		content += "</div>"
		content += hr
		content += "<div class=\"tl_l3\">"
		val regex = "javascript:download\\('(.*?)',\\s?'(.*?)'\\);"
		val replacement = "/servlet/controller.library.DownloadServlet?p_savefile=$1&p_realfile=$2"
		content += doc.select(".tl_l3").html().replace(regex.toRegex(), replacement)	// 첨부파일 (자바스크립트 변환)
		content += "</div>"
		content += "</body></html>"
		return content
	}

	override fun getServlet() = "AssPdsStuServlet"
}
