package hadoop.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import hadoop.connection.HadoopCluster;
import hadoop.dblp.model.Page;

/**
 * Servlet implementation class index
 */
// @WebServlet("/index")
public class index extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public index() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*HttpSession sess = request.getSession(true);
		sess.setAttribute("empty_list", "");*/
	}

	/**
	 * @throws IOException
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String search = request.getParameter("search");
		int tieuChiTimKiem = Integer.parseInt(request.getParameter("radTieuchitimkiem"));
		String type = request.getParameter("loaibaibao");
		ArrayList<Page> arrayList=new ArrayList<>();
		HttpSession sess = request.getSession(true);
		try {
			// Khi chạy trên Hadoop Cluster cần có file jar được export từ code
			String jarPath=getServletContext().getRealPath("/")+"jars/MapReduceDriver.jar";
				arrayList=HadoopCluster.checkResultHadoopCluster(jarPath, search, type, tieuChiTimKiem);
				if (arrayList!=null && arrayList.size()>1) {
					System.out.println("Có dữ liệu");
					sess.setAttribute("listPage",arrayList);
				}
				else {
					/*PrintWriter out=response.getWriter();
					 out.println("<script type='text/javascript'>alert('Quá trình chạy Hadoop thất bại hoặc không có dữ liệu tìm thấy') </script>");*/
					 sess.setAttribute("empty_list", "Từ khóa "+search+" không tìm thấy dữ liệu");
				}
				PrintWriter out=response.getWriter();
				out.println("<script type=\"text/javascript\">");  
				out.println("$('.loading').hide();");  
				out.println("</script>"); 
				response.sendRedirect("index.jsp");

		} catch (Exception e) {
			 sess.setAttribute("empty_list", "Quá trình chạy Hadoop MapReduce thất bại ");
			e.printStackTrace();
		}
	
		
	}

}
