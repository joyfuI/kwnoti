package tc.wo.joyfui.kwnoti.u_campus

import org.jsoup.nodes.Document

interface ListStrategy {
	fun getList(doc: Document): List<MyItem>
	fun getServlet(): String
}
