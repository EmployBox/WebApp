package isel.ps.employbox.api.model.binder;

import isel.ps.employbox.api.model.ModelBinder;
import isel.ps.employbox.api.model.input.InChat;
import isel.ps.employbox.api.model.output.OutChat;
import isel.ps.employbox.dal.model.Chat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatBinder implements ModelBinder<Chat, OutChat, InChat, Long> {
    @Override
    public List<OutChat> bindOutput(List<Chat> list) {
        return null;
    }

    @Override
    public List<Chat> bindInput(List<InChat> list) {
        return null;
    }

    @Override
    public OutChat bindOutput(Chat object) {
        return null;
    }

    @Override
    public Chat bindInput(InChat object) {
        return null;
    }
}
