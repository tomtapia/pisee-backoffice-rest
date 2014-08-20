package cl.gob.minsegpres.pisee.rest.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cl.gob.minsegpres.pisee.rest.business.CallerServiceBusiness;
import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeEncabezado;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;
import cl.gob.minsegpres.pisee.rest.util.ConfigProveedoresServicios;
import cl.gob.minsegpres.pisee.rest.util.JSONUtil;
import cl.gob.minsegpres.pisee.rest.util.ParametersName;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

@Path("/pruebas")
public class RestFile {

	private final static Log LOGGER = LogFactory.getLog(RestFile.class);

	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/multipleFiles")    
	public String restDemo(@FormDataParam("zfile") List<FormDataBodyPart> zfile){
		String str = new String();
		for (FormDataBodyPart formData: zfile){
			InputStream fileInputStream = formData.getValueAs(InputStream.class);
			String pathName = "E://USR/" + formData.getFormDataContentDisposition().getFileName();
			
			str = str + pathName + " ---- "; 
			
			writeToFile(fileInputStream, pathName);
		}
		return str;
	}
	
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileDetail) {

		String uploadedFileLocation = "E://USR/" + fileDetail.getFileName();
		writeToFile(uploadedInputStream, uploadedFileLocation);
		String output = "File uploaded to : " + uploadedFileLocation;
		return Response.status(200).entity(output).build();

	}

	private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
		try {
			OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------
	@POST
	@Path("/v1/testPost")
	@Produces(MediaType.APPLICATION_JSON)
	public String testPost(MultivaluedMap<String, String> parameter1) {
		String output = " Form parameters :\n";
		for (String key : parameter1.keySet()) {
			output += key + " : " + parameter1.getFirst(key) + "\n";

			System.out.println("key == " + key + " , " + parameter1.getFirst(key));
		}
		System.out.println(output);

		JSONUtil jsonUtil = new JSONUtil();
		PiseeRespuesta respuesta = new PiseeRespuesta();
		PiseeEncabezado encabezado = new PiseeEncabezado();

		encabezado.setEmisorSobre("emisorSobre");
		encabezado.setEstadoSobre("estadoSobre");
		encabezado.setFechaHora("fechaHora");
		encabezado.setGlosaSobre("glosaSobre");
		encabezado.setIdSobre("idSobre");
		encabezado.setNombreConsumidor("nombreConsumidor");
		encabezado.setNombreProveedor("nombreProveedor");

		// encabezado.setNombreServicio(param01);
		// encabezado.setNombreTramite(param02);

		respuesta.setEncabezado(encabezado);

		return jsonUtil.toJSON(respuesta);
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Path("/v1/testPost2")
	//@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
	public String uploadDocFile(@Context HttpServletRequest req) {
		try {
			System.out.println("POST Parameters:");
			Enumeration e = req.getParameterNames();
			while (e.hasMoreElements()) {
				Object key = e.nextElement();
				System.out.println("Key: " + key);
				System.out.println("Val: " + req.getParameter(key.toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		JSONUtil jsonUtil = new JSONUtil();
		PiseeRespuesta respuesta = new PiseeRespuesta();
		PiseeEncabezado encabezado = new PiseeEncabezado();
		encabezado.setEmisorSobre("emisorSobre");
		encabezado.setEstadoSobre("estadoSobre");
		encabezado.setFechaHora("fechaHora");
		encabezado.setGlosaSobre("glosaSobre");
		encabezado.setIdSobre("idSobre");
		encabezado.setNombreConsumidor("nombreConsumidor");
		encabezado.setNombreProveedor("nombreProveedor");
		respuesta.setEncabezado(encabezado);
		return jsonUtil.toJSON(respuesta);		
	}

	
	@POST
	@Path("/v1/testPost3")
	//@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String testPost(@FormParam("param01") String param01, @FormParam("param02") String param02) {
		
		System.out.println("param01 == " + param01);
		System.out.println("param02 == " + param02);
		JSONUtil jsonUtil = new JSONUtil();
		PiseeRespuesta respuesta = new PiseeRespuesta();
		PiseeEncabezado encabezado = new PiseeEncabezado();
		encabezado.setEmisorSobre("emisorSobre");
		encabezado.setEstadoSobre("estadoSobre");
		encabezado.setFechaHora("fechaHora");
		encabezado.setGlosaSobre("glosaSobre");
		encabezado.setIdSobre("idSobre");
		encabezado.setNombreConsumidor("nombreConsumidor");
		encabezado.setNombreProveedor("nombreProveedor");
		encabezado.setNombreServicio(param01);
		encabezado.setNombreTramite(param02);
		respuesta.setEncabezado(encabezado);
		return jsonUtil.toJSON(respuesta);
		
	}	
	// ------------------------------------------------------------------------------------------------------------------------
	
	
	
	@PUT
	@Path("/v1/testPut")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response testPut(@QueryParam("param01") String param01, @QueryParam("param02") String param02, @QueryParam("param03") String param03) {
		String output = "PUT -- Parametro 1: " + param01 + " , Parametro 2: " + param02 + " , Parametro 3: " + param03;
		return Response.status(200).entity(output).build();
	}

	// ------------------------------------------------------------------------------------------------------------------------

	@GET
	@Path("v1/piseeRestListadoEsperaCorazon")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String piseeRestListadoEsperaCorazon(@QueryParam("pisee_token") String piseeToken) {
		long startTime = System.currentTimeMillis();

		JSONUtil jsonUtil = new JSONUtil();
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		InputParameter inputParameter = new InputParameter();

		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);

		respuesta = restBusiness.callService(ConfigProveedoresServicios.REST_PISEE__ISP, inputParameter);
		String value = jsonUtil.toJSON(respuesta);

		long endTime = System.currentTimeMillis();
		LOGGER.info("piseeRestListadoEsperaCorazon - TIME == " + (endTime - startTime) + " MILISECONDS");
		return value;
	}

}