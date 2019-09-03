package ru.cleverhause.common.persist.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cleverhause.common.persist.api.entity.NewBoardUID;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/10/2018.
 */
public interface NewBoardUidDao extends JpaRepository<NewBoardUID, Long> {
    NewBoardUID findByBoardName(String boardName);

    NewBoardUID findByBoardUID(Long boardUID);
}
