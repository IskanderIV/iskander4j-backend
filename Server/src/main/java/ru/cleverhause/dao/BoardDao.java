package ru.cleverhause.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cleverhause.model.Board;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 5/28/2018.
 */
public interface BoardDao extends JpaRepository<Board, Long> {
    Board findByBoardName(String boardName);
}
