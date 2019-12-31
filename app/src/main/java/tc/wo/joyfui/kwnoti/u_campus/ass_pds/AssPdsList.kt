package tc.wo.joyfui.kwnoti.u_campus.ass_pds

import org.jsoup.nodes.Document
import tc.wo.joyfui.kwnoti.u_campus.ListStrategy
import tc.wo.joyfui.kwnoti.u_campus.MyItem
import java.util.regex.Pattern

class AssPdsList : ListStrategy {
	override fun getList(doc: Document): List<MyItem> {
		val list = ArrayList<MyItem>()
		val boardTable = doc.select(".mid2 > table:nth-child(4)").select("tr")
		for (i in boardTable) {
			if (i.`is`("tr:nth-child(2)") || i.childNodeSize() < 10) {	// 제목, 빈 행 제외
				continue
			}
			val name = i.getElementsByIndexEquals(2).text()
//			val time = i.getElementsByIndexEquals(3).text()
			val time = i.select("td:nth-child(4)").text()
			val m = Pattern.compile("'(.*?)'").matcher(i.select("td:nth-child(3) > samp > a").attr("href"))
			m.find()
			val p_bdseq = m.group(1)!!
			list.add(MyItem(name, time, p_bdseq))
		}
		return list
	}

	override fun getServlet() = "AssPdsStuServlet"
}
