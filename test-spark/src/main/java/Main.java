import static spark.Spark.get;
import static spark.SparkBase.port;
import static spark.SparkBase.staticFileLocation;

/**
 * Created by Cosmin on 8/9/2017.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        port(1234);

        get("/", (req, res) -> {
            return "Hello World!";
        });
    }

}
