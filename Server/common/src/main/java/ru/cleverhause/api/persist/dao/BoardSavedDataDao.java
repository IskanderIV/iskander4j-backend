package ru.cleverhause.api.persist.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cleverhause.api.persist.entities.BoardSavedData;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/7/2018.
 */
public interface BoardSavedDataDao extends JpaRepository<BoardSavedData, Long> {

}
