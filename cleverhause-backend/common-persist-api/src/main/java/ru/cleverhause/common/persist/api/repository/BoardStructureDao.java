package ru.cleverhause.common.persist.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cleverhause.common.persist.api.entity.BoardStructure;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/10/2018.
 */
public interface BoardStructureDao extends JpaRepository<BoardStructure, Long> {
}
