package rs.edu.raf.msa.game.service;

import org.springframework.data.relational.core.mapping.event.AbstractRelationalEventListener;
import org.springframework.data.relational.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import rs.edu.raf.msa.game.entity.Play;

@Component
@RequiredArgsConstructor
public class PlayListener extends AbstractRelationalEventListener<Play> {

    final PlaySender playSender;
    
    @Override
    protected void onAfterSave(AfterSaveEvent<Play> event) {
        super.onAfterSave(event);
        playSender.sendGameScore(event.getEntity());
    }

}