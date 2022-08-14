package test;

import annotations.Controller;
import annotations.GET;
import annotations.POST;
import annotations.Path;
import annotations.di.Autowired;
import framework.response.JsonResponse;
import framework.response.Response;
import test.testttt.MultipleInterfaceClass;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @Autowired(verbose = true)
    MultipleInterfaceClass multipleInterfaceClass;
    String value;

    public TestController(){
    }

    @Path(location = "/test")
    @GET
    public Response testGET(HashMap<String, String> parameters){
        System.out.println(this.multipleInterfaceClass);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("route_location", "/test");
        responseMap.put("method", "testGET()");
        responseMap.put("class", this.getClass().getName());
        if(parameters.size() > 0){
            responseMap.put("parameters", parameters);
        }
        Response response = new JsonResponse(responseMap);
        return response;
    }

    @Path(location = "/test")
    @POST
    public Response testPOST(HashMap<String, String> parameters){
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("route_location", "/test");
        responseMap.put("method", "testPOST()");
        responseMap.put("class", this.getClass().getName());
        responseMap.put("parameters", parameters);
        Response response = new JsonResponse(responseMap);
        return response;
    }

}
