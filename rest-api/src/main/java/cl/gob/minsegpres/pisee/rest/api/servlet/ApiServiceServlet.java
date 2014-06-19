package cl.gob.minsegpres.pisee.rest.api.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cl.gob.minsegpres.pisee.rest.api.business.ApiBusiness;

public class ApiServiceServlet extends HttpServlet {
	
	private static final Log LOGGER = LogFactory.getLog(ApiServiceServlet.class);
	
	private static final long serialVersionUID = 1L;
       
    public ApiServiceServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	//FIXME: Que solo se puede consumir desde la misma maquina
	private void doProcess(HttpServletRequest request, HttpServletResponse response) {
		try{
	        response.setContentType("text/x-json;charset=UTF-8");           
	        response.setHeader("Cache-Control", "no-cache");
	        
	        String typeClass = request.getParameter("type_class");
	        ApiBusiness apiBusiness = new ApiBusiness();
	        
	        response.getWriter().write(apiBusiness.findJSON(typeClass));
	        
		}catch(Exception e){
			LOGGER.error("Exception - e:" +e.fillInStackTrace());
		}
	}
	

}
