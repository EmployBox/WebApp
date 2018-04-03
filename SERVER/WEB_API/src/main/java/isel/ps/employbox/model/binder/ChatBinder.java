package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.ModelBinder;
import isel.ps.employbox.model.input.InChat;
import isel.ps.employbox.model.output.OutChat;
import isel.ps.employbox.model.entities.Chat;
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
