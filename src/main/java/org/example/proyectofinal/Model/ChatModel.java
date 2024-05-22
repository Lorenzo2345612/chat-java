package org.example.proyectofinal.Model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class ChatModel {
    private BooleanProperty isAbleToSee = new SimpleBooleanProperty(false);

    public final void setIsAbleToSee(boolean value) {
        isAbleToSee.set(value);
    }

    public final boolean isAbleToSee() {
        return isAbleToSee.get();
    }

    public final BooleanProperty isAbleToSeeProperty() {
        return isAbleToSee;
    }
}
