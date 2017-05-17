function drawLinks(){
	jQuery.getJSON('/rest-api/ApiServiceServlet',{type_class:'api_links'},function(resultData){
		var apidivHTML = "";
		var apiLink = "";
		apidivHTML += "<h4>Links Utiles</h4>";
		apidivHTML += "<table class=\"table table-condensed table-striped table-bordered\">";
		apidivHTML += "	<tbody>";

		apidivHTML += "	<tr>";	
		apidivHTML += "		<th>Estructura Respuesta PISEE</th>";     	
		apidivHTML += "		<td><a href=\"/rest-api/estructura_respuesta.html\">Estructura Respuesta</a></td>";	
		apidivHTML += "	</tr>";
		apidivHTML += "	<tr>";	
		apidivHTML += "		<th>Errores PISEE</th>";     	
		apidivHTML += "		<td><a href=\"/rest-api/errores_pisee.html\">Errores PISEE</a></td>";	
		apidivHTML += "	</tr>";
		
		for (var i = 0; i < resultData.length; i++ ){
			apiLink = "";
			apiLink += "	<tr>";
			apiLink += "		<th>" + resultData[i].name + "</th>";
			apiLink += "     	<td><a href=\"" + resultData[i].value + "\" id=\"" + resultData[i].id + "\">" + resultData[i].value + "</a></td>";
			apiLink += "	</tr>";
			apidivHTML += apiLink;
		}
		apidivHTML += "	</tbody>";
		apidivHTML += "</table>";
		$("#divLinks").html(apidivHTML);
	});
}

function drawServices(pintDivCenter){
	jQuery.getJSON('/rest-api/ApiServiceServlet',{type_class:'api_services'},function(resultData){
    	//-- LEFT DIV
		var apidivHTML = "";		
		apidivHTML += "<ul class=\"nav nav-list\">";
		apidivHTML += "<li class=\"nav-header\">APIs</li>";
		for (var i = 0; i < resultData.length; i++ ){
			apidivHTML += "<li><a href=\"javascript:void(0)\" id=\"" + resultData[i].id + "\">" + resultData[i].name + "</a></li>";
		}
		apidivHTML += "</ul>";
		$("#apidiv").html(apidivHTML);

		//-- BINDING
		$("#apidiv a").each(function() {
			$(this).click(function() { 
				for (var i = 0; i < resultData.length; i++ ){
					if (resultData[i].id == this.id){
						drawCenter(resultData[i]);								
					}
				}
			});
		});			
		
		//-- CENTER DIV
		if (pintDivCenter){
			drawCenter(resultData[0]);
		}
		
	});		
}

