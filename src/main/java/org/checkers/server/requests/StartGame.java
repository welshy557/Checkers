package org.checkers.server.requests;

import java.awt.*;
import java.io.Serializable;

public record StartGame(Color playersColor) implements Serializable {
}
