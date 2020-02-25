package com.phonebook;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import spark.Request;
import spark.Response;
import spark.Route;

import com.phonebook.model.Model;
import com.phonebook.payloads.Empty;

import java.io.IOException;
import java.util.Map;

public abstract class AbstractRequestHandler<V extends Validable> implements RequestHandler<V>, Route {

    private Class<V> valueClass;
    protected Model model;

    private static final int HTTP_BAD_REQUEST = 400;

    public AbstractRequestHandler(Class<V> valueClass, Model model){
        this.valueClass = valueClass;
        this.model = model;
    }

    public static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(data);
        } catch (IOException e){
            throw new RuntimeException("IOException from StringWriter - dataToJson");
        }
    }

    public final Result process(V value, Map<String, String> urlParams) {
        if (value != null && !value.isValid()) {
            return new Result(HTTP_BAD_REQUEST);
        } else {
            return processImpl(value, urlParams);
        }
    }

    protected abstract Result processImpl(V value, Map<String, String> urlParams);


    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            V value = null;
            if (valueClass != Empty.class) {
                value = objectMapper.readValue(request.body(), valueClass);
            }
            Map<String, String> urlParams = request.params();
            Result result = process(value, urlParams);
            response.status(result.getCode());
            response.type("application/json");
            response.body(result.getBody());
            return result.getBody();
        } catch (JsonMappingException e) {
            response.status(Constants.HTTP_BAD_REQUEST);
            response.body(e.getMessage());
            return e.getMessage();
        }
    }
}
