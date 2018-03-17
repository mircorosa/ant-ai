package ant;

import ant.data.GamesData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public interface GameEventHandler extends EventHandler<ActionEvent> {
	void pushGameData(GamesData gamesData);
}
