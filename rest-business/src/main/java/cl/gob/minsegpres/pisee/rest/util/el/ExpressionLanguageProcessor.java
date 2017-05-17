package cl.gob.minsegpres.pisee.rest.util.el;

import cl.gob.minsegpres.pisee.rest.entities.InputParameter;
import cl.gob.minsegpres.pisee.rest.util.AppConstants;

public class ExpressionLanguageProcessor {

	public String processInput(String inputXml, InputParameter inputParameter){
		StringBuffer bufferOutput = new StringBuffer();
		String[] arrayInput = inputXml.split(AppConstants.PREFIX_EL);
		for (int i = 0; i < arrayInput.length; i++){
			if (i > 0){
				arrayInput[i] = processInputLine(arrayInput[i], inputParameter);
			}
			bufferOutput.append(arrayInput[i]);
		}
		return bufferOutput.toString();
	}
	
	public String processInputLine(String oldLine, InputParameter inputParameter){
		ExpressionLanguageUtil expressionLanguageUtil = new ExpressionLanguageUtil();
		StringBuffer output = new StringBuffer();
		String[] arrayLine;
		Object processedEL;
		arrayLine = oldLine.split(AppConstants.LOWER_THAN);
		processedEL = expressionLanguageUtil.processExpressionLanguage(arrayLine[0], inputParameter);
		output.append(processedEL);
		for(int i = 1; i < arrayLine.length; i++){
			output.append(AppConstants.LOWER_THAN);
			output.append(arrayLine[i]);
		}
		return output.toString();
	}
	
}
