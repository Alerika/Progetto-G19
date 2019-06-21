package it.unipv.gui.managerarea;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.InputEvent;
import javafx.util.StringConverter;

class CustomTimeSpinner extends Spinner<LocalTime> {

     enum Mode {

        HOURS {
            @Override
            LocalTime increment(LocalTime time, int steps) {
                return time.plusHours(steps);
            }
            @Override
            void select(CustomTimeSpinner spinner) {
                int index = spinner.getEditor().getText().indexOf(':');
                spinner.getEditor().selectRange(0, index);
            }
        },
        MINUTES {
            @Override
            LocalTime increment(LocalTime time, int steps) {
                return time.plusMinutes(steps);
            }
            @Override
            void select(CustomTimeSpinner spinner) {
                int hrIndex = spinner.getEditor().getText().indexOf(':');
                int minIndex = spinner.getEditor().getText().indexOf(':', hrIndex + 1);
                spinner.getEditor().selectRange(hrIndex+1, minIndex);
            }
        };

        abstract LocalTime increment(LocalTime time, int steps);
        abstract void select(CustomTimeSpinner spinner);
        LocalTime decrement(LocalTime time, int steps) {
            return increment(time, -steps);
        }
    }

    private final ObjectProperty<Mode> mode = new SimpleObjectProperty<>(Mode.HOURS) ;

    private CustomTimeSpinner(LocalTime time) {
        setEditable(true);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        StringConverter<LocalTime> localTimeConverter = new StringConverter<LocalTime>() {

            @Override
            public String toString(LocalTime time) {
                return formatter.format(time);
            }

            @Override
            public LocalTime fromString(String string) {
                String[] tokens = string.split(":");
                int hours = getIntField(tokens, 0);
                int minutes = getIntField(tokens, 1) ;
                int totalSeconds = (hours * 60 + minutes) * 60;
                return LocalTime.of((totalSeconds / 3600) % 24, (totalSeconds / 60) % 60);
            }

            private int getIntField(String[] tokens, int index) {
                if (tokens.length <= index || tokens[index].isEmpty()) {
                    return 0 ;
                }
                return Integer.parseInt(tokens[index]);
            }

        };

        TextFormatter<LocalTime> textFormatter = new TextFormatter<>(localTimeConverter, LocalTime.now(), c -> {
            String newText = c.getControlNewText();
            if (newText.matches("[0-9]{0,2}:[0-9]{0,2}")) {
                return c;
            }
            return null;
        });

        SpinnerValueFactory<LocalTime> valueFactory =
                new SpinnerValueFactory<LocalTime>() { {
                    setConverter(localTimeConverter);
                    setValue(time);
                }

            @Override
            public void decrement(int steps) {
                setValue(mode.get().decrement(getValue(), steps));
                mode.get().select(CustomTimeSpinner.this);
            }

            @Override
            public void increment(int steps) {
                setValue(mode.get().increment(getValue(), steps));
                mode.get().select(CustomTimeSpinner.this);
            }

        };

        this.setValueFactory(valueFactory);
        this.getEditor().setTextFormatter(textFormatter);

        this.getEditor().addEventHandler(InputEvent.ANY, e -> {
            int caretPos = this.getEditor().getCaretPosition();
            int hrIndex = this.getEditor().getText().indexOf(':');
            if (caretPos <= hrIndex) {
                mode.set( Mode.HOURS );
            } else {
                mode.set( Mode.MINUTES );
            }
        });

        mode.addListener((obs, oldMode, newMode) -> newMode.select(this));

    }

    CustomTimeSpinner() { this(LocalTime.now()); }
}
