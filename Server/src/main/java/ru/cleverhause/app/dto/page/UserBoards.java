package ru.cleverhause.app.dto.page;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/9/2018.
 */
@Data
public class UserBoards implements Serializable {
    private List<UserBoard> userBoards = Lists.newArrayList();
}
