package ru.cleverhause.devices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cleverhause.common.persist.api.entity.Board;

import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 5/28/2018.
 */
public interface BoardDao extends JpaRepository<Board, Long> {
    List<Board> findByBoardName(String boardName);

    Board findByBoardUID(Long boardUID);
}
