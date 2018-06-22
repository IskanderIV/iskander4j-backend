package ru.cleverhause.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cleverhause.model.BoardControlData;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/10/2018.
 */
public interface BoardControlDataDao extends JpaRepository<BoardControlData, Long> {
    BoardControlData findByBoardId(Long boardId);
}
