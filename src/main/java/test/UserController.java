package test;

import annotations.Controller;
import annotations.GET;
import annotations.Path;
import framework.response.JsonResponse;
import framework.response.Response;

import java.util.*;

@Controller
public class UserController {

    List<String> users;


    public UserController(){
        this.users = Arrays.asList("Milica", "Lazar", "Stefan", "Sanja");
    }

    @Path(location = "/user")
    @GET
    public Response getUser(HashMap<String, String> parameters){
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("route_location", "/user");
        responseMap.put("class", this.getClass().getName());
        responseMap.put("username", users.get(0));
        if(parameters.size() > 0){
            responseMap.put("parameters", parameters);
        }
        Response response = new JsonResponse(responseMap);
        return response;
    }

    @Path(location = "/all-users")
    @GET
    public Response getAllUsers(HashMap<String, String> parameters){
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("route_location", "/all-users");
        responseMap.put("class", this.getClass().getName());
        responseMap.put("all users:", this.users);
        if(parameters.size() > 0){
            responseMap.put("parameters", parameters);
        }
        Response response = new JsonResponse(responseMap);
        return response;
    }
}