function drawCenter(jsonApi){
	var divContentHTML, divAccordionHTML, apiMethod, i, j;
	apiMethod = "";
	divAccordionHTML = "";
	divContentHTML = "";
	divContentHTML += "<blockquote>";
	divContentHTML += "	<p style=\"text-transform: uppercase;\">";
	divContentHTML += "		<span id=\"apiName\">" + jsonApi.id + "</span>";
	divContentHTML += "	</p>";
	divContentHTML += "	<small>";
	divContentHTML += "		<span id=\"apiDescription\">" + jsonApi.description + "</span>";
	divContentHTML += "	</small>";				
	divContentHTML += "</blockquote>";
	$("#content").html("<div class=\"accordion\" id=\"accordion\">");
	for (i = 0; i < jsonApi.methods.length; i++ ){
		apiMethod = jsonApi.methods[i];
		divAccordionHTML = "";					
		divAccordionHTML += "<div class=\"accordion-group\">";
		divAccordionHTML += "	<div class=\"accordion-heading\">";
		divAccordionHTML += "		<span class=\"label pull-right " + apiMethod.request.method + "\">" + apiMethod.request.method + "</span>";
		divAccordionHTML += "		<a href=\"#_" + apiMethod.id + "\" id=\"" + apiMethod.id + "\"";
		divAccordionHTML += "			rel=\"method\" data-parent=\"#accordion\" data-toggle=\"collapse\" class=\"accordion-toggle\"> " + apiMethod.name + " </a>";
		divAccordionHTML += "	</div>";
		divAccordionHTML += "</div>";
		divAccordionHTML += "<div class=\"accordion-body collapse\" id=\"_" + apiMethod.id + "\">";
		divAccordionHTML += "	<div class=\"accordion-inner\">";
		divAccordionHTML += "		<table class=\"table table-condensed table-striped table-bordered\">";
		divAccordionHTML += "				<tr>";
		divAccordionHTML += "					<th>Descripcion</th>";
		divAccordionHTML += "					<td>" + apiMethod.description + "</td>";
		divAccordionHTML += "				</tr>";			
		divAccordionHTML += "				<tr>";
		divAccordionHTML += "					<th>Metodo</th>";
		divAccordionHTML += "					<td><span class=\"label " + apiMethod.request.method + "\">" + apiMethod.request.method + "</span></td>";
		divAccordionHTML += "				</tr>";
		divAccordionHTML += "				<tr>";
		divAccordionHTML += "					<th>Version</th>";
		divAccordionHTML += "					<td><code>" + apiMethod.request.version + "</code></td>";
		divAccordionHTML += "				</tr>";												
		divAccordionHTML += "				<tr>";
		divAccordionHTML += "					<th>Path</th>";
		divAccordionHTML += "					<td><code>" + apiMethod.request.path + "</code></td>";
		divAccordionHTML += "				</tr>";
		divAccordionHTML += "				<tr>";
		divAccordionHTML += "					<th>Ejemplo URI</th>";
		divAccordionHTML += "					<td>" + apiMethod.request.exampleURI + "</td>";
		divAccordionHTML += "				</tr>";					
		divAccordionHTML += "				<tr>";
		divAccordionHTML += "					<th>Produces</th>";
		divAccordionHTML += "					<td><code>" + apiMethod.response.produces + "</code></td>";
		divAccordionHTML += "				</tr>";				
		divAccordionHTML += "				<tr>";
		divAccordionHTML += "						<table class=\"table table-condensed table-striped table-bordered\">";
		divAccordionHTML += "							<thead>	";
		divAccordionHTML += "								<tr>";
		divAccordionHTML += "									<th align=\"center\" colspan=\"4\">Parametros de Entrada</th>";
		divAccordionHTML += "								</tr>";
		divAccordionHTML += "							</thead>";					
		divAccordionHTML += "							<tr>";
		divAccordionHTML += "								<td style=\"font-weight: bold;\">Nombre</td>";
		divAccordionHTML += "								<td style=\"font-weight: bold;\">Descripcion</td>";
		divAccordionHTML += "								<td style=\"font-weight: bold;\">Obligatorio</td>";					
		divAccordionHTML += "								<td style=\"font-weight: bold;\">Tipo</td>";
		divAccordionHTML += "							</tr>";
		var tmpParametersHTML = "";
		for (j = 0; j < apiMethod.request.parameters.length; j++ ){
			tmpParametersHTML += "						<tr id=\"" +  apiMethod.request.parameters[j].id + "\">";
			tmpParametersHTML += "							<td>" + apiMethod.request.parameters[j].name + "</td>";
			tmpParametersHTML += "							<td>" + apiMethod.request.parameters[j].description + "</td>";
			tmpParametersHTML += "							<td>" + apiMethod.request.parameters[j].required + "</td>";
			tmpParametersHTML += "							<td>" + apiMethod.request.parameters[j].type + "</td>";
			tmpParametersHTML += "						</tr>";
		}
		divAccordionHTML += tmpParametersHTML;
		divAccordionHTML += "						</table>";
		divAccordionHTML += "				</tr>";
		divAccordionHTML += "				<tr>";
		divAccordionHTML += "						<table class=\"table table-condensed table-striped table-bordered\">";
		divAccordionHTML += "							<thead>	";
		divAccordionHTML += "								<tr>";
		divAccordionHTML += "									<th align=\"center\" colspan=\"3\">Parametros de Salida</th>";
		divAccordionHTML += "								</tr>";
		divAccordionHTML += "							</thead>";					
		divAccordionHTML += "							<tr>";
		divAccordionHTML += "								<td style=\"font-weight: bold;\">Nombre</td>";
		divAccordionHTML += "								<td style=\"font-weight: bold;\">Descripcion</td>";										
		divAccordionHTML += "								<td style=\"font-weight: bold;\">Tipo</td>";
		divAccordionHTML += "							</tr>";
		var tmpParametersHTML = "";
		for (j = 0; j < apiMethod.response.parameters.length; j++ ){
			tmpParametersHTML += "						<tr id=\"" +  apiMethod.response.parameters[j].id + "\">";
			tmpParametersHTML += "							<td>" + apiMethod.response.parameters[j].name + "</td>";
			tmpParametersHTML += "							<td>" + apiMethod.response.parameters[j].description + "</td>";						
			tmpParametersHTML += "							<td>" + apiMethod.response.parameters[j].type + "</td>";
			tmpParametersHTML += "						</tr>";
		}
		divAccordionHTML += tmpParametersHTML;
		divAccordionHTML += "						</table>";
		divAccordionHTML += "				</tr>";
		divAccordionHTML += "				<tr>";
		divAccordionHTML += "					<th>Ejemplo de Respuesta</th>";
		divAccordionHTML += "					<td><pre>" + JSON.stringify(JSON.parse(apiMethod.response.example), null, '\t') + "</pre></td>";
		divAccordionHTML += "				</tr>";
		divAccordionHTML += "		</table>";
		divAccordionHTML += "	</div>";
		divAccordionHTML += "</div>";
		divContentHTML += divAccordionHTML;
	}
	
	divContentHTML += "</div>";
	$("#content").html(divContentHTML);
	
}