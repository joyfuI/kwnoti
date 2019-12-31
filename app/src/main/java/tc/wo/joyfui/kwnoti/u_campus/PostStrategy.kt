package tc.wo.joyfui.kwnoti.u_campus

import org.jsoup.nodes.Document

interface PostStrategy {
	fun getTitle(doc: Document): String
	fun getContent(doc: Document): String
	fun getServlet(): String
}
