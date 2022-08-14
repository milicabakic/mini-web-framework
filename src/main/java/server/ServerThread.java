package server;

import framework.response.JsonResponse;
import framework.response.Response;
import framework.request.enums.Method;
import framework.request.Header;
import framework.request.Helper;
import framework.request.Request;
import framework.request.exceptions.RequestNotValidException;
import ioc.DIEngine;
import routes.ControllerPair;
import routes.HttpPair;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerThread implements Runnable{

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private DIEngine diEngine;

    public ServerThread(Socket socket, DIEngine diEngine){
        this.socket = socket;
        this.diEngine = diEngine;

        try {
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try {

            Request request = this.generateRequest();
            if(request == null) {
                in.close();
                out.close();
                socket.close();
                return;
            }

            Response response;
            if(request.getLocation().equals("/")) {
                // Response example
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("route_location", request.getLocation());
                responseMap.put("route_method", request.getMethod().toString());
                responseMap.put("parameters", request.getParameters());
                response = new JsonResponse(responseMap);
            }
            else{
                response = processTheRequest(request);
            }

            out.println(response.render());

            in.close();
            out.close();
            socket.close();

        } catch (IOException | RequestNotValidException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Response processTheRequest(Request request) {
        Response response = new JsonResponse("Oops something went wrong");
        String location = "";
        if(request.getParameters().size() > 0 && request.getMethod() == Method.GET){
            String arr[] = request.getLocation().split("\\?");
            location = arr[0];
        }
        else{
            location = request.getLocation();
        }
        System.out.println(location);

        ControllerPair pair = null;
        for(HttpPair key : diEngine.getRoutes().keySet()){
            if(location.equals(key.getRoute()) && request.getMethod().toString().equals(key.getMethod())) {
                pair = diEngine.getRoutes().get(key);
                break;
            }
        }

        if(pair != null){
            try {
                //ukoliko je controller vec inicijalizovan
                String className = pair.getClassE().getName();
                if (diEngine.getInitializedControllers().containsKey(className)) {
                    response = (Response) pair.getMethod().invoke(diEngine.getInitializedControllers().get(className), request.getParameters());
                    return response;
                }

                //ukoliko nije, inicijalizujemo ga i dodajemo u mapu
                Object object = diEngine.inject(pair.getClassE());
                diEngine.getInitializedControllers().put(className, object);
                response = (Response) pair.getMethod().invoke(object, request.getParameters());

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        return response;
    }

    private Request generateRequest() throws IOException, RequestNotValidException {
        String command = in.readLine();
        if(command == null) {
            return null;
        }

        String[] actionRow = command.split(" ");
        Method method = Method.valueOf(actionRow[0]);
        String route = actionRow[1];
        Header header = new Header();
        HashMap<String, String> parameters = Helper.getParametersFromRoute(route);

        do {
            command = in.readLine();
            String[] headerRow = command.split(": ");
            if(headerRow.length == 2) {
                header.add(headerRow[0], headerRow[1]);
            }
        } while(!command.trim().equals(""));

        if(method.equals(Method.POST)) {
            int contentLength = Integer.parseInt(header.get("Content-Length"));
            char[] buff = new char[contentLength];
            in.read(buff, 0, contentLength);
            String parametersString = new String(buff);
            System.out.println(parametersString);

            HashMap<String, String> postParameters = Helper.getParametersFromString(parametersString);
            for (String parameterName : postParameters.keySet()) {
                parameters.put(parameterName, postParameters.get(parameterName));
            }
        }

        Request request = new Request(method, route, header, parameters);

        return request;
    }

}
