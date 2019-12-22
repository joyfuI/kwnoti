package tc.wo.joyfui.kwnoti.u_campus;

import org.jsoup.nodes.Document;

import java.util.List;

public interface ListStrategy {
	List<MyItem> getList(Document doc);
	String getServlet();
}
