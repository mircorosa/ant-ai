package ant;

import ant.data.TestData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public interface GameEventHandler extends EventHandler<ActionEvent> {
	void pushGameData(TestData testData);
}
