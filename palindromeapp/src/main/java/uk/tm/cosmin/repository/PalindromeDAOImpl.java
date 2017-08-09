package uk.tm.cosmin.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import uk.tm.cosmin.domain.PalindromeRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Cosmin on 7/25/2017.
 */
@Repository
public class PalindromeDAOImpl implements PalindromeDAO {

    private static final Logger LOG = LoggerFactory.getLogger(PalindromeDAOImpl.class);

    // the DataSource can easily be swapped with something else, like Redis
    private List<PalindromeRequest> requests = new ArrayList<PalindromeRequest>();

    @Override
    public void save(PalindromeRequest palindromeRequest) {
        requests.add(0, palindromeRequest);
    }

    @Override
    public List<PalindromeRequest> findAllPalindromes(Integer startRange, Integer rangeSize) {
        synchronized (requests) { // should not need to lock if transactional DataSource would be used
            Integer requestsSize = requests.size();

            if (startRange < 0 ||
                    startRange >= requestsSize ||
                    rangeSize <= 0) {
                return Collections.emptyList();
            }
            Integer endIndex = startRange + rangeSize;
            if (endIndex > requestsSize) {
                endIndex = requestsSize;
            }
            return requests.subList(startRange, endIndex);
        }
    }

}
