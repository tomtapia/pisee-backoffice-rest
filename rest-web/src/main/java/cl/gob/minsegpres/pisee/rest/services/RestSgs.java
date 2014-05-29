package cl.gob.minsegpres.pisee.rest.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cl.gob.minsegpres.pisee.rest.business.CallerServiceBusiness;
import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;
import cl.gob.minsegpres.pisee.rest.util.AppConstants;
import cl.gob.minsegpres.pisee.rest.util.ConfigProveedoresServicios;
import cl.gob.minsegpres.pisee.rest.util.JSONUtil;
import cl.gob.minsegpres.pisee.rest.util.ParametersName;

@Path("/sgs")
@Component
@Scope("singleton")
public class RestSgs {
	
	private final static Log LOGGER = LogFactory.getLog(RestSgs.class);

	//FIXME: El metodo GET es solo para pruebas, en produccion debe ser POST por la cantidad de parametros
	@GET
//	@POST
	@Path("v1/enviaSolicitudSGS")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String sendToPortalTransaparencia(@QueryParam("pisee_token") String piseeToken,
											@QueryParam("p01") String p01,
											@QueryParam("p02") String p02,
											@QueryParam("p03") String p03,
											@QueryParam("p04") String p04,
											@QueryParam("p05") String p05,
											@QueryParam("p06") String p06,
											@QueryParam("p07") String p07,
											@QueryParam("p08") String p08,
											@QueryParam("p09") String p09,
											@QueryParam("p10") String p10,
											@QueryParam("p11") String p11,
											@QueryParam("p12") String p12,
											@QueryParam("p13") String p13,
											@QueryParam("p14") String p14,
											@QueryParam("p15") String p15,
											@QueryParam("p16") String p16,
											@QueryParam("p17") String p17,
											@QueryParam("p18") String p18,
											@QueryParam("p19") String p19,
											@QueryParam("p20") String p20,
											@QueryParam("p21") String p21,
											@QueryParam("p22") String p22,
											@QueryParam("p23") String p23,
											@QueryParam("p24") String p24,
											@QueryParam("p25") String p25,
											@QueryParam("p26") String p26,
											@QueryParam("p27") String p27,
											@QueryParam("p28") String p28,
											@QueryParam("p29") String p29,
											@QueryParam("p30") String p30,
											@QueryParam("p31") String p31,
											@QueryParam("p32") String p32,
											@QueryParam("p33") String p33,
											@QueryParam("p34") String p34,
											@QueryParam("p35") String p35,
											@QueryParam("p36") String p36,
											@QueryParam("p37") String p37,
											@QueryParam("p38") String p38,
											@QueryParam("p39") String p39,
											@QueryParam("p40") String p40,
											@QueryParam("p41") String p41,
											@QueryParam("p42") String p42,
											@QueryParam("p43") String p43,
											@QueryParam("p44") String p44,
											@QueryParam("p45") String p45,
											@QueryParam("p46") String p46,
											@QueryParam("p47") String p47,
											@QueryParam("p48") String p48,
											@QueryParam("p49") String p49,
											@QueryParam("p50") String p50,
											@QueryParam("p51") String p51
										) {
		
		long startTime = System.currentTimeMillis();
		
		InputParameter inputParameter = new InputParameter();
		JSONUtil jsonUtil = new JSONUtil();
		CallerServiceBusiness restBusiness = new CallerServiceBusiness();
		PiseeRespuesta respuesta;
		
		if (null == p01){p01 = AppConstants.BLANK;}
		if (null == p02){p02 = AppConstants.BLANK;}
		if (null == p03){p03 = AppConstants.BLANK;}
		if (null == p04){p04 = AppConstants.BLANK;}
		if (null == p05){p05 = AppConstants.BLANK;}
		if (null == p06){p06 = AppConstants.BLANK;}
		if (null == p07){p07 = AppConstants.BLANK;}
		if (null == p08){p08 = AppConstants.BLANK;}
		if (null == p09){p09 = AppConstants.BLANK;}
		if (null == p10){p10 = AppConstants.BLANK;}
		
		if (null == p11){p11 = AppConstants.BLANK;}
		if (null == p12){p12 = AppConstants.BLANK;}
		if (null == p13){p13 = AppConstants.BLANK;}
		if (null == p14){p14 = AppConstants.BLANK;}
		if (null == p15){p15 = AppConstants.BLANK;}
		if (null == p16){p16 = AppConstants.BLANK;}
		if (null == p17){p17 = AppConstants.BLANK;}
		if (null == p18){p18 = AppConstants.BLANK;}
		if (null == p19){p19 = AppConstants.BLANK;}
		if (null == p20){p20 = AppConstants.BLANK;}		

		if (null == p21){p21 = AppConstants.BLANK;}
		if (null == p22){p22 = AppConstants.BLANK;}
		if (null == p23){p23 = AppConstants.BLANK;}
		if (null == p24){p24 = AppConstants.BLANK;}
		if (null == p25){p25 = AppConstants.BLANK;}
		if (null == p26){p26 = AppConstants.BLANK;}
		if (null == p27){p27 = AppConstants.BLANK;}
		if (null == p28){p28 = AppConstants.BLANK;}
		if (null == p29){p29 = AppConstants.BLANK;}
		if (null == p30){p30 = AppConstants.BLANK;}
		
		if (null == p31){p31 = AppConstants.BLANK;}
		if (null == p32){p32 = AppConstants.BLANK;}
		if (null == p33){p33 = AppConstants.BLANK;}
		if (null == p34){p34 = AppConstants.BLANK;}
		if (null == p35){p35 = AppConstants.BLANK;}
		if (null == p36){p36 = AppConstants.BLANK;}
		if (null == p37){p37 = AppConstants.BLANK;}
		if (null == p38){p38 = AppConstants.BLANK;}
		if (null == p39){p39 = AppConstants.BLANK;}
		if (null == p40){p40 = AppConstants.BLANK;}		
		
		if (null == p41){p41 = AppConstants.BLANK;}
		if (null == p42){p42 = AppConstants.BLANK;}
		if (null == p43){p43 = AppConstants.BLANK;}
		if (null == p44){p44 = AppConstants.BLANK;}
		if (null == p45){p45 = AppConstants.BLANK;}
		if (null == p46){p46 = AppConstants.BLANK;}
		if (null == p47){p47 = AppConstants.BLANK;}
		if (null == p48){p48 = AppConstants.BLANK;}
		if (null == p49){p49 = AppConstants.BLANK;}
		if (null == p50){p50 = AppConstants.BLANK;}
		
		if (null == p51){p51 = AppConstants.BLANK;}
		
		inputParameter.addBodyParameter("p01", p01);
		inputParameter.addBodyParameter("p02", p02);
		inputParameter.addBodyParameter("p03", p03);
		inputParameter.addBodyParameter("p04", p04);
		inputParameter.addBodyParameter("p05", p05);
		inputParameter.addBodyParameter("p06", p06);
		inputParameter.addBodyParameter("p07", p07);
		inputParameter.addBodyParameter("p08", p08);
		inputParameter.addBodyParameter("p09", p09);
		inputParameter.addBodyParameter("p10", p10);
		
		inputParameter.addBodyParameter("p11", p11);
		inputParameter.addBodyParameter("p12", p12);
		inputParameter.addBodyParameter("p13", p13);
		inputParameter.addBodyParameter("p14", p14);
		inputParameter.addBodyParameter("p15", p15);
		inputParameter.addBodyParameter("p16", p16);
		inputParameter.addBodyParameter("p17", p17);
		inputParameter.addBodyParameter("p18", p18);
		inputParameter.addBodyParameter("p19", p19);
		inputParameter.addBodyParameter("p20", p20);
		
		inputParameter.addBodyParameter("p21", p21);
		inputParameter.addBodyParameter("p22", p22);
		inputParameter.addBodyParameter("p23", p23);
		inputParameter.addBodyParameter("p24", p24);
		inputParameter.addBodyParameter("p25", p25);
		inputParameter.addBodyParameter("p26", p26);
		inputParameter.addBodyParameter("p27", p27);
		inputParameter.addBodyParameter("p28", p28);
		inputParameter.addBodyParameter("p29", p29);
		inputParameter.addBodyParameter("p30", p30);		
		
		inputParameter.addBodyParameter("p31", p31);
		inputParameter.addBodyParameter("p32", p32);
		inputParameter.addBodyParameter("p33", p33);
		inputParameter.addBodyParameter("p34", p34);
		inputParameter.addBodyParameter("p35", p35);
		inputParameter.addBodyParameter("p36", p36);
		inputParameter.addBodyParameter("p37", p37);
		inputParameter.addBodyParameter("p38", p38);
		inputParameter.addBodyParameter("p39", p39);
		inputParameter.addBodyParameter("p40", p40);		
		
		inputParameter.addBodyParameter("p41", p41);
		inputParameter.addBodyParameter("p42", p42);
		inputParameter.addBodyParameter("p43", p43);
		inputParameter.addBodyParameter("p44", p44);
		inputParameter.addBodyParameter("p45", p45);
		inputParameter.addBodyParameter("p46", p46);
		inputParameter.addBodyParameter("p47", p47);
		inputParameter.addBodyParameter("p48", p48);
		inputParameter.addBodyParameter("p49", p49);
		inputParameter.addBodyParameter("p50", p50);
		
		inputParameter.addBodyParameter("p51", p51);		
		
		inputParameter.addBodyParameter(ParametersName.PISEE_TOKEN, piseeToken);
		
		respuesta = restBusiness.callService(ConfigProveedoresServicios.SOAP_SGS__PORTAL_TRANSPARENCIA, inputParameter);
		
		String value = jsonUtil.toJSON(respuesta);
		long endTime = System.currentTimeMillis();		
		LOGGER.info("sendToPortalTransaparencia SGS - TIME == " + (endTime - startTime) + " MILISECONDS");	
		
	    return value;	
	    
	}

}
