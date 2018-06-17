package ru.cleverhause.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cleverhause.model.Board;

import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 5/28/2018.
 */
public interface BoardDao extends JpaRepository<Board, Long> {
    List<Board> findByBoardname(String boardname);

    Board findByBoardUID(Long boardUID);
}
