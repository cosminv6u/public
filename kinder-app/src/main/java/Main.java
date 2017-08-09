import kinder.KinderService;
import kinder.KinderType;
import org.apache.commons.lang3.StringUtils;

import static spark.Spark.get;
import static spark.SparkBase.port;

/**
 * Created by Cosmin on 8/9/2017.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        port(1234);

        get("/getkindertypes", (req, res) -> {
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Content-Type", "application/json");

            return KinderType.getAllAsString();
        });

        get("/getkindersurprise/:code/:type", (req, res) -> {
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Content-Type", "application/json");

            String code = req.params(":code");
            String typeId = req.params(":type");

            if (StringUtils.isBlank(code) || StringUtils.isBlank(typeId)) {
                return "";
            }

            return KinderService.getSeriesSurpriseByCodeAndType(code, KinderType.getById(typeId));
        });

        get("/getkinderseries/:year/:type", (req, res) -> {
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Content-Type", "application/json");

            String year = req.params(":year");
            String typeId = req.params(":type");

            if (StringUtils.isBlank(year) || StringUtils.isBlank(typeId)) {
                return "";
            }

            return KinderService.getAllSeriesForYearAndType(year, KinderType.getById(typeId));
        });
    }

}
