package cl.gob.minsegpres.pisee.rest.util;

import cl.gob.minsegpres.pisee.rest.entities.response.PiseeRespuesta;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class PiseeRESTExclusionStrategy implements ExclusionStrategy {

    public boolean shouldSkipClass(Class<?> arg0) {
        return false;
    }

    public boolean shouldSkipField(FieldAttributes f) {
        return (f.getDeclaringClass() == PiseeRespuesta.class && 
        		(f.getName().equals("responseRest") || f.getName().equals("temporalData")) );
    }

}