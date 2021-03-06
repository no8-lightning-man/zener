package com.n8lm.zener.sdk;

import com.n8lm.zener.math.EditableCurveFunction;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Created by Alchemist0823 on 7/12/2015.
 */
public class SingleFunctionEditor extends FunctionEditorBase {

    protected ObjectProperty<EditableCurveFunction> value;

    public ObjectProperty<EditableCurveFunction> functionValueProperty() {
        return value;
    }

    public final void setFunctionValue(EditableCurveFunction value) {
        functionValueProperty().set(value);
    }

    public final EditableCurveFunction getFunctionValue() {
        return functionValueProperty().get();
    }

    public SingleFunctionEditor() {
        super();

        this.value = new SimpleObjectProperty<>(this, "value", new EditableCurveFunction());
        functionValueProperty().addListener((observable, oldValue, newValue) -> {
            functionView.replace(oldValue, newValue);
        });
        functionView.addFunction(getFunctionValue());
    }
}
