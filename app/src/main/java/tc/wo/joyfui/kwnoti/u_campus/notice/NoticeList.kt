package tc.wo.joyfui.kwnoti.u_campus.notice

import org.jsoup.nodes.Document
import tc.wo.joyfui.kwnoti.u_campus.ListStrategy
import tc.wo.joyfui.kwnoti.u_campus.MyItem
import java.util.regex.Pattern

class NoticeList : ListStrategy {
	override fun getList(doc: Document): List<MyItem> {
		val list = ArrayList<MyItem>()
		val boardTable = doc.select(".mid2 > table:nth-child(2)").select("tr")
		for (i in boardTable) {
			if (i.`is`("tr:nth-child(2)") || i.text().isEmpty()) {	// 제목, 빈 행 제외
				continue
			}
			val name = i.getElementsByIndexEquals(2).text()
			val time = i.getElementsByIndexEquals(4).text()
			val m = Pattern.compile("'(.*?)'").matcher(i.select("td:nth-child(3) > samp > a").attr("href"))
			m.find()
			val p_bdseq = m.group(1)!!
			list.add(MyItem(name, time, p_bdseq))
		}
		return list
	}

	override fun getServlet() = "NoticeStuServlet"
}
