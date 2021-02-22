package senberg.portals.jaxwsobject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("jsonCalculator")
public class JsonCalculator {

    @GET
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    public Result add(@QueryParam("a") int a, @QueryParam("b") int b){
        return new Result(a + b);
    }

    public static class Result{
        int output;

        public Result(){};

        public Result(int output){
            this.output = output;
        }

        public int getOutput() {
            return output;
        }

        public void setOutput(int output) {
            this.output = output;
        }
    }
}
