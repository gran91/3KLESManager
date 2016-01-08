package org.kles.fx.indicator;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class SimpleIndicator extends Control {

    public static final String STYLE_CLASS_OFF = "indicator-off";
    public static final String STYLE_CLASS_RED = "indicator-red";
    public static final String STYLE_CLASS_GREEN = "indicator-green";
    public static final String STYLE_CLASS_BLUE = "indicator-blue";
    public static final String STYLE_CLASS_YELLOW = "indicator-yellow";
    public static final String STYLE_CLASS_ORANGE = "indicator-orange";
    public static final String STYLE_CLASS_CYAN = "indicator-cyan";
    public static final String STYLE_CLASS_MAGENTA = "indicator-magenta";
    public static final String STYLE_CLASS_PURPLE = "indicator-purple";
    public static final String STYLE_CLASS_GRAY = "indicator-gray";

    public enum IndicatorStyle {

        OFF(STYLE_CLASS_OFF),
        RED(STYLE_CLASS_RED),
        GREEN(STYLE_CLASS_GREEN),
        BLUE(STYLE_CLASS_BLUE),
        YELLOW(STYLE_CLASS_YELLOW),
        ORANGE(STYLE_CLASS_ORANGE),
        CYAN(STYLE_CLASS_CYAN),
        MAGENTA(STYLE_CLASS_MAGENTA),
        PURPLE(STYLE_CLASS_PURPLE),
        GRAY(STYLE_CLASS_GRAY);

        public final String CLASS;

        private IndicatorStyle(final String CLASS_NAME) {
            CLASS = CLASS_NAME;
        }
    }
    private IndicatorStyle _indicatorStyle = IndicatorStyle.OFF;
    private ObjectProperty<IndicatorStyle> indicatorStyle;

    public SimpleIndicator() {
        getStyleClass().add("indicator");
    }

    public final IndicatorStyle getIndicatorStyle() {
        return null == indicatorStyle ? _indicatorStyle : indicatorStyle.get();
    }

    public final void setIndicatorStyle(final IndicatorStyle INDICATOR_STYLE) {
        if (null == indicatorStyle) {
            _indicatorStyle = INDICATOR_STYLE;
        } else {
            indicatorStyle.set(INDICATOR_STYLE);
        }
    }

    public final ObjectProperty<IndicatorStyle> indicatorStyleProperty() {
        if (null == indicatorStyle) {
            indicatorStyle = new SimpleObjectProperty<>(this, "indicatorStyle", _indicatorStyle);
        }
        return indicatorStyle;
    }

//******************** Style related *************************************
    @Override
    protected Skin createDefaultSkin() {
        return new SimpleIndicatorSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return getClass().getResource("simpleindicator.css").toExternalForm();
    }
}
