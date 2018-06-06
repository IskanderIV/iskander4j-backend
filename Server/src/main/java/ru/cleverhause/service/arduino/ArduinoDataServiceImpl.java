package ru.cleverhause.service.arduino;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.cleverhause.rest.dto.ArduinoJSON;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/2/2017.
 */
@Service
public class ArduinoDataServiceImpl implements ArduinoDataService {

    private Map<String, ArduinoJSON> arduinoDataRepository = new HashMap<>();

    private static final Logger logger = Logger.getLogger(ArduinoDataServiceImpl.class);

    @Override
    public boolean put(String key, ArduinoJSON arduinoJSON) {
        arduinoDataRepository.put(key, arduinoJSON);
        logger.info("PUT operation");
        return true;
    }

    @Override
    public ArduinoJSON getLast() {
        int capacity = arduinoDataRepository.size();
        if (capacity > 0) {
            for (String key : arduinoDataRepository.keySet()) {
                return arduinoDataRepository.get(key);
            }
        }
        logger.info("Get operation");
        return null;
    }
}
