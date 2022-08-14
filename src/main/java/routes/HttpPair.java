package routes;

public class HttpPair {

    String route;
    String method;


    public HttpPair(){

    }

    public HttpPair(String route, String method){
        this.route = route;
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public String getRoute() {
        return route;
    }
}
