package ru.cleverhause.service.board.converter;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.cleverhause.api.dto.form.NewBoardUidForm;
import ru.cleverhause.api.persist.entities.NewBoardUID;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/10/2018.
 */

@Component
public class NewBoardUID_To_NewBoardUidFormConverter implements Converter<NewBoardUID, NewBoardUidForm> {

    @Nullable
    @Override
    public NewBoardUidForm convert(@Nullable NewBoardUID newBoardUID) {
        if (newBoardUID == null) {
            return null;
        }

        NewBoardUidForm form = new NewBoardUidForm();
        form.setNewBoardUid(ObjectUtils.toString(newBoardUID.getBoardUID()));
        form.setBoardName(newBoardUID.getBoardName());

        return form;
    }
}
